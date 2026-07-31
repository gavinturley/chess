package chess;

//done

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public record ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessMove that)) {
            return false;
        }
        return startPosition.equals(that.startPosition)
                && endPosition.equals(that.endPosition)
                && promotionPiece == that.promotionPiece;
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "start=" + startPosition +
                ", end=" + endPosition +
                ", promotion=" + promotionPiece +
                '}';
    }

    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }
}
