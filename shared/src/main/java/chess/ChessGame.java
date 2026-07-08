package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard gameBoard;
    TeamColor teamTurn;


    public ChessGame() {
        this.gameBoard = new ChessBoard();
        this.gameBoard.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
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
        // valid if doesn't leave king in check or move king into check
        if (gameBoard.getPiece(startPosition) == null) return null;

        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        for (ChessMove move : currentPiece.pieceMoves(gameBoard, startPosition)){
            if (!leavesKingInCheck(gameBoard, move)){
                moves.add(move);
            }
        }

        return moves;
    }

    private boolean leavesKingInCheck(ChessBoard board, ChessMove move){
        // if move happens and king is in check, return false
        ChessGame.TeamColor teamColor = board.getPiece(move.getStartPosition()).getTeamColor();
        ChessBoard tempBoard = board.copy();
        tempBoard.addPiece(move.getStartPosition(), null);
        tempBoard.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));

        return isInCheck(teamColor, tempBoard);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (gameBoard.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException("No piece in specified location.");
        }

        boolean isValid = false;
        for (ChessMove validMove : validMoves(move.getStartPosition())){
            if (validMove.equals(move)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) throw new InvalidMoveException("Not a valid move.");

        ChessGame.TeamColor pieceColor = gameBoard.getPiece(move.getStartPosition()).getTeamColor();
        if (teamTurn != pieceColor) throw new InvalidMoveException("Not your turn");

        ChessBoard newBoard = gameBoard.copy();
        if (move.getPromotionPiece() == null){
            newBoard.addPiece(move.getStartPosition(), null);
            newBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
        } else {
            newBoard.addPiece(move.getStartPosition(), null);
            ChessPiece promotionPiece = new ChessPiece(pieceColor, move.getPromotionPiece());
            newBoard.addPiece(move.getEndPosition(), promotionPiece);
        }

        setBoard(newBoard);
        setTeamTurn(teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, gameBoard);
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard board){
        ChessPosition kingPosition = findKing(teamColor, board);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition tile = new ChessPosition(row + 1, col + 1);
                ChessPiece piece = board.getPiece(tile);
                if (piece == null) continue;
                for (ChessMove move : piece.pieceMoves(board, tile)){
                    if (move.getEndPosition().equals(kingPosition)) return true;
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor, ChessBoard board){
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                ChessPosition tile = new ChessPosition(row + 1, col + 1);
                ChessPiece piece = board.getPiece(tile);
                if (piece == null) continue;
                if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                    return tile;
                }
            }
        }
        return null;
    }

    private boolean hasNoValidMoves(TeamColor teamColor){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition tile = new ChessPosition(row + 1, col + 1);
                ChessPiece piece = gameBoard.getPiece(tile);
                if (piece == null || piece.getTeamColor() != teamColor) continue;
                Collection <ChessMove> moves = validMoves(tile);
                if (moves != null && !moves.isEmpty()) return false;

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
        //in checkmate if isInCheck() and no valid moves
        return isInCheck(teamColor) && hasNoValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // if not in check but no valid moves
        return !isInCheck(teamColor) && hasNoValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.gameBoard;
    }

    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        ChessGame chessGame = (ChessGame) object;
        return java.util.Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }
}
