package chess;

import java.util.Collection;
import java.util.ArrayList;

//done
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
                if (row == 0 && col == 0) continue;
                int tempRow = myPosition.getRow() + row;
                int tempCol = myPosition.getColumn() + col;
                if (tempRow > 8 || tempRow < 1) continue;
                if (tempCol > 8 || tempCol < 1) continue;
                ChessPosition endPosition = new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col);
                if (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != this.getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, endPosition, null);
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        // if space ahead is empty or space diagonal is taken, moves.add(tile)
        // if starting position and no pieces ahead, move two ahead
        // if pawn is white, moves forward, otherwise moves back
        Collection<ChessMove> moves = new ArrayList<>();

        boolean isWhite = this.getTeamColor() == ChessGame.TeamColor.WHITE;
        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 2 : 7;
        int promotionRow = isWhite ? 8 : 1;

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int oneForwardRow = row + direction;
        if (oneForwardRow >= 1 && oneForwardRow <= 8) {
            ChessPosition oneForward = new ChessPosition(oneForwardRow, col);
            if (board.getPiece(oneForward) == null) {
                addPawnMove(moves, myPosition, oneForward, oneForwardRow, promotionRow);

                int twoForwardRow = row + 2 * direction;
                if (row == startRow) {
                    ChessPosition twoForward = new ChessPosition(twoForwardRow, col);
                    if (board.getPiece(twoForward) == null) {
                        moves.add(new ChessMove(myPosition, twoForward, null));
                    }
                }
            }
        }

        for (int dc : new int[]{-1, 1}) {
            int captureCol = col + dc;
            if (captureCol < 1 || captureCol > 8) continue;
            if (oneForwardRow < 1 || oneForwardRow > 8) continue;

            ChessPosition capturePosition = new ChessPosition(oneForwardRow, captureCol);
            ChessPiece occupant = board.getPiece(capturePosition);
            if (occupant != null && occupant.getTeamColor() != this.getTeamColor()) {
                addPawnMove(moves, myPosition, capturePosition, oneForwardRow, promotionRow);
            }
        }

        return moves;
    }

    private void addPawnMove(Collection<ChessMove> moves, ChessPosition start, ChessPosition end, int endRow, int promotionRow) {
        if (endRow == promotionRow) {
            moves.add(new ChessMove(start, end, PieceType.QUEEN));
            moves.add(new ChessMove(start, end, PieceType.ROOK));
            moves.add(new ChessMove(start, end, PieceType.BISHOP));
            moves.add(new ChessMove(start, end, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return slidingMoves(board, myPosition, directions);
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dy = {-1, 1, -2, 2, 2, -2, -1, 1};

        for (int i = 0; i < 8; i++){
            int tempRow = myPosition.getRow() + dx[i];
            int tempCol = myPosition.getColumn() + dy[i];

            if (tempRow > 8 || tempRow < 1) continue;
            if (tempCol > 8 || tempCol < 1) continue;

            ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() != this.getTeamColor()) {
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                moves.add(move);
            }
        }

        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return slidingMoves(board, myPosition, directions);
    }

    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        // runs through different moves using dx and dy arrays
        Collection<ChessMove> moves = new ArrayList<>();

        for (int[] direction : directions) {
            int tempRow = myPosition.getRow();
            int tempCol = myPosition.getColumn();

            while (true) {
                tempRow += direction[0];
                tempCol += direction[1];

                if (tempRow > 8 || tempRow < 1 || tempCol > 8 || tempCol < 1) break;

                ChessPosition endPosition = new ChessPosition(tempRow, tempCol);
                ChessPiece occupant = board.getPiece(endPosition);

                if (occupant == null) {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                } else {
                    if (occupant.getTeamColor() != this.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, endPosition, null));
                    }
                    break;
                }
            }
        }

        return moves;
    }
}

