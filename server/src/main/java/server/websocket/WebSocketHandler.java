package server.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {
    private final HashMap<Integer,ConnectionManager> connections = new HashMap<>();
    private final GameDAO gameDAO=new SqlGameDAO();
    private final AuthDAO authDAO= new SqlAuthDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        String username = authDAO.getAuth(command.getAuthToken()).username();
            switch (command.getCommandType()) {
                case CONNECT -> connect (command,session, username);
                case MAKE_MOVE -> makeMove(command,session, username);
                case LEAVE -> leave(command,session, username);
                case RESIGN -> resign(command,session, username);
            }
    }

    private void connect(UserGameCommand command,Session session, String currentUser) throws IOException, DataAccessException {
        Integer gameID=command.getGameID();
        System.out.print("connections in place");
        if (connections.get(gameID)!=null) {
            connections.get(gameID).add(currentUser, session);
            System.out.print("User added to connections list");
        } else {
            connections.put(gameID,new ConnectionManager());
            System.out.print("GameID added to connections HashMap");
        }
        String blackPlayer=gameDAO.getGame(gameID).blackUsername();
        String whitePlayer=gameDAO.getGame(gameID).whiteUsername();
        if (currentUser.equals(blackPlayer)){
            String message=String.format("%s joined the game as BLACK", currentUser);
            System.out.print("Message prepared");
            connections.get(gameID).broadcast(currentUser,message);
            System.out.print ("Should have broadcasted");
        } else if (currentUser.equals(whitePlayer)){
            String message=String.format("%s joined the game as WHITE", currentUser);
            System.out.print("Message prepared");
            connections.get(gameID).broadcast(currentUser,message);
            System.out.print ("Should have broadcasted");
        } else {
            String message=String.format("%s is observing", currentUser);
            System.out.print("Message prepared");
            connections.get(gameID).broadcast(currentUser,message);
            System.out.print ("Should have broadcasted");
        }
    }

    private void makeMove(UserGameCommand command,Session session, String currentUser) throws IOException {
        Integer gameID=command.getGameID();
        String message=String.format("%s moved from __ to __", currentUser);
        connections.get(gameID).broadcast(currentUser,message);
    }

    private void leave(UserGameCommand command,Session session, String currentUser) throws IOException {
        Integer gameID=command.getGameID();
        connections.get(gameID).remove(currentUser);
        String message=String.format("%s left the game",currentUser);
        connections.get(gameID).broadcast(currentUser, message);
    }

    private void resign(UserGameCommand command, Session session, String currentUser) throws IOException {
        Integer gameID=command.getGameID();
        String message=String.format("%s has resigned the game", currentUser);
        connections.get(gameID).broadcast(currentUser,message);
    }
}
