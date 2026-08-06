package server.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import websocket.WebSocketGson;
import websocket.commands.*;
import websocket.messages.*;
import io.javalin.websocket.WsContext;

public class WebSocketHandler {
    private final Gson gson = WebSocketGson.create();
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void onMessage(WsContext context, String message) {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        try {
            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(context, command);
                case MAKE_MOVE -> handleMakeMove(context, (MakeMoveCommand) gson.fromJson(message, MakeMoveCommand.class));
                case LEAVE -> handleLeave(context, command);
                case RESIGN -> handleResign(context, command);
            }
        } catch (Exception exception) {
            context.send(gson.toJson(new ErrorMessage("Error: " + exception.getMessage())));
        }
    }

    private void handleConnect(WsContext context, UserGameCommand command) throws DataAccessException {
        var auth = authDAO.getAuth(command.getAuthToken());
        if (auth == null) {
            context.send(gson.toJson(new ErrorMessage("Error: unauthorized")));
            return;
        }
        GameData game = gameDAO.getGame(command.getGameID());
        if (game == null) {
            context.send(gson.toJson(new ErrorMessage("Error: bad game ID")));
            return;
        }

        connections.add(command.getAuthToken(), command.getGameID(), context);
        context.send(gson.toJson(new LoadGameMessage(game.game())));
        String role = auth.username().equals(game.whiteUsername()) ? "white"
                : auth.username().equals(game.blackUsername()) ? "black" : "observer";
        String note = "%s connected as %s".formatted(auth.username(), role);
        connections.broadcast(command.getGameID(), command.getAuthToken(), new NotificationMessage(note));
    }

    private void handleMakeMove(WsContext context, MakeMoveCommand command) throws DataAccessException {
        var auth = authDAO.getAuth(command.getAuthToken());
        GameData game = gameDAO.getGame(command.getGameID());

        // TODO: validate its this players turn and move is legal
        // game.game().makeMove(command.getMove());   // throws InvalidMoveException
        // gameDAO.updateGame(game with updated ChessGame);

        connections.broadcast(command.getGameID(), null, new LoadGameMessage(game.game()));

        String note = auth.username() + " moved " + command.getMove();
        connections.broadcast(command.getGameID(), command.getAuthToken(), new NotificationMessage(note));

        // if (game.game().isInCheckmate(...)) connections.broadcast(gameID, null, new NotificationMessage(...));
    }

    private void handleLeave(WsContext context, UserGameCommand command) throws DataAccessException {
        handleGameEnd(context, command, " left the game");
    }

    private void handleResign(WsContext context, UserGameCommand command) throws DataAccessException {
        handleGameEnd(context, command, " resigned");
    }

    private void handleGameEnd(WsContext context, UserGameCommand command, String message) throws DataAccessException {
        var auth = authDAO.getAuth(command.getAuthToken());

        connections.broadcast(command.getGameID(), null, new NotificationMessage(auth.username() + message));
    }
}
