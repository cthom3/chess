package ui;

import dataaccess.DataAccessException;
import service.gamerecords.*;
import service.userrecords.*;

import java.util.Arrays;
import java.util.Objects;

public class PostLoginClient {
    private String authToken;
    private final ServerFacade server;
    private final String serverUrl;

    public PostLoginClient(String serverUrl){
        server=new ServerFacade(serverUrl);
        this.serverUrl=serverUrl;
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

    public String logout() throws DataAccessException{
        LogoutRequest request=new LogoutRequest(authToken);
        LogoutResult result=server.logout(request);
        authToken=null;
        return ("Logout Successful");
    }

    public String createGame(String... params) throws DataAccessException{
        if (params.length >=1){
            var gameName=params[0];
            CreateGameRequest request = new CreateGameRequest(gameName,authToken);
            CreateGameResult result=server.createGame(request);
            return String.format("%s created", gameName);
        }
        return ("Missing Information: need game name");
    }

    public String listGames() throws DataAccessException {
        ListGamesRequest request=new ListGamesRequest(authToken);
        ListGamesResult result = server.listGames(request);
        return result.games().toString();
        // potentially need to fix how this is printed out
    }

    public String playGame(String... params) throws DataAccessException {
        if (params.length >=2){
           var gameNumber=params[0];
           var playerColor=params[1];
           JoinGameRequest request = new JoinGameRequest(playerColor,Integer.parseInt(gameNumber),authToken);
           JoinGameResult result=server.joinGame(request);
           if (Objects.equals(playerColor, "WHITE")){
               return String.format("Successfully joined game as white player");
           } else {
               return String.format("Successfully joined game as black player");
           }
        }
        return ("Missing Information");
    }

    public String observeGame(String...params) throws DataAccessException {
        if (params.length >=1){
            var gameNumber=params[0];
            JoinGameRequest request = new JoinGameRequest(null,Integer.parseInt(gameNumber),authToken);
            JoinGameResult result=server.joinGame(request);
            String message= String.format("Successfully joined game as observer");
            return message;
        }
        return ("Missing game number");
    }

    public String help(){
        return """
                - create <gameName>
                - play <playerColor gameNumber>
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
