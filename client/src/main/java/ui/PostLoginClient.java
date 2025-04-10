package ui;


import model.GameData;
import server.websocket.ConnectionManager;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import userrecords.*;
import gamerecords.*;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class PostLoginClient {
    private String authToken;
    private final ServerFacade server;
    private final String serverUrl;
    private final Map<Integer,Integer> gameList;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    ConnectionManager allConnections= new ConnectionManager();


    public PostLoginClient(String serverUrl){
        server=new ServerFacade(serverUrl);
        this.serverUrl=serverUrl;
        gameList=new HashMap<>();
        authToken= null;

    }

    public String eval (String input)  {
        try {
            var tokens=input.split(" ");
            var command = (tokens.length > 0) ? tokens[0]: "help";
            var params= Arrays.copyOfRange(tokens,1,tokens.length);
            return switch (command){
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex){
            return ex.getMessage();
        }
    }

    public String logout() throws Exception{
        LogoutRequest request=new LogoutRequest(authToken);
        LogoutResult result=server.logout(request);
        authToken=null;
        return ("Logout Successful");
    }

    public String createGame(String... params) throws Exception{
        if (params.length >=1){
            var gameName=params[0];
            CreateGameRequest request = new CreateGameRequest(gameName,authToken);
            CreateGameResult result=server.createGame(request);
            return String.format("%s created", gameName);
        }
        return ("Missing Information: need game name");
    }

    public String listGames() throws Exception {
        ListGamesRequest request=new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request);
        if (result.games() != null) {
            if (result.games().isEmpty()){
                return "No games";
            }
            gameList.clear();
            Integer i=1;
            String finalString="#: gameName, whiteUsername, blackUsername";
            for (GameData game: result.games()){
                gameList.put (i,game.gameID());
                String newString=("\n" + i.toString()+ ": "+game.gameName()+", "+game.whiteUsername()+", "+game.blackUsername());
                finalString=finalString.concat(newString);
                i++;
            }
            return finalString;
        } else{
            return "No games found";
        }
    }

    public String playGame(String... params) throws Exception {
        if (params.length >=2){
           var gameNumber=params[0];
           if (gameNumber.equals("WHITE")| gameNumber.equals("BLACK")){
               return "Wrong order <gameNumber [WHITE | BLACK]>";
           }
           Integer gameID=gameList.get(Integer.parseInt(gameNumber));
           var playerColor=params[1];
           JoinGameRequest request = new JoinGameRequest(playerColor,gameID,authToken);
           JoinGameResult result=server.joinGame(request);
           if (result.statusCode()!=200){
               return "Spot already full. Choose another spot or create Game.";
           }
           ws = new WebSocketFacade(serverUrl, notificationHandler);
           allConnections.add(userName,session);
           if (Objects.equals(playerColor, "WHITE")){
               return String.format("Successfully joined game as white player");
           } else {
               return String.format("Successfully joined game as black player");
           }
        }
        return ("Missing Information");
    }

    public String observeGame(String...params) throws Exception {
        if (params.length >=1){
            var gameNumber=params[0];
            Integer gameID=gameList.get(Integer.parseInt(gameNumber));
            ws = new WebSocketFacade(serverUrl, notificationHandler);

            String message= String.format("Successfully joined game as observer");
            return message;
        }
        return ("Missing game number");
    }

    public String help(){
        return """
                - create <gameName>
                - play <gameNumber [WHITE | BLACK]>
                - observe <gameNumber>
                - list
                - logout
                - quit
                - help
                """;
    }

    public void setAuthToken(String newAuthToken){
        this.authToken=newAuthToken;
    }



}
