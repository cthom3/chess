package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    int counter =0;
    ChessBoard currentBoard= new ChessBoard();

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        if (counter%2==0){
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }

    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        counter++;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece=currentBoard.getPiece(startPosition);
        Collection<ChessMove> acceptableMoves = new ArrayList<>();
        if (piece==null){
            return acceptableMoves;
        } else {
            if (piece.getTeamColor()==getTeamTurn()) {
                ChessBoard copyBoard=currentBoard.DeepCopy();
                Collection<ChessMove> potentialMoves = piece.pieceMoves(currentBoard, startPosition);
                for (ChessMove move : potentialMoves) {
                    if (isInCheck(piece.getTeamColor()) == false) {
                        ChessPosition newPosition=move.getEndPosition();
                        ChessPosition oldPosition=move.getStartPosition();
                        copyBoard.addPiece(newPosition,piece);
                        copyBoard.addPiece(oldPosition, null);
                        if (Check(piece.getTeamColor(),copyBoard)==false){
                            acceptableMoves.add(move);
                        }
                    }
                }
                return acceptableMoves;
            } else {
                return acceptableMoves;
            }
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return Check(teamColor, currentBoard);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard=board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    public boolean Check(TeamColor teamColor, ChessBoard board) {
        boolean inCheck=false;
        for (int i=1; i<9; i++){
            for(int j=1; j<9; j++){
                ChessPosition position=new ChessPosition(i,j);
                ChessPiece piece=board.getPiece(position);
                if (piece!=null && piece.getTeamColor()!=teamColor){
                    Collection<ChessMove> potentialMoves=piece.pieceMoves(board,position);
                    for (ChessMove move: potentialMoves){
                        ChessPosition newPosition=move.getEndPosition();
                        ChessPiece potentialPiece=board.getPiece(newPosition);
                        if (potentialPiece!=null && potentialPiece.getPieceType()==ChessPiece.PieceType.KING){
                            inCheck=true;
                            return inCheck;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

}
