package server.websocket;

import chess.ChessGame;
import chess.ChessGame.*;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final HashMap<Integer,ConnectionManager> connections = new HashMap<>();
    private final GameDAO gameDAO=new SqlGameDAO();
    private final AuthDAO authDAO= new SqlAuthDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        if (authDAO.getAuth(command.getAuthToken())==null){
            ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errors.setErrorMessage("Not authorized");
            String sendingMessage = new Gson().toJson(errors);
            session.getRemote().sendString(sendingMessage);
        } else {
            String username = authDAO.getAuth(command.getAuthToken()).username();
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, session, username);
                case MAKE_MOVE -> makeMove(command, session, username);
                case LEAVE -> leave(command, session, username);
                case RESIGN -> resign(command, session, username);
            }
        }
    }

    private void connect(UserGameCommand command,Session session, String currentUser) throws IOException, DataAccessException {
        Integer gameID=command.getGameID();
        if (gameDAO.getGame(gameID)!=null){
    //        System.out.print("connections in place");
            if (connections.get(gameID)!=null) {
                connections.get(gameID).add(currentUser, session);
    //            System.out.print("User added to connections list");
            } else {
                connections.put(gameID,new ConnectionManager());
                connections.get(gameID).add(currentUser, session);
    //            System.out.print("GameID added to connections HashMap");
            }
            String blackPlayer=gameDAO.getGame(gameID).blackUsername();
            String whitePlayer=gameDAO.getGame(gameID).whiteUsername();
            if (currentUser.equals(blackPlayer)){
                String message=String.format("%s joined the game as BLACK", currentUser);
    //            System.out.print("Message prepared");
                ServerMessage serverMessage= new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                connections.get(gameID).broadcast(currentUser,message);
                ServerMessage boardMessage=new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                boardMessage.setLoadGameObject(gameDAO.getGame(gameID).game().toString());
                String sendingMessage= new Gson().toJson(boardMessage);
                System.out.print(sendingMessage);
                session.getRemote().sendString(sendingMessage);
    //            System.out.print ("Should have broadcasted");
            } else if (currentUser.equals(whitePlayer)){
                String message=String.format("%s joined the game as WHITE", currentUser);
    //            System.out.print("Message prepared");
                connections.get(gameID).broadcast(currentUser,message);
                ServerMessage boardMessage=new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                boardMessage.setLoadGameObject(gameDAO.getGame(gameID).game());
                String sendingMessage= new Gson().toJson(boardMessage);
                System.out.print(sendingMessage);
                session.getRemote().sendString(sendingMessage);
    //            System.out.print ("Should have broadcasted");
            } else {
                String message = String.format("%s is observing", currentUser);
                //            System.out.print("Message prepared");
                connections.get(gameID).broadcast(currentUser, message);
                ServerMessage boardMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                boardMessage.setLoadGameObject(gameDAO.getGame(gameID).game().toString());
                String sendingMessage = new Gson().toJson(boardMessage);
                System.out.print(sendingMessage);
                session.getRemote().sendString(sendingMessage);
                //            System.out.print ("Should have broadcasted");
            }
        } else {
            ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errors.setErrorMessage("GameID does not exist");
            String sendingMessage = new Gson().toJson(errors);
            session.getRemote().sendString(sendingMessage);
        }
    }

    private void makeMove(UserGameCommand command,Session session, String currentUser) throws IOException, DataAccessException, InvalidMoveException {
        Integer gameID=command.getGameID();
        String blackPlayer=gameDAO.getGame(gameID).blackUsername();
        String whitePlayer=gameDAO.getGame(gameID).whiteUsername();
        ChessGame game=gameDAO.getGame(gameID).game();
        String turnPlayer=game.getTeamTurn().toString();
        if (!Objects.equals(currentUser, blackPlayer) & !Objects.equals(currentUser, whitePlayer)){
            ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errors.setErrorMessage("Observer cannot make moves");
            String sendingMessage = new Gson().toJson(errors);
            session.getRemote().sendString(sendingMessage);
        } else {
            ChessMove wantedMove=command.getMove();
            try {
                game.makeMove(wantedMove);
                String message = String.format("%s moved from __ to __", currentUser);
                connections.get(gameID).broadcast(currentUser, message);
                ServerMessage boardMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                boardMessage.setLoadGameObject(game.toString());
                String sendingMessage = new Gson().toJson(boardMessage);
                System.out.print(sendingMessage);
                session.getRemote().sendString(sendingMessage);
            } catch (InvalidMoveException ex){
                ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errors.setErrorMessage(ex.getMessage());
                String sendingMessage = new Gson().toJson(errors);
                session.getRemote().sendString(sendingMessage);
            }
        }
    }

    private void leave(UserGameCommand command,Session session, String currentUser) throws IOException {
        Integer gameID=command.getGameID();
        connections.get(gameID).remove(currentUser);
        String message=String.format("%s left the game",currentUser);
        connections.get(gameID).broadcast(currentUser, message);
    }

    private void resign(UserGameCommand command, Session session, String currentUser) throws IOException, DataAccessException {
        Integer gameID=command.getGameID();
        String blackPlayer=gameDAO.getGame(gameID).blackUsername();
        String whitePlayer=gameDAO.getGame(gameID).whiteUsername();
        if (!Objects.equals(currentUser, blackPlayer) & !Objects.equals(currentUser, whitePlayer)){
            ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errors.setErrorMessage("Observer cannot Resign");
            String sendingMessage = new Gson().toJson(errors);
            session.getRemote().sendString(sendingMessage);
        } else {
            String message = String.format("%s has resigned the game", currentUser);
            connections.get(gameID).broadcast(null, message);
        }
    }
}
