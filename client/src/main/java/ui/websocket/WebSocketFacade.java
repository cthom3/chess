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
    String savedAuthToken=null;
    Integer savedGameID=null;

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
        savedAuthToken=authToken;
        savedGameID=gameID;
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void makeMove() throws IOException {
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,savedAuthToken, savedGameID);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void leave() throws IOException {
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.LEAVE,savedAuthToken, savedGameID);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void resign () throws IOException {
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.RESIGN, savedAuthToken, savedGameID);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }



}
