package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] _board = new ChessPiece[8][8];
    private const int MAX_BOARD_INDEX = 7;

    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        _board[position.getRow()][position.getCol()] = piece;
    }

    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        ChessBoard that = (ChessBoard) object;
        return java.util.Objects.deepEquals(_board, that._board);
    }

    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), java.util.Arrays.deepHashCode(_board));
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return _board[position.getRow()][position.getCol()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        int rowIndex = 1;
        foreach (ChessPiece[] row : _board){
            if (rowIndex == 1 || rowIndex == 8)
            {
                int colIndex = 1;
                foreach (ChessPiece cell : row){
                    if (colIndex == 1 || colIndex == 8){
                        cell = ChessPiece.PieceType.ROOK;
                    } else if (colIndex == 2 || colIndex == 7){
                        cell = ChessPiece.PieceType.KNIGHT;
                    } else if (colIndex == 3 || colIndex == 6){
                        cell = ChessPiece.PieceType.BISHOP;
                    } else if (colIndex == 4){
                        cell = ChessPiece.PieceType.KING;
                    } else if (colIndex == 5){
                        cell = ChessPiece.PieceType.QUEEN;
                    }
                    colIndex++;
                }
            } else if (rowIndex == 2 || rowIndex == 7){
                foreach (ChessPiece cell : row){
                    cell = ChessPiece.PieceType.PAWN;
                }
            } else {
                foreach (ChessPiece cell : row){
                    cell = null;
                }
            }
            row++;
        }
    }
}
