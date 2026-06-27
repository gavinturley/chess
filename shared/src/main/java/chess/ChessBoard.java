package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] _board = new ChessPiece[8][8];
    private final int MAX_BOARD_INDEX = 7;

    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        _board[position.getRow()][position.getColumn()] = piece;
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
        return _board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
            for (int rowIndex = 0; rowIndex < _board.length; rowIndex++){
                for (int colIndex = 0; colIndex < _board[0].length; colIndex++){
                    int row = rowIndex + 1;
                    int col = colIndex + 1;

                    ChessGame.TeamColor color = (row <= 2) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    if (row == 1 || row == 8){
                        ChessPiece.PieceType type;
                        if (col == 1 || col == 8) type = ChessPiece.PieceType.ROOK;
                        else if (col == 2 || col == 7) type = ChessPiece.PieceType.KNIGHT;
                        else if (col == 3 || col == 6) type = ChessPiece.PieceType.BISHOP;
                        else if (col == 4) type = ChessPiece.PieceType.QUEEN;
                        else type = ChessPiece.PieceType.KING;

                        _board[rowIndex][colIndex] = new ChessPiece(color, type);
                    } else if (row == 2 || row == 7){
                        _board[rowIndex][colIndex] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                    }
                }
        }
    }
}
