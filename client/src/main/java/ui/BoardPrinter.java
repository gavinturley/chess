package ui;

import chess.*;
import static ui.EscapeSequences.*;

public class BoardPrinter {

    private static final int BOARD_SIZE = 8;

    public static String drawBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);

        boolean whitePerspective = perspective != ChessGame.TeamColor.BLACK;

        stringBuilder.append(columnHeaders(whitePerspective));

        int startRow = whitePerspective ? BOARD_SIZE : 1;
        int endRow = whitePerspective ? 1 : BOARD_SIZE;
        int rowStep = whitePerspective ? -1 : 1;

        for (int row = startRow; whitePerspective ? row <= endRow: row >= endRow; row += rowStep){
            stringBuilder.append(rowLabel(row));

            int startCol = whitePerspective ? 1 : BOARD_SIZE;
            int endCol = whitePerspective ? BOARD_SIZE : 1;
            int colStep = whitePerspective ? 1 : -1;

            for (int col = startCol; whitePerspective ? col <= endCol: col >= endCol; col += colStep){
                boolean isWhite = (row + col) % 2 == 0;
                stringBuilder.append(isWhite ? SET_BG_COLOR_WHITE : SET_BG_COLOR_DARK_GREY);
                stringBuilder.append(pieceAt(board, row, col));
                stringBuilder.append(RESET_BG_COLOR);
            }

            stringBuilder.append(rowLabel(row)).append(RESET_TEXT_COLOR).append("\n");
        }

        stringBuilder.append(columnHeaders(whitePerspective));
        return stringBuilder.toString();
    }

    private static String pieceAt(ChessBoard board, int row, int col){
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null){
            return EMPTY;
        }

        boolean isTan = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        String color = isTan ? SET_TEXT_COLOR_TAN : SET_BG_COLOR_BLACK;
        String pieceImg = switch (piece.getPieceType()){
            case KING -> isTan ? WHITE_KING : BLACK_KING;
            case QUEEN -> isTan ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> isTan ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isTan ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> isTan ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> isTan ? WHITE_PAWN : BLACK_PAWN;
        }
    }
}