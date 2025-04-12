package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;

public class NotificationHandler {
    private ChessGame chessGame=new ChessGame();

    public void toString(ServerMessage message){
//        System.out.println("made it to NotificationHandler");
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
            System.out.println("saved");
        }

    }

    public ChessGame getChessGame(){
        System.out.println("in NotificationHandler");
        return chessGame;
    }

    public void setChessGame(ChessGame game){
        System.out.println("Set game");
        chessGame=game;
    }
}
