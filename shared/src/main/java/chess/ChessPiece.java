package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
        } else if (currentPiece.getPieceType()==PieceType.ROOK){
            return rookMoves(board,myPosition);
        } else if (currentPiece.getPieceType()==PieceType.KNIGHT){
            return knightMoves(board,myPosition);
        }else if (currentPiece.getPieceType()==PieceType.BISHOP){
            return bishopMoves(board,myPosition);
        }else{
            Collection<ChessMove> moves = new ArrayList<>();
            return moves;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor color=board.getBoard()[myPosition.getRow()][myPosition.getColumn()].getTeamColor();
        int i= myPosition.getRow()+1;
        int j= myPosition.getColumn()+1;
        if (i<8 && j<8){
            if (board.getBoard()[i][j]==null) {
                ChessPosition newPosition = new ChessPosition(i+1, j+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, j+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        i=myPosition.getRow()-1;
        j=myPosition.getColumn()-1;
        if (i>=0 && j>=0){
            if (board.getBoard()[i][j]==null){
                ChessPosition newPosition=new ChessPosition(i+1,j+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
            } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, j+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        i=myPosition.getColumn()-1;
        j=myPosition.getRow()+1;
        if (i>=0 && j<8){
            if (board.getBoard()[j][i]==null) {
                ChessPosition newPosition = new ChessPosition(j+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if(board.getBoard()[j][i].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(j+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        i=myPosition.getColumn()+1;
        j=myPosition.getRow()-1;
        if (i<8 && j>=0){
            if (board.getBoard()[j][i]==null){
                ChessPosition newPosition=new ChessPosition(j+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
            } else if(board.getBoard()[j][i].getTeamColor()!=color){
                ChessPosition newPosition=new ChessPosition(j+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
            }
        }
        i= myPosition.getRow()+1;
        if (i<8){
            if (board.getBoard()[i][myPosition.getColumn()]==null) {
                ChessPosition newPosition = new ChessPosition(i+1, myPosition.getColumn()+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if (board.getBoard()[i][myPosition.getColumn()].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, myPosition.getColumn()+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        i=myPosition.getRow()-1;
        if (i>=0){
            if (board.getBoard()[i][myPosition.getColumn()]==null){
                ChessPosition newPosition=new ChessPosition(i+1,myPosition.getColumn()+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
            } else if (board.getBoard()[i][myPosition.getColumn()].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, myPosition.getColumn()+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        i=myPosition.getColumn()+1;
        if (i<8){
            if (board.getBoard()[myPosition.getRow()][i]==null) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            } else if(board.getBoard()[myPosition.getRow()][i].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        i=myPosition.getColumn()-1;
        if (i>=0){
            if (board.getBoard()[myPosition.getRow()][i]==null){
                ChessPosition newPosition=new ChessPosition(myPosition.getRow()+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
            } else if(board.getBoard()[myPosition.getRow()][i].getTeamColor()!=color){
                ChessPosition newPosition=new ChessPosition(myPosition.getRow()+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
            }
        }
        return moves;
    }
    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves=new ArrayList<>();
        for (ChessMove rMove: rookMoves(board,myPosition)){
            moves.add(rMove);
        } for (ChessMove bMove: bishopMoves(board, myPosition)){
            moves.add(bMove);
        }
        return moves;
    }

    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor color=board.getBoard()[myPosition.getRow()][myPosition.getColumn()].getTeamColor();
        int i=myPosition.getRow()+1;
        int j=myPosition.getColumn()+2;
        int k=0;
        int l=0;
        while (i<8 && i>=0 && l<2){
            while (j>=0 && j<8 && k<1){
                if (board.getBoard()[i][j]==null){
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                k++;
            }
            j=myPosition.getColumn()-2;
            l++;
            k=0;
        }
        i=myPosition.getRow()+2;
        j=myPosition.getColumn()+1;
        k=0;
        l=0;
        while (i<8 && i>=0 && l<2){
            while (j>=0 && j<8 && k<1){
                if (board.getBoard()[i][j]==null){
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                j=myPosition.getColumn()-1;
                k++;
            }
            j=myPosition.getColumn()-1;
            l++;
            k=0;
        }
        i=myPosition.getRow()-1;
        j=myPosition.getColumn()+2;
        k=0;
        l=0;
        while (i<8 && i>=0 && l<2){
            while (j>=0 && j<8 && k<1){
                if (board.getBoard()[i][j]==null){
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                j=myPosition.getColumn()-2;
                k++;
            }
            j=myPosition.getColumn()-2;
            l++;
            k=0;
        }
        i=myPosition.getRow()-2;
        j=myPosition.getColumn()+1;
        k=0;
        l=0;
        while (i<8 && i>=0 && l<2){
            while (j>=0 && j<8 && k<1){
                if (board.getBoard()[i][j]==null){
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                    ChessPosition newPosition = new ChessPosition(i+1, j+1);
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                j=myPosition.getColumn()-1;
                k++;
            }
            j=myPosition.getColumn()-1;
            l++;
            k=0;
        }
        return moves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor color=board.getBoard()[myPosition.getRow()][myPosition.getColumn()].getTeamColor();
        int i= myPosition.getRow()+1;
        int j= myPosition.getColumn()+1;
        while (i<8 && j<8){
            if (board.getBoard()[i][j]==null) {
                ChessPosition newPosition = new ChessPosition(i+1, j+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i++;
                j++;
            } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, j+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i=8;
                j=8;
            } else {
                i=8;
                j=8;
            }
        }
        i=myPosition.getRow()-1;
        j=myPosition.getColumn()-1;
        while (i>=0 && j>=0){
            if (board.getBoard()[i][j]==null){
                ChessPosition newPosition=new ChessPosition(i+1,j+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
                i--;
                j--;
            } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, j+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i=-1;
                j=-1;
            } else {
                i=-1;
                j=-1;
            }
        }
        i=myPosition.getColumn()-1;
        j=myPosition.getRow()+1;
        while (i>=0 && j<8){
            if (board.getBoard()[j][i]==null) {
                ChessPosition newPosition = new ChessPosition(j+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i--;
                j++;
            } else if(board.getBoard()[j][i].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(j+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i=-1;
                j=8;
            } else {
                i=-1;
                j=8;
            }
        }
        i=myPosition.getColumn()+1;
        j=myPosition.getRow()-1;
        while (i<8 && j>=0){
            if (board.getBoard()[j][i]==null){
                ChessPosition newPosition=new ChessPosition(j+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
                i++;
                j--;
            } else if(board.getBoard()[j][i].getTeamColor()!=color){
                ChessPosition newPosition=new ChessPosition(j+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
                i=8;
                j=-1;
            }else {
                i=8;
                j=-1;
            }
        }
        return moves;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor color=board.getBoard()[myPosition.getRow()][myPosition.getColumn()].getTeamColor();
        int i= myPosition.getRow()+1;
        while (i<8){
            if (board.getBoard()[i][myPosition.getColumn()]==null) {
                ChessPosition newPosition = new ChessPosition(i+1, myPosition.getColumn()+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i++;
            } else if (board.getBoard()[i][myPosition.getColumn()].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, myPosition.getColumn()+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i=8;
            } else {
                i=8;
            }
        }
        i=myPosition.getRow()-1;
        while (i>=0){
            if (board.getBoard()[i][myPosition.getColumn()]==null){
                ChessPosition newPosition=new ChessPosition(i+1,myPosition.getColumn()+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
                i--;
            } else if (board.getBoard()[i][myPosition.getColumn()].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(i+1, myPosition.getColumn()+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i=-1;
            } else {
                i=-1;
            }
        }
        i=myPosition.getColumn()+1;
        while (i<8){
            if (board.getBoard()[myPosition.getRow()][i]==null) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i++;
            } else if(board.getBoard()[myPosition.getRow()][i].getTeamColor()!=color) {
                ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, i+1);
                moves.add(new ChessMove(myPosition, newPosition, null));
                i=8;
            } else {
                i=8;
            }
        }
        i=myPosition.getColumn()-1;
        while (i>=0){
            if (board.getBoard()[myPosition.getRow()][i]==null){
                ChessPosition newPosition=new ChessPosition(myPosition.getRow()+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
                i--;
            } else if(board.getBoard()[myPosition.getRow()][i].getTeamColor()!=color){
                ChessPosition newPosition=new ChessPosition(myPosition.getRow()+1,i+1);
                moves.add(new ChessMove (myPosition, newPosition, null));
                i=-1;
            }else {
                i=-1;
            }
        }
        return moves;
    }

}
