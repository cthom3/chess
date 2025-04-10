package server.websocket;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import webSocketMessages.Notification;
import websocket.commands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO=new SqlGameDAO();
    private final AuthDAO authDAO= new SqlAuthDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        String username = authDAO.getAuth(command.getAuthToken()).username();
            switch (command.getCommandType()) {
                case CONNECT -> connect (session, username);
                case MAKE_MOVE -> makeMove(session, username);
                case LEAVE -> leave(session, username);
                case RESIGN -> resign(session, username);
            }
    }

    private void connect(Session session, String currentUser) throws IOException {
        connections.add(currentUser, session);
        String message=String.format("%s joined the game as __", currentUser);
        Notification notification = new Notification(Notification.Type.PLAYER,message);
        connections.broadcast(currentUser,notification);
    }

    private void makeMove(Session session, String currentUser) throws IOException {
        String message=String.format("%s moved from __ to __", currentUser);
        Notification notification= new Notification(Notification.Type.MOVE, message);
        connections.broadcast(currentUser,notification);
    }

    private void leave(Session session, String currentUser) throws IOException {
        connections.remove(currentUser);
        String message=String.format("%s left the game",currentUser);
        Notification notification=new Notification(Notification.Type.LEFT,message);
        connections.broadcast(currentUser, notification);
    }

    private void resign(Session session, String currentUser) throws IOException {
        String message=String.format("%s has resigned the game", currentUser);
        Notification notification = new Notification(Notification.Type.RESIGN,message);
        connections.broadcast(currentUser,notification);
    }


}
