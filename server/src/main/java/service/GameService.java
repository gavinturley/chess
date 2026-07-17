package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import server.CreateGameResult;
import server.GameSummary;
import server.JoinGameRequest;
import server.ListGameResult;

import java.util.Collection;

public class GameService {
    final private GameDAO gameDAO;
    final private AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGameResult listGames(String authToken) throws DataAccessException {
        requireAuth(authToken);
        var summaries = gameDAO.listGames().stream().map(GameSummary::from)toList();
        return new ListGameResult((Collection<GameSummary>) summaries);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws DataAccessException {
        requireAuth(authToken);
        if (isBlank(request.gameName())){
            throw new BadRequestException("Error: bad request");
        }

        var game = new GameData(0, null, null, request.gameName(), new ChessGame());
        int gameID = gameDAO.createGame(game);

        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException {
        var auth = requireAuth(authToken);

        if (!isValidColor(request.playerColor())){
            throw new BadRequestException("Error: bad request");
        }

        var game = gameDAO.getGame(request.gameID());
        if (game == null){
            throw new BadRequestException("Error: bad request");
        }

        GameData updated;
        if (request.playerColor().equalsIgnoreCase("WHITE")){
            if (game.whiteUsername() != null){
                throw new AlreadyTakenException("Error: already taken");
            }
            updated = game.withWhiteUsername(auth.username());
        } else {
            if (game.blackUsername() != null){
                throw new AlreadyTakenException("Error: already taken");
            }
            updated = game.withBlackUsername(auth.username());
        }

        gameDAO.updateGame(updated);
    }

    private AuthData requireAuth(String authToken) throws DataAccessException {
        if (authToken == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        var auth = authDAO.getAuth(authToken);
        if (auth == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        return auth;
    }

    private static boolean isValidColor(String color) {
        return "WHITE".equalsIgnoreCase(color) || "BLACK".equalsIgnoreCase(color);
    }

    private static boolean isBlank(String string){
        return string == null || string.isBlank();
    }
}
