package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType _type;
    private ChessGame.TeamColor _color;
    Collection<ChessMove> moves = new ArrayList<>();

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        _type = type;
        _color = pieceColor;
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
        return _color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return _type;
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
        if (pieceType == PieceType.QUEEN){
            return queenMoves(board, myPosition);
        } else if (pieceType == PieceType.PAWN){
            return pawnMoves(board, myPosition);
        } else if (pieceType == PieceType.BISHOP){
            return bishopMoves(board, myPosition);
        } else if (pieceType == PieceType.KNIGHT){
            return knightMoves(board, myPosition);
        } else if (pieceType == PieceType.ROOK){
            return rookMoves(board, myPostion);
        } else if (pieceType == PieceType.KING){
            return kingMoves(board, myPosition);
        } else {
            System.out.println("Chess piece not valid.");
            return null;
        }
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
    }
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
    }
    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
    }
    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
    }
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
    }
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
    }
}

