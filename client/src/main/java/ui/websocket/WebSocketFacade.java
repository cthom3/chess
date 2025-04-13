package ui.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;


public class WebSocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;
    String savedAuthToken=null;
    Integer savedGameID=null;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url=url.replace("http", "ws");
            URI socketURI = new URI(url+"/ws");
            this.notificationHandler = notificationHandler;
            WebSocketContainer container=ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>(){
                @Override
                public void onMessage(String message){
//                    System.out.println("sent made it WebSocketFacade");
//                    System.out.println(message);
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.toString(notification);
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
        UserGameCommand command=new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, null);
        savedAuthToken=authToken;
        savedGameID=gameID;
//        System.out.print("Truly Connected");
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void makeMove(ChessMove move) throws IOException {
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,savedAuthToken, savedGameID, move);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void leave() throws IOException {
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.LEAVE,savedAuthToken, savedGameID, null);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void resign () throws IOException {
        UserGameCommand command= new UserGameCommand(UserGameCommand.CommandType.RESIGN, savedAuthToken, savedGameID, null);
        session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public String getAuthToken() {
        return savedAuthToken;
    }

    public Integer getGameID(){
        return savedGameID;
    }



}
