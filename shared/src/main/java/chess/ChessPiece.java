package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor=pieceColor;
        this.type=type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece currentPiece=board.getPiece(myPosition);
        if (currentPiece.getPieceType()==PieceType.KING){
            return kingMoves(board, myPosition);
        } else if (currentPiece.getPieceType()==PieceType.QUEEN){
            return queenMoves(board, myPosition);
        } else{
            Collection<ChessMove> moves = new ArrayList<>();
            return moves;
        }
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves=new ArrayList<>();
        return moves;
    }
    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves=new ArrayList<>();
        return moves;
    }
}
