package ui.websocket;

import com.google.gson.Gson;
import webSocketMessages.Notification;
import javax.websocket.*;
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



}
