package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        }else if (currentPiece.getPieceType()==PieceType.PAWN){
            return pawnMoves(board,myPosition);
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

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor color=board.getBoard()[myPosition.getRow()][myPosition.getColumn()].getTeamColor();
        int j=myPosition.getColumn();
        if (color==ChessGame.TeamColor.BLACK){
            ChessGame.TeamColor enemy=ChessGame.TeamColor.WHITE;
            int i=myPosition.getRow()-1;
            if (i>=0){
                if (board.getBoard()[i][j]==null) {
                    if (i==0){
                        moves=promotionPawn(myPosition,new ChessPosition(i+1, j+1),moves);
                    }else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j+1), null));
                    }
                    if (i==5 && board.getBoard()[i-1][j]==null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(i , j + 1), null));
                    }
                } if ((j-1)>=0 && board.getBoard()[i][j-1]!=null){
                    if (board.getBoard()[i][j-1].getTeamColor()==enemy && i==0) {
                        moves=promotionPawn(myPosition,new ChessPosition(i+1, j),moves);
                    } else if (board.getBoard()[i][j-1].getTeamColor()==enemy){
                        moves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j), null));
                    }
                } if ((j+1)<8 && board.getBoard()[i][j+1]!=null){
                    if (board.getBoard()[i][j+1].getTeamColor()==enemy && i==0) {
                        moves=promotionPawn(myPosition,new ChessPosition(i+1, j+2),moves);
                    } else if (board.getBoard()[i][j+1].getTeamColor()==enemy){
                        moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j+2), null));
                    }
                }
            }
        } else if (color==ChessGame.TeamColor.WHITE){
            ChessGame.TeamColor enemy=ChessGame.TeamColor.BLACK;
            int i=myPosition.getRow()+1;
            if (i<=7){
                if (board.getBoard()[i][j]==null) {
                    if (i==7) {
                        moves = promotionPawn(myPosition, new ChessPosition(i+1, j+1), moves);
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j+1), null));
                    }
                    if (i==2 && board.getBoard()[i+1][j]==null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(i+2 , j + 1), null));
                    }
                } if ((j-1)>=0 && board.getBoard()[i][j-1]!=null){
                    if (board.getBoard()[i][j-1].getTeamColor()==enemy && i==7){
                        moves=promotionPawn(myPosition,new ChessPosition(i+1, j),moves);
                    } else if (board.getBoard()[i][j-1].getTeamColor()==enemy){
                        moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j), null));
                    }
                } if ((j+1)<8 && board.getBoard()[i][j+1]!=null){
                    if (board.getBoard()[i][j+1].getTeamColor()==enemy && i==7) {
                        moves = promotionPawn(myPosition, new ChessPosition(i + 1, j + 2), moves);
                    } else if (board.getBoard()[i][j+1].getTeamColor()==enemy){
                        moves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j + 2), null));
                    }
                }
            }
            if (i==1){
                if (board.getBoard()[i+2][j]==null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(i+3, j+1), null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> promotionPawn(ChessPosition myPosition,ChessPosition newPosition,Collection<ChessMove> moves){
        moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
        return moves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves=new ArrayList<>();
        ChessGame.TeamColor color=board.getBoard()[myPosition.getRow()][myPosition.getColumn()].getTeamColor();
        if ((myPosition.getRow()+1)<8 && (myPosition.getColumn()+1)<8){
            moves=kingMovesDiagonalLeft(myPosition.getRow()+1,myPosition.getColumn()+1,board,myPosition,color,moves);
        }
        if ((myPosition.getRow()-1)>=0 && (myPosition.getColumn()-1)>=0){
            moves=kingMovesDiagonalLeft(myPosition.getRow()-1,myPosition.getColumn()-1,board,myPosition,color,moves);
        }
        if ((myPosition.getColumn()-1)>=0 && (myPosition.getRow()+1)<8){
            moves=kingMovesDiagonalRight(myPosition.getColumn()-1,myPosition.getRow()+1,board,myPosition,color,moves);
        }
        if ((myPosition.getColumn()+1)<8 && (myPosition.getRow()-1)>=0){
            moves=kingMovesDiagonalRight(myPosition.getColumn()+1,myPosition.getRow()-1,board,myPosition,color,moves);
        }
        int i= myPosition.getRow()+1;
        if (i<8){
            moves=kingMovesColumn(i,board,myPosition,color,moves);
        }
        i=myPosition.getRow()-1;
        if (i>=0){
            moves=kingMovesColumn(i,board,myPosition,color,moves);
        }
        i=myPosition.getColumn()+1;
        if (i<8){
            moves=kingMovesRow(i,board,myPosition,color,moves);
        }
        i=myPosition.getColumn()-1;
        if (i>=0){
            moves=kingMovesRow(i,board,myPosition,color,moves);
        }
        return moves;
    }

    private Collection<ChessMove> kingMovesDiagonalRight (int i, int j, ChessBoard board, ChessPosition myPosition,
                                                   ChessGame.TeamColor color, Collection<ChessMove> moves){
        if (board.getBoard()[j][i]==null || board.getBoard()[j][i].getTeamColor()!=color){
            moves.add(new ChessMove (myPosition, new ChessPosition(j+1,i+1), null));
        }
        return moves;
    }

    private Collection<ChessMove> kingMovesDiagonalLeft (int i, int j, ChessBoard board, ChessPosition myPosition,
                                                     ChessGame.TeamColor color, Collection<ChessMove> moves){
        if (board.getBoard()[i][j]==null || board.getBoard()[i][j].getTeamColor()!=color){
            moves.add(new ChessMove (myPosition, new ChessPosition(i+1,j+1), null));
        }
        return moves;
    }

    private Collection<ChessMove> kingMovesColumn (int i, ChessBoard board, ChessPosition myPosition,
                                                     ChessGame.TeamColor color, Collection<ChessMove> moves){
        if (board.getBoard()[i][myPosition.getColumn()]==null || board.getBoard()[i][myPosition.getColumn()].getTeamColor()!=color) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i+1, myPosition.getColumn()+1), null));
        }
        return moves;
    }

    private Collection<ChessMove> kingMovesRow (int i, ChessBoard board, ChessPosition myPosition,
                                                   ChessGame.TeamColor color, Collection<ChessMove> moves){
        if (board.getBoard()[myPosition.getRow()][i]==null || board.getBoard()[myPosition.getRow()][i].getTeamColor()!=color){
            moves.add(new ChessMove (myPosition, new ChessPosition(myPosition.getRow()+1,i+1), null));
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

        List<Integer> outside=new ArrayList<>();
        outside.add(myPosition.getColumn()+2);
        outside.add(myPosition.getColumn()-2);

        moves=checkRowKnightMoves(myPosition.getRow()+1,outside,board,myPosition,color,moves);
        moves=checkRowKnightMoves(myPosition.getRow()-1,outside,board,myPosition,color,moves);

        List<Integer> inside=new ArrayList<>();
        inside.add(myPosition.getColumn()+1);
        inside.add(myPosition.getColumn()-1);
        moves=checkRowKnightMoves(myPosition.getRow()+2,inside,board,myPosition,color,moves);
        moves=checkRowKnightMoves(myPosition.getRow()-2,inside,board,myPosition,color,moves);
        return moves;
    }

    private Collection<ChessMove> checkRowKnightMoves (int i, List<Integer> j, ChessBoard board, ChessPosition myPosition,
                                                       ChessGame.TeamColor color, Collection<ChessMove> moves){
        int l=0;
        int k=0;
        while (i<8 && i>=0 && l<2){
            while (j.get(l)>=0 && j.get(l)<8 && k<1){
                if (board.getBoard()[i][j.get(l)]==null || board.getBoard()[i][j.get(l)].getTeamColor()!=color){
                    moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j.get(l)+1), null));
                }
                k++;
            }
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
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j+1), null));
                i++;
                j++;
            } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j+1), null));
                break;
            } else {
                break;
            }
        }
        i=myPosition.getRow()-1;
        j=myPosition.getColumn()-1;
        while (i>=0 && j>=0){
            if (board.getBoard()[i][j]==null){
                moves.add(new ChessMove (myPosition, new ChessPosition(i+1,j+1), null));
                i--;
                j--;
            } else if (board.getBoard()[i][j].getTeamColor()!=color) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j+1), null));
                break;
            } else {
                break;
            }
        }
        i=myPosition.getColumn()-1;
        j=myPosition.getRow()+1;
        while (i>=0 && j<8){
            if (board.getBoard()[j][i]==null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(j+1, i+1), null));
                i--;
                j++;
            } else if(board.getBoard()[j][i].getTeamColor()!=color) {
                moves.add(new ChessMove(myPosition, new ChessPosition(j+1, i+1), null));
                break;
            } else {
                break;
            }
        }
        i=myPosition.getColumn()+1;
        j=myPosition.getRow()-1;
        while (i<8 && j>=0){
            if (board.getBoard()[j][i]==null){
                moves.add(new ChessMove (myPosition, new ChessPosition(j+1,i+1), null));
                i++;
                j--;
            } else if(board.getBoard()[j][i].getTeamColor()!=color){
                moves.add(new ChessMove (myPosition, new ChessPosition(j+1,i+1), null));
                break;
            }else {
                break;
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
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, myPosition.getColumn()+1), null));
                i++;
            } else if (board.getBoard()[i][myPosition.getColumn()].getTeamColor()!=color) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, myPosition.getColumn()+1), null));
                break;
            } else {
                break;
            }
        }
        i=myPosition.getRow()-1;
        while (i>=0){
            if (board.getBoard()[i][myPosition.getColumn()]==null){
                moves.add(new ChessMove (myPosition, new ChessPosition(i+1,myPosition.getColumn()+1), null));
                i--;
            } else if (board.getBoard()[i][myPosition.getColumn()].getTeamColor()!=color) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, myPosition.getColumn()+1), null));
                break;
            } else {
                break;
            }
        }
        i=myPosition.getColumn()+1;
        while (i<8){
            if (board.getBoard()[myPosition.getRow()][i]==null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, i+1), null));
                i++;
            } else if(board.getBoard()[myPosition.getRow()][i].getTeamColor()!=color) {
                moves.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1, i+1), null));
                break;
            } else {
                break;
            }
        }
        i=myPosition.getColumn()-1;
        while (i>=0){
            if (board.getBoard()[myPosition.getRow()][i]==null){
                moves.add(new ChessMove (myPosition, new ChessPosition(myPosition.getRow()+1,i+1), null));
                i--;
            } else if(board.getBoard()[myPosition.getRow()][i].getTeamColor()!=color){
                moves.add(new ChessMove (myPosition, new ChessPosition(myPosition.getRow()+1,i+1), null));
                break;
            }else {
                break;
            }
        }
        return moves;
    }

}
