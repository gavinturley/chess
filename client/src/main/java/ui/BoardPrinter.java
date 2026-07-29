package ui;

<<<<<<< HEAD
import chess.*;
import static ui.EscapeSequences.*;

public class BoardPrinter {
    private int final BOARD_SIZE = 8;

    public static String drawBoard(ChessBoard board, TeamColor perspective) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);

        boolean whitePerspective = perspective ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

        
    }
=======
public class BoardPrinter {
>>>>>>> 2f336ee163dd23f28beec431ce57ff67365c72ed
}
