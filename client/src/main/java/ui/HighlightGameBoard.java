package ui;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static ui.EscapeSequences.*;


public class HighlightGameBoard {
    private static final int BOARD_SIZE=8;
    private Map<ChessPiece.PieceType,String> pieceToString=Map.of(ChessPiece.PieceType.ROOK, " R ",
            ChessPiece.PieceType.KNIGHT, " N ",
            ChessPiece.PieceType.BISHOP," B ",
            ChessPiece.PieceType.QUEEN, " Q ",
            ChessPiece.PieceType.KING, " K ",
            ChessPiece.PieceType.PAWN, " p ");
    private Map<ChessGame.TeamColor,String> colorToString=Map.of(
            ChessGame.TeamColor.WHITE, SET_TEXT_COLOR_WHITE,
            ChessGame.TeamColor.BLACK, SET_TEXT_COLOR_BLUE);

    public void highlightAll(ChessGame game, Collection<ChessMove> moves, ChessPosition start,String playerColor) {
        ChessBoard board = game.getBoard();
        ArrayList<ChessPosition> endPositions= new ArrayList<>();
        for (ChessMove move:moves){
            endPositions.add(move.getEndPosition());
        }
        var out = new PrintStream(System.out, true,StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        out.println();
        drawHeaders(out,playerColor);
        drawChessBoard(out,playerColor,board, endPositions, start);
        drawHeaders(out, playerColor);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void drawChessBoard(PrintStream out, String playerColor, ChessBoard board, ArrayList<ChessPosition> endPositions, ChessPosition start){
        ChessPiece[][] squares=board.getBoard();
        if (Objects.equals(playerColor, "black")){
            for (int i=0; i<8;i++){
                ArrayList<String> piece= new ArrayList<>();
                ArrayList<String> colors= new ArrayList<>();
                for (int j=7;j>=0;j--){
                    if (squares[i][j]!=null) {
                        String letter = pieceToString.get(squares[i][j].getPieceType());
                        piece.add(letter);
                        String textColor = colorToString.get(squares[i][j].getTeamColor());
                        colors.add(textColor);
                    } else {
                        piece.add("   ");
                        colors.add(SET_TEXT_COLOR_BLACK);
                    }
                }
//                System.out.println(piece);
                drawRow(i+1,piece,colors,playerColor,endPositions,start,out);
            }
        } else {
            for (int i=7; i>=0;i--){
                ArrayList<String> piece= new ArrayList<>();
                ArrayList<String> colors= new ArrayList<>();
                for (int j=0;j<8;j++){
                    if (squares[i][j]!=null) {
                        String letter = pieceToString.get(squares[i][j].getPieceType());
                        piece.add(letter);
                        String textColor = colorToString.get(squares[i][j].getTeamColor());
                        colors.add(textColor);
                    } else {
                        piece.add("   ");
                        colors.add(SET_TEXT_COLOR_BLACK);
                    }
                }
//                System.out.println(piece);
                drawRow(i+1,piece,colors,playerColor,endPositions,start,out);
            }
        }
    }

    private void drawRow(int row, ArrayList<String> symbol, ArrayList<String> color,String playerColor,ArrayList<ChessPosition> endPositions,ChessPosition start,PrintStream out){
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" "+row+" ");
        if (Objects.equals(playerColor, "BLACK")){
            for (int boardCol = 7; boardCol>=0; boardCol--) {
                if (Objects.equals(new ChessPosition(row,boardCol+1),start)){
                    out.print(SET_BG_COLOR_RED);
                    out.print(color.get(boardCol));
                    out.print(symbol.get(boardCol));
                } else if (endPositions.contains(new ChessPosition(row,boardCol+1))){
                    if (boardCol % 2 == (row % 2)) {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    } else {
                        out.print(SET_BG_COLOR_GREEN);
                    }
                    out.print(color.get(boardCol));
                    out.print(symbol.get(boardCol));
                } else{
                    if (boardCol % 2 == (row % 2)) {
                        out.print(SET_BG_COLOR_DARK_GREY);
                    } else {
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                    out.print(color.get(boardCol));
                    out.print(symbol.get(boardCol));
                }
            }
        } else {
            for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
                if (Objects.equals(new ChessPosition(row,boardCol+1),start)){
                    out.print(SET_BG_COLOR_YELLOW);
                    out.print(color.get(boardCol));
                    out.print(symbol.get(boardCol));
                } else if (endPositions.contains(new ChessPosition(row,boardCol+1))) {
                    if (boardCol % 2 == (row % 2)) {
                        out.print(SET_BG_COLOR_GREEN);
                    } else {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    out.print(color.get(boardCol));
                    out.print(symbol.get(boardCol));
                } else {
                    if (boardCol % 2 == (row % 2)) {
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    } else {
                        out.print(SET_BG_COLOR_DARK_GREY);
                    }
                    out.print(color.get(boardCol));
                    out.print(symbol.get(boardCol));
                }
            }
        }
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" "+row+" ");
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println();
    }

    private void drawHeaders(PrintStream out, String playerColor){
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        if (Objects.equals(playerColor, "BLACK")) {
            String[] headers = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ","   "};
            for (int boardCol = 0; boardCol<BOARD_SIZE+2; ++boardCol) {
                out.print((headers[boardCol]));
            }
        } else {
            String[] headers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ","   "};
            for (int boardCol = 0; boardCol<BOARD_SIZE+2; ++boardCol){
                out.print((headers[boardCol]));
            }
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
}
