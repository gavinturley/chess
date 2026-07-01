package chess;

//done
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }



    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int rowIndex = 0; rowIndex < board.length; rowIndex++){
            for (int colIndex = 0; colIndex < board[0].length; colIndex++){
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

                    board[rowIndex][colIndex] = new ChessPiece(color, type);
                } else if (row == 2 || row == 7){
                    board[rowIndex][colIndex] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                }
            }
        }
    }

    public ChessBoard copy() {
        ChessBoard newBoard = new ChessBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = this.getPiece(pos);
                if (piece != null) {
                    newBoard.addPiece(pos, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }
        return newBoard;
    }
}
