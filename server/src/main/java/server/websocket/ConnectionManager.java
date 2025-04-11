package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

public class ConnectionManager {
    public final ConcurrentHashMap<String,Connection> connections= new ConcurrentHashMap<>();

    public ConnectionManager(){};

    public void add(String currentUser, Session session){
        var connection = new Connection(currentUser,session);
        connections.put(currentUser,connection);
    }

    public void remove(String currentUser){
        connections.remove(currentUser);
    }

    public void broadcast(String excludeUser, String notification) throws IOException {
        ArrayList<Connection> removeList=new ArrayList<Connection>();
        System.out.print("Got to broadcast function");
        for (var connection: connections.values()){
            if (connection.session.isOpen()){
                if (!connection.currentUser.equals(excludeUser)){
                    connection.send(notification);
                }
            } else{
                removeList.add(connection);
            }
        }
        for (var connection: removeList){
            connections.remove(connection.currentUser);
        }
    }
}

