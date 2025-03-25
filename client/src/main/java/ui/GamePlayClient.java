package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GamePlayClient {
    private static final int BOARD_SIZE=8;
    private static final int SPACE = 1;
    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        out.println();
        drawHeaders(out);
        drawChessBoard(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        String[] headers = {" ","a","b","c","d","e","f","g","h"};
        for (int boardCol = 0; boardCol<BOARD_SIZE+1; ++boardCol){
            out.print(EMPTY.repeat(SPACE/2));
            out.print((headers[boardCol]));
            out.print(EMPTY.repeat(SPACE/2));
        }
        out.println();
    }

    private static void drawChessBoard(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY.repeat(SPACE/2));
        out.print("8");
        out.print(EMPTY.repeat(SPACE/2));
        String[] headers = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK};
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
            if (boardCol %2==0){
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }
            else{
                out.print(SET_BG_COLOR_DARK_GREY);
            }
            out.print(EMPTY.repeat(SPACE / 2));
            out.print((headers[boardCol]));
            out.print(EMPTY.repeat(SPACE / 2));
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
        out.print(EMPTY.repeat(SPACE/2));
        out.print("7");
        out.print(EMPTY.repeat(SPACE/2));
        String headers1 = WHITE_PAWN;
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
            if (boardCol %2==0){
                out.print(SET_BG_COLOR_DARK_GREY);
            }
            else{
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }
            out.print(EMPTY.repeat(SPACE / 2));
            out.print(headers1);
            out.print(EMPTY.repeat(SPACE / 2));
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
        drawEmptyRow(6,out);
        drawEmptyRow(5,out);
        drawEmptyRow(4,out);
        drawEmptyRow(3,out);
        out.print(EMPTY.repeat(SPACE/2));
        out.print("2");
        out.print(EMPTY.repeat(SPACE/2));
        String headers2 = BLACK_PAWN;
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
            if (boardCol %2==0){
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }
            else{
                out.print(SET_BG_COLOR_DARK_GREY);
            }
            out.print(EMPTY.repeat(SPACE / 2));
            out.print(headers2);
            out.print(EMPTY.repeat(SPACE / 2));
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
        out.print(EMPTY.repeat(SPACE/2));
        out.print("1");
        out.print(EMPTY.repeat(SPACE/2));
        String[] headers4 = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK};
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
            if (boardCol %2==0){
                out.print(SET_BG_COLOR_DARK_GREY);
            }
            else{
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }
            out.print(EMPTY.repeat(SPACE / 2));
            out.print((headers4[boardCol]));
            out.print(EMPTY.repeat(SPACE / 2));
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();


    }

    private static void drawEmptyRow(int row, PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY.repeat(SPACE / 2));
        out.print(row);
        out.print(EMPTY.repeat(SPACE / 2));
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
            if (boardCol %2==(row%2)){
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }else{
                out.print(SET_BG_COLOR_DARK_GREY);
            }
            out.print(EMPTY);
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
}
