package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import webSocketMessages.Action;
import webSocketMessages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        Action action = new Gson().fromJson(message, Action.class);
        switch (action.type()) {
            case PLAYER -> player(action.currentUser(), session);
            case OBSERVER -> observer (action.currentUser(), session);
            case MOVE -> move(action.currentUser(), session);
            case LEFT -> left(action.currentUser(),session);
            case RESIGN -> resign(action.currentUser(),session);
            case CHECK -> check(action.currentUser(), session);
            case CHECKMATE -> checkmate(action.currentUser(), session);
        }
    }

    private void player(String currentUser, Session session) throws IOException {
        connections.add(currentUser, session);
        String message=String.format("%s joined the game as ___", currentUser);
        Notification notification = new Notification(Notification.Type.PLAYER,message);
        connections.broadcast(currentUser,notification);
    }

    private void observer(String currentUser, Session session) throws IOException {
        connections.add(currentUser,session);
        String message=String.format("%s is observing", currentUser);
        Notification notification = new Notification(Notification.Type.OBSERVER,message);
        connections.broadcast(currentUser,notification);
    }

    private void move(String currentUser, Session session) throws IOException {
        connections.add(currentUser,session);
        String message=String.format("%s moved from __ to __", currentUser);
        Notification notification= new Notification(Notification.Type.MOVE, message);
        connections.broadcast(currentUser,notification);
    }

    private void left(String currentUser, Session session) throws IOException {
        connections.remove(currentUser);
        String message=String.format("%s left the game",currentUser);
        Notification notification=new Notification(Notification.Type.LEFT,message);
        connections.broadcast(currentUser, notification);
    }

    private void resign(String currentUser, Session session) throws IOException {
        connections.add(currentUser,session);
        String message=String.format("%s has resigned the game", currentUser);
        Notification notification = new Notification(Notification.Type.RESIGN,message);
        connections.broadcast(currentUser,notification);
    }

    private void check (String currentUser, Session session) throws IOException {
        connections.add(currentUser,session);
        String message=String.format("%s is in check", currentUser);
        Notification notification = new Notification(Notification.Type.CHECK,message);
        connections.broadcast(currentUser,notification);
    }

    private void checkmate(String currentUser, Session session){

    }
}
