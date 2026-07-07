package chess;

import java.util.Arrays;
import java.util.Objects;

//done
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] chessBoard;

    public ChessBoard() {
        chessBoard = new ChessPiece[8][8];
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }

    public ChessBoard copy(){
        ChessBoard newBoard = new ChessBoard();
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                ChessPosition position = new ChessPosition(row, col);
                newBoard.addPiece(position, chessBoard[row][col]);
            }
        }
        return newBoard;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // blank board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                chessBoard[row][col] = null;
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                ChessGame.TeamColor currentColor;
                if (row < 2) currentColor = ChessGame.TeamColor.WHITE;
                else currentColor = ChessGame.TeamColor.BLACK;

                if (row == 1 || row == 6) chessBoard[row][col] = new ChessPiece(currentColor, ChessPiece.PieceType.PAWN);
                else if (row == 0 || row == 7){
                    if (col == 0 || col == 7) chessBoard[row][col] = new ChessPiece(currentColor, ChessPiece.PieceType.ROOK);
                    else if (col == 1 || col == 6) chessBoard[row][col] = new ChessPiece(currentColor, ChessPiece.PieceType.KNIGHT);
                    else if (col == 2 || col == 5) chessBoard[row][col] = new ChessPiece(currentColor, ChessPiece.PieceType.BISHOP);
                    else if (col == 3) chessBoard[row][col] = new ChessPiece(currentColor, ChessPiece.PieceType.QUEEN);
                    else chessBoard[row][col] = new ChessPiece(currentColor, ChessPiece.PieceType.KING);
                }
            }
        }
    }
}
