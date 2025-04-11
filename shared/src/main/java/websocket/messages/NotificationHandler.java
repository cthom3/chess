package websocket.messages;

import com.google.gson.Gson;

public class NotificationHandler {
    public void toString(ServerMessage message){
        String finalMessage=new Gson().toJson(message);
        System.out.println(finalMessage);
    }
}
