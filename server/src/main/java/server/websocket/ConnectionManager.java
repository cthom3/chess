package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

public class ConnectionManager {
    public final ConcurrentHashMap<String,Connection> connections= new ConcurrentHashMap<>();

    public ConnectionManager(){}

    public void add(String currentUser, Session session){
        var connection = new Connection(currentUser,session);
//        System.out.println(connection);
//        System.out.println("new Connection");
        connections.put(currentUser,connection);
//        System.out.println(connections);
    }

    public void remove(String currentUser){
        connections.remove(currentUser);
    }

    public void broadcast(String excludeUser, String notification) throws IOException {
        ArrayList<Connection> removeList=new ArrayList<Connection>();
//        System.out.print("Got to broadcast function");
        for (var connection: connections.values()){
//            System.out.println(connection);
            if (connection.session.isOpen()){
//                System.out.println("OpenSession");
                if (!connection.currentUser.equals(excludeUser)){
//                    System.out.println("Another User");
                    connection.send(notification);
//                    System.out.println("Was notification sent?");
                }
            } else{
                removeList.add(connection);
            }
        }
        for (var connection: removeList){
            connections.remove(connection.currentUser);
        }
    }

    public void reloadBoard(String excludeUser, Object notification) throws IOException {
        ArrayList<Connection> removeList=new ArrayList<Connection>();
//        System.out.print("Got to broadcast function");
        for (var connection: connections.values()){
//            System.out.println(connection);
            if (connection.session.isOpen()){
//                System.out.println("OpenSession");
                if (!connection.currentUser.equals(excludeUser)){
//                    System.out.println("Another User");
                    connection.reload(notification);
//                    System.out.println("Was notification sent?");
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

