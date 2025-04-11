package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;

public class NotificationHandler {
    private ChessGame chessgame=null;

    public void toString(ServerMessage message){
//        System.out.println("made it to NotificationHandler");
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)){
            String finalMessage=message.getNotificationMessage();
            System.out.println(finalMessage);
        } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
            String finalMessage=message.getErrorMessage();
            System.out.println(finalMessage);
        } else {
            Object finalObject=message.getLoadGameObject();
            ChessGame chessboard= (ChessGame) finalObject;
            String newString=new Gson().toJson(finalObject);
            System.out.println(newString);
        }

    }

    public ChessGame getChessGame(){
        return chessgame;
    }
}
