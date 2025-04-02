package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String,Connection> connections= new ConcurrentHashMap<>();

    public void add(String visitorName, Session session){
        var connection = new Connection(visitorName,session);
        connections.put(visitorName,connection);
    }
}

