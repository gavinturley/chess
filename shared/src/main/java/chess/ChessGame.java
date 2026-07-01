package chess;
import java.util.Objects;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard currentBoard;

    public ChessGame() {
        this.currentBoard = new ChessBoard();
        this.currentBoard.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        if (piece == null) return null;

        Collection<ChessMove> pseudoLegalMoves = piece.pieceMoves(getBoard(), startPosition);
        Collection<ChessMove> legalMoves = new java.util.ArrayList<>();
        for (ChessMove move : pseudoLegalMoves) {
            if (!leavesKingInCheck(move, piece.getTeamColor())) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = currentBoard.getPiece(move.getStartPosition());
        if (piece == null || piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("No piece of yours at start position");
        }
        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());
        if (legalMoves == null || !legalMoves.contains(move)) {
            throw new InvalidMoveException("Illegal move");
        }

        ChessPiece.PieceType promotionType = move.getPromotionPiece();
        ChessPiece pieceToPlace;
        if (promotionType != null) {
            pieceToPlace = new ChessPiece(piece.getTeamColor(), promotionType);
        } else {
            pieceToPlace = piece;
        }

        currentBoard.addPiece(move.getEndPosition(), pieceToPlace);
        currentBoard.addPiece(move.getStartPosition(), null);

        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    private ChessPosition findKing(ChessGame.TeamColor teamColor){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition tile = new ChessPosition(row, col);
                ChessPiece piece = getBoard().getPiece(tile);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    return tile;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor){
                    for (ChessMove move : piece.pieceMoves(currentBoard, position)){
                        if (move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean leavesKingInCheck(ChessMove move, ChessGame.TeamColor teamColor) {
        // if move is made and king of same color is then in check, return true
        ChessBoard simulated = currentBoard.copy();
        ChessPiece moving = simulated.getPiece(move.getStartPosition());

        ChessPiece.PieceType promotionType = move.getPromotionPiece();
        ChessPiece pieceToPlace;
        if (promotionType != null) {
            pieceToPlace = new ChessPiece(moving.getTeamColor(), promotionType);
        } else {
            pieceToPlace = moving;
        }

        simulated.addPiece(move.getStartPosition(), null);
        simulated.addPiece(move.getEndPosition(), pieceToPlace);

        ChessBoard saved = currentBoard;
        currentBoard = simulated;

        boolean inCheck = isInCheck(teamColor);
        currentBoard = saved;

        return inCheck;
    }

    private boolean hasNoLegalMoves(TeamColor teamColor){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(position);
                    if (moves != null && !moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // if (is in check) and (no possible moves) return true
        if (!isInCheck(teamColor)) return false;
        return hasNoLegalMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;
        return hasNoLegalMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.currentBoard;
    }
}
