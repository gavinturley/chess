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

    /**
     * @return ChessPosition of starting location
     */
    @Override
    public ChessPosition startPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    @Override
    public ChessPosition endPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    @Override
    public ChessPiece.PieceType promotionPiece() {
        return this.promotionPiece;
    }
}
