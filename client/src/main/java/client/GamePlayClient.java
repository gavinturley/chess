package client;

import chess.*;
import ui.BoardPrinter;
import websocket.messages.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamePlayClient {
    private final ServerFacade serverFacade;
    private final Repl repl;

    private WebSocketFacade webSocketFacade;
    private ChessGame currentGame;
    private ChessGame.TeamColor playerColor;
    private Integer gameID;
    private boolean gameOver = false;

    public GamePlayClient(ServerFacade serverFacade, Repl repl) {
        this.serverFacade = serverFacade;
        this.repl = repl;
    }

    public void start(int gameID, ChessGame.TeamColor playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.gameOver = false;
        try {
            webSocketFacade = new WebSocketFacade(serverFacade.getServerUrl(), this::handleServerMessage);
            webSocketFacade.connect(repl.getAuthToken(), gameID);
        } catch (ResponseException e) {
            System.out.println("Error: unable to connect - " + e.getMessage());
            repl.setState(State.SIGNED_IN);
        }
    }

    private void handleServerMessage(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> {
                currentGame = ((LoadGameMessage) message).getGame();
                System.out.println(redrawChessBoardQuiet());
            }
            case NOTIFICATION -> {
                System.out.println(((NotificationMessage) message).getMessage());
            }
            case ERROR -> {
                System.out.println(((ErrorMessage) message).getErrorMessage());
            }
            repl.printPrompt();
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.trim().split("\\s+");
            var command = tokens.length > 0 ? tokens[0].toLowerCase() : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "redraw" -> redrawChessBoard();
                case "leave" -> leave();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves(params);
                case "help" -> help();
                default -> "Unknown command.\n" + help();
            };
        } catch (ResponseException exception) {
            return exception.getMessage();
        } catch (Exception exception) {
            return "Error: unable to process command.";
        }
    }

    private String redrawChessBoard() throws ResponseException {
        return redrawChessBoardQuiet();
    }

    private String redrawChessBoardQuiet() {
        if (currentGame ==  null) {
            return "Board not loaded yet.\n";
        }
        ChessGame.TeamColor perspective = (playerColor != null) ? playerColor : ChessGame.TeamColor.WHITE;
        return BoardPrinter.drawBoard(currentGame.getBoard(), perspective);
    }

    private String leave() throws ResponseException {
        webSocketFacade.leave(repl.getAuthToken(), gameID);
        gameID = null;
        currentGame = null;
        playerColor = null;
        repl.setState(State.SIGNED_IN);
        return repl.getUsername() + " left the game.\n";
    }

    private String makeMove(String... params) throws ResponseException {
        if (gameOver) {
            return "The game is over. No moves can be made.\n";
        }
        if (playerColor == null) {
            return "Observers cannot make moves.\n";
        }
        if (params.length != 2 && params.length != 3) {
            return "Expected move <FROM> <TO> [PROMOTION] (e.g. move e2 e4 or move e7 e8 queen)\n";
        }

        ChessPosition start = parsePosition(params[0]);
        ChessPosition end = parsePosition(params[1]);

        if (start == null || end == null) {
            return "Invalid square. Use chess notation like b3.\n";
        }

        ChessPiece.PieceType promotion = null;
        if (params.length == 3) {
            promotion = parsePromotion(params[2]);
            if (promotion == null) {
                return "Invalid promotion piece. Use queen, rook, bishop, or knight.\n";
            }
        }

        webSocketFacade.makeMove(repl.getAuthToken(), gameID, new ChessMove(start, end, promotion));
        return "";
    }
}