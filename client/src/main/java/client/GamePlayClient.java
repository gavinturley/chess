package client;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;
import ui.BoardPrinter;
import java.util.Arrays;

public class GamePlayClient {
    private final ServerFacade serverFacade;
    private final Repl repl;

    public GamePlayClient(ServerFacade serverFacade, Repl repl){
        this.serverFacade = serverFacade;
        this.repl = repl;
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
                default -> "Unknown command. \n" + help();
            };
        } catch (ResponseException exception){
            return exception.getMessage();
        } catch (Exception exception){
            return "Error: unable to process command.";
        }
    }

    private String redrawChessBoard() throws ResponseException {}

    private String leave() throws ResponseException {}

    /* Allow the user to input what move they want to make.
    The board is updated to reflect the result of the move,
    and the board automatically updates on all clients involved in the game.*/
    private String makeMove(String... params) throws ResponseException {

    }

    /* Prompts the user to confirm they want to resign.
    If they do, the user forfeits the game and the game is over.
    Does not cause the user to leave the game. */
    private String resign() throws ResponseException {

    }

    /* Allows the user to input the piece for which they want to highlight legal moves.
    The selected piece’s current square and all squares it can legally move to are highlighted.
    This is a local operation and has no effect on remote users’ screens. */
    private String highlightLegalMoves(String... params) throws ResponseException {}

    private String help() {
        return """
                Commands:
                  redraw - redraws the chess board
                  leave	- removes the user from the game
                  move - input what move they want to make
                  resign - confirms input then forfeits the game
                  highlight	- inputs the piece for which they want to highlight legal moves
                  help - display this help text
                """;
    }
}
