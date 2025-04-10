package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import webSocketMessages.Notification;

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

    public void broadcast(String excludeUser, Notification notification) throws IOException {
        ArrayList<Connection> removeList=new ArrayList<Connection>();
        for (var connection: connections.values()){
            if (connection.session.isOpen()){
                if (!connection.currentUser.equals(excludeUser)){
                    connection.send(notification.toString());
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

