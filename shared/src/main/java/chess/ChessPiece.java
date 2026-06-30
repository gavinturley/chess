package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor color;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceType pieceType = getPieceType();
        if (pieceType == PieceType.QUEEN) return queenMoves(board, myPosition);
        else if (pieceType == PieceType.PAWN) return pawnMoves(board, myPosition);
        else if (pieceType == PieceType.BISHOP) return bishopMoves(board, myPosition);
        else if (pieceType == PieceType.KNIGHT) return knightMoves(board, myPosition);
        else if (pieceType == PieceType.ROOK) return rookMoves(board, myPosition);
        else if (pieceType == PieceType.KING) return kingMoves(board, myPosition);
        else {
            System.out.println("Chess piece not valid.");
            return null;
        }
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (ChessMove move : rookMoves(board, myPosition)){
            moves.add(move);
        }
        for (ChessMove move : bishopMoves(board, myPosition)){
            moves.add(move);
        }
        return moves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        // if space 1 away from king is empty and isnt its self, moves.add(tile)
        Collection<ChessMove> moves = new ArrayList<>();

        for (int row = -1; row < 2; row++){
            for (int col = -1; col < 2; col++){
                int tempRow = myPosition.getRow() + row;
                int tempCol = myPosition.getColumn() + col;
                if (tempRow > 8 || tempRow < 1) continue;
                if (tempCol > 8 || tempCol < 1) continue;
                ChessPosition endPosition = new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col);
                if (endPosition == myPosition) continue;
                if (ChessBoard.getPiece(temp) == null) {
                    ChessMove move = new ChessMove(myPosition, endPosition, null);
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        // if space ahead is empty or space diagonal is taken, moves.add(tile)
        Collection<ChessMove> moves = new ArrayList<>();
        int tempCol = myPosition.getColumn() + 1;
        int tempRow = myPosition.getRow();
        if (tempCol > 8) return moves;

        ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
        if (ChessBoard.getPiece(temp) == null) {
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            moves.add(move);
        }

        endPosition = new ChessPosition(tempRow - 1, tempCol);
        if (ChessBoard.getPiece(temp) != null){
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            moves.add(move);
        }

        endPosition = new ChessPosition(tempRow + 1, tempCol);
        if (ChessBoard.getPiece(temp) != null){
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            moves.add(move);
        }

        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collections<ChessMove> moves = new ArrayList<>();
        int[] multiplier = {1, -1};
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 8; j++){
                int tempRow = myPosition.getRow * multiplier[i];
                int tempCol = myPosition.getColumn;

                if (tempRow > 8 || tempRow < 1) break;
                ChessPosition endPosition = new ChessPosition(tempRow + j, tempCol);
                if (board.getPiece(endPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, endPosition, null);
                    moves.add(move);
                } else {
                    break;
                }
            }
        }

        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 8; j++){
                int tempRow = myPosition.getRow;
                int tempCol = myPosition.getColumn * multiplier[i];

                if (tempCol > 8 || tempCol < 1) break;
                ChessPosition endPosition = new ChessPosition(tempRow, tempCol + j);
                if (board.getPiece(endPosition) == null) {
                    ChessMove move = new ChessMove(myPosition, endPosition, null);
                    moves.add(move);
                } else {
                    break;
                }
            }
        }

        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dy = {-1, 1, -2, 2, 2, -2, -1, 1};

        for (int i = 0; i < 8; i++){
            int tempRow = myPosition.getRow + dx[i];
            int tempCol = myPosition.getCol + dy[i];

            if (tempRow > 8 || tempRow < 1) continue;
            if (tempCol > 8 || tempCol < 1) continue;

            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                moves.add(move);
            }
        }

        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMoves> moves = new ArrayList<>();
        int[] multiplier = {1, -1};

        for (int i = 0; i < 2; i++){
            int rowMult = multiplier[i];
            for (int j = 0; j < 2; j++){
                int colMult = multiplier[j];

                for (int k = 0; k < 8; k++){
                    int tempRow = (myPosition.getRow() + k) * rowMult;
                    int tempCol = (myPosition.getCol() + k) * colMult;

                    if (tempRow > 8 || tempRow < 1) continue;
                    if (tempCol > 8 || tempCol < 1) continue;

                    ChessPosition endPosition = new ChessPostion(tempRow, tempCol);
                    if (board.getPiece(endPosition) == null){
                        ChessMove move = new ChessMove(myPosition, endPosition, null);
                        moves.add(move);
                    } else {
                        break;
                    }
                }
            }
        }

        return moves;
    }
}

