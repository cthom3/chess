package server.websocket;

import chess.ChessGame;
import chess.ChessGame.*;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final HashMap<Integer,ConnectionManager> connections = new HashMap<>();
    private final GameDAO gameDAO=new SqlGameDAO();
    private final AuthDAO authDAO= new SqlAuthDAO();
    private final Map<Integer, String> positionKey= Map.of(1,"a",2, "b",3, "c",4,
            "d", 5,"e", 6, "f", 7, "g", 8, "h");

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
                ServerMessage serverMessage= new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                connections.get(gameID).broadcast(currentUser,message);
                ServerMessage boardMessage=new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                boardMessage.setLoadGameObject(gameDAO.getGame(gameID).game());
                String sendingMessage= new Gson().toJson(boardMessage);
                session.getRemote().sendString(sendingMessage);
            } else if (currentUser.equals(whitePlayer)){
                String message=String.format("%s joined the game as WHITE", currentUser);
                connections.get(gameID).broadcast(currentUser,message);
                ServerMessage boardMessage=new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                boardMessage.setLoadGameObject(gameDAO.getGame(gameID).game());
                String sendingMessage= new Gson().toJson(boardMessage);
                session.getRemote().sendString(sendingMessage);
            } else {
                String message = String.format("%s is observing", currentUser);
                //            System.out.print("Message prepared");
                connections.get(gameID).broadcast(currentUser, message);
                ServerMessage boardMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                boardMessage.setLoadGameObject(gameDAO.getGame(gameID).game());
                String sendingMessage = new Gson().toJson(boardMessage);
                session.getRemote().sendString(sendingMessage);
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
        String gameName=gameDAO.getGame(gameID).gameName();
        String blackPlayer=gameDAO.getGame(gameID).blackUsername();
        String whitePlayer=gameDAO.getGame(gameID).whiteUsername();
        ChessGame game=gameDAO.getGame(gameID).game();
        String turnPlayer=game.getTeamTurn().toString();
//        System.out.println(game.getGameState());
        if (game.getGameState()==true){
//            Object newObject=gameDAO.getGame(gameID).game();
//            connections.get(gameID).reloadBoard(null,newObject);
            ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errors.setErrorMessage("Game Over");
            String sendingMessage = new Gson().toJson(errors);
            session.getRemote().sendString(sendingMessage);
        } else {
            if (!Objects.equals(currentUser, blackPlayer) & !Objects.equals(currentUser, whitePlayer)){
                ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errors.setErrorMessage("Observer cannot make moves");
                String sendingMessage = new Gson().toJson(errors);
                session.getRemote().sendString(sendingMessage);
            } else if (Objects.equals(turnPlayer, "WHITE") & !Objects.equals(currentUser, whitePlayer)){
                ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errors.setErrorMessage("It is WHITE turn");
                String sendingMessage = new Gson().toJson(errors);
                session.getRemote().sendString(sendingMessage);
            } else if (Objects.equals(turnPlayer, "BLACK") & !Objects.equals(currentUser, blackPlayer)){
                ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                errors.setErrorMessage("It is BLACK turn");
                String sendingMessage = new Gson().toJson(errors);
                session.getRemote().sendString(sendingMessage);
            }else {
                ChessMove wantedMove = command.getMove();
                try {
                    game.makeMove(wantedMove);
                    GameData newGameData= new GameData(gameID,whitePlayer,blackPlayer,gameName,game);
                    gameDAO.updateGame(newGameData);
                    ChessPosition startPosition=wantedMove.getStartPosition();
                    ChessPosition endPosition=wantedMove.getEndPosition();
//                    System.out.println(positionKey.get(startPosition.getColumn()+1));
                    String start=String.join("",positionKey.get(startPosition.getColumn()+1),Integer.toString(startPosition.getRow()+1));
                    String end=String.join("",positionKey.get(endPosition.getColumn()+1),Integer.toString(endPosition.getRow()+1));
                    String message = String.format("%s moved from %s to %s", currentUser, start, end);
                    connections.get(gameID).broadcast(currentUser, message);
                    ChessGame newObject = gameDAO.getGame(gameID).game();
                    connections.get(gameID).reloadBoard(null, newObject);
                    TeamColor playerColor=null;
                    System.out.println(playerColor);
                    if (Objects.equals(turnPlayer, "BLACK")){
                        System.out.println("WHITE");
                        playerColor=TeamColor.WHITE;
                    } else {
                        System.out.println("WHITE");
                        playerColor=TeamColor.BLACK;
                    }
                    if (newObject.isInCheckmate(playerColor)){
                        System.out.println("checkmate");
                        String messageCheckmate = String.format ("%s is in checkmate", currentUser);
                        connections.get(gameID).broadcast(null, messageCheckmate);
                    } else if (newObject.isInStalemate(playerColor)){
                        System.out.println("stalemate");
                        String messageStalemate="Stalemate. Game over.";
                        connections.get(gameID).broadcast(null,messageStalemate);
                    } else if (newObject.isInCheck(playerColor)){
                        System.out.println("check");
                        String messageCheck=String.format("%s is in check", currentUser);
                        connections.get(gameID).broadcast(null,messageCheck);
                    }
                } catch (InvalidMoveException ex) {
                    ServerMessage errors = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    errors.setErrorMessage(ex.getMessage());
                    String sendingMessage = new Gson().toJson(errors);
                    session.getRemote().sendString(sendingMessage);
                }
            }
        }
    }

    private void leave(UserGameCommand command,Session session, String currentUser) throws IOException, DataAccessException {
        Integer gameID=command.getGameID();
        ChessGame game=gameDAO.getGame(gameID).game();
        String gameName=gameDAO.getGame(gameID).gameName();
        String blackPlayer=gameDAO.getGame(gameID).blackUsername();
        String whitePlayer=gameDAO.getGame(gameID).whiteUsername();
        if (Objects.equals(currentUser, blackPlayer)){
            GameData newGameData= new GameData(gameID,whitePlayer,null,gameName,game);
            gameDAO.updateGame(newGameData);
        } else if (Objects.equals(currentUser,whitePlayer)){
            GameData newGameData= new GameData(gameID,null,blackPlayer,gameName,game);
            gameDAO.updateGame(newGameData);
        }

        connections.get(gameID).remove(currentUser);
        String message=String.format("%s left the game",currentUser);
        connections.get(gameID).broadcast(currentUser, message);
    }

    private void resign(UserGameCommand command, Session session, String currentUser) throws IOException, DataAccessException {
        Integer gameID=command.getGameID();
        ChessGame game=gameDAO.getGame(gameID).game();
        String gameName=gameDAO.getGame(gameID).gameName();
        String blackPlayer=gameDAO.getGame(gameID).blackUsername();
        String whitePlayer=gameDAO.getGame(gameID).whiteUsername();
        Boolean resigned=game.getGameState();
        if (!Objects.equals(currentUser, blackPlayer) & !Objects.equals(currentUser, whitePlayer)){
            ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errors.setErrorMessage("Observer cannot Resign");
            String sendingMessage = new Gson().toJson(errors);
            session.getRemote().sendString(sendingMessage);
        } else if (resigned==true){
            ServerMessage errors=new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errors.setErrorMessage("Game already ended");
            String sendingMessage = new Gson().toJson(errors);
            session.getRemote().sendString(sendingMessage);
        } else {
            game.setGameState(true);
            GameData newGameData= new GameData(gameID,whitePlayer,blackPlayer,gameName,game);
            gameDAO.updateGame(newGameData);
            String message = String.format("%s has resigned the game", currentUser);
            connections.get(gameID).broadcast(null, message);
        }
    }
}
