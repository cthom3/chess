package server.websocket;

import com.google.gson.Gson;
import messages.Notification;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import messages.Action;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
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

    private void player(String currentUser, Session session){
        connections.add(currentUser, session);
        String message=String.format("%s joined the game as ___", currentUser);
        Notification notification = new Notification(Notification.Type.PLAYER,message);
        connections.broadcast(currentUser,notification);
    }

    private void observer(String currentUser, Session session){

    }

    private void move(String currentUser, Session session){

    }

    private void left(String currentUser, Session session){

    }

    private void resign(String currentUser, Session session){

    }

    private void check (String currentUser, Session session){

    }

    private void checkmate(String currentUser, Session session){

    }
}
