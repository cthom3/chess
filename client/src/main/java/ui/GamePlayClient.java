package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GamePlayClient {
    private static final int BOARD_SIZE=8;
    private static final int SPACE = 2;
    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out);
        drawChessBoard(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        String[] headers = {"a","b","c","d","e","f","g","h"};
        for (int boardCol = 0; boardCol<BOARD_SIZE; ++boardCol){
            out.print(EMPTY.repeat(SPACE));
            out.print((headers[boardCol]));
            out.print(EMPTY.repeat(SPACE));
        }
    }

    private static void drawChessBoard(PrintStream out){
        for (int boardRow = 0; boardRow<BOARD_SIZE; boardRow++){
            out.print(SET_BG_COLOR_LIGHT_GREY);

        }
    }

    private static void drawRow (int boardRow, PrintStream out){

    }
}
