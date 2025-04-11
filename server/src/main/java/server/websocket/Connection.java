package server.websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String currentUser;
    public Session session;

    public Connection (String currentUser,Session session){
        this.currentUser=currentUser;
        this.session = session;
    }

    public void send(String msg) throws IOException {
//        System.out.println("Getting ready to send back message");
        ServerMessage newMessage=new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        newMessage.setNotification(msg);
        String sendingMessage= new Gson().toJson(newMessage);
        session.getRemote().sendString(sendingMessage);
    }

    public void reload(Object msg) throws IOException {
//        System.out.println("Getting ready to send back message");
        ServerMessage boardMessage=new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        boardMessage.setLoadGameObject(msg.toString());
        String sendingMessage = new Gson().toJson(boardMessage);
        session.getRemote().sendString(sendingMessage);
    }
}
