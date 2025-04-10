package ui.websocket;

import com.google.gson.Gson;
import webSocketMessages.Notification;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;


public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url,NotificationHandler notificationHandler) throws Exception {
        try {
            url=url.replace("http", "ws");
            URI socketURI = new URI(url+"/ws");
            this.notificationHandler = notificationHandler;
            WebSocketContainer container=ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){
    }

    public void connect(String authToken, Integer gameID) throws IOException {
        UserGameCommand command=new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void makeMove(String authToken,Integer gameID) throws IOException {
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,authToken, gameID);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void leave(String authToken, Integer gameID){
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.LEAVE,authToken, gameID);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void resign (String authToken, Integer gameID){
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.RESIGN,authToken, gameID);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }



}
