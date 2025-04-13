package ui.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.DrawGameBoard;
import websocket.messages.ServerMessage;

import java.util.Objects;

public class NotificationHandler {
    private ChessGame chessGame=new ChessGame();
    private String playerColor;

    public NotificationHandler (String playerColor){
        this.playerColor=playerColor;
    }

    public void toString(ServerMessage message){
//        System.out.println();
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)){
            String finalMessage=message.getNotificationMessage();
            System.out.println(finalMessage);
        } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
            String finalMessage=message.getErrorMessage();
            System.out.println(finalMessage);
        } else {
            ChessGame finalObject=message.getLoadGameObject();
            chessGame= finalObject;
            String newString=new Gson().toJson(finalObject);
            DrawGameBoard drawGameBoard=new DrawGameBoard();
            drawGameBoard.drawWholeBoard(chessGame, playerColor);
//            System.out.println("saved");
        }

    }

    public ChessGame getChessGame(){
        System.out.println("in NotificationHandler");
        return chessGame;
    }

//    public void setChessGame(ChessGame game){
//        System.out.println("Set game");
//        chessGame=game;
//    }
}
