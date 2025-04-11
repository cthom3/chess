package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String currentUser;
    public Session session;

    public Connection (String currentUser,Session session){
        this.currentUser=currentUser;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        System.out.println("Getting ready to send back message");
        session.getRemote().sendString(msg);
    }
}
