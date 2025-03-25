package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class GamePlayClient {
    private static final int BOARD_SIZE=8;
    public static void main(String playerColor){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        out.println();
        drawHeaders(out);
        drawChessBoard(out, playerColor);
        drawHeaders(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        String[] headers = {"   "," a "," b "," c "," d "," e "," f "," g "," h "};
        for (int boardCol = 0; boardCol<BOARD_SIZE+1; ++boardCol){
            out.print((headers[boardCol]));
        }
        out.println();
    }

    private static void drawChessBoard(PrintStream out, String playerColor){
        String[] headers1 = {" R ", " N ", " B ", " K ", " Q ", " B ", " N ", " R "};
        if (Objects.equals(playerColor, "white")){
//            String[] headers1 = {" R ", " N ", " B ", " K ", " Q ", " B ", " N ", " R "};
//            String[] headers1 = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK};
            drawFirstRow(8,headers1, SET_TEXT_COLOR_BLUE,out);
            drawRow(7," p ",SET_TEXT_COLOR_BLUE,out);
            drawRow(6,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(5,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(4,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(3,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(2," p ",SET_TEXT_COLOR_RED,out);
//            String[] headers2 = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK};
            drawFirstRow(1,headers1,SET_TEXT_COLOR_RED,out);
        } else {
//            String[] headers1 = {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK};
            drawFirstRow(1,headers1,SET_TEXT_COLOR_RED,out);
            drawRow(2," p ",SET_TEXT_COLOR_RED,out);
            drawRow(3,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(4,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(5,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(6,"   ",SET_TEXT_COLOR_RED,out);
            drawRow(7," p ",SET_TEXT_COLOR_BLUE,out);
//            String[] headers2 = {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK};
            drawFirstRow(8,headers1,SET_TEXT_COLOR_BLUE,out);
        }
    }

    private static void drawFirstRow(int row, String[] headers, String textColor,PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(" "+row+" ");
        out.print(textColor);
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
            if (boardCol %2==(row%2)){
                out.print(SET_BG_COLOR_LIGHT_GREY);
            } else{
                out.print(SET_BG_COLOR_DARK_GREY);
            }
            out.print((headers[boardCol]));
        }
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" "+row+" ");
        out.println();
    }

    private static void drawRow(int row, String symbol, String color,PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(" "+row+" ");
        out.print(color);
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol) {
            if (boardCol %2==(row%2)){
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }else{
                out.print(SET_BG_COLOR_DARK_GREY);
            }
            out.print(symbol);
        }
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" "+row+" ");
        out.println();
    }
}
