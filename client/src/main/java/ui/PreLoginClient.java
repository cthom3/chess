package ui;
import dataaccess.DataAccessException;
import service.userrecords.LoginRequest;
import service.userrecords.LoginResult;
import service.userrecords.RegisterRequest;
import service.userrecords.RegisterResult;

import java.util.Scanner;
import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl){
        server=new ServerFacade (serverUrl);
        this.serverUrl=serverUrl;
    }

    public String eval (String input){
        try{
            var tokens=input.split(" ");
            var command=(tokens.length>0) ? tokens[0]: "help";
            var params=Arrays.copyOfRange(tokens,1,tokens.length);
            return switch (command){
                case "register" -> register(params);
                case "login" -> login(params);
                default -> help();
            };
        } catch (Exception ex){
            return ex.getMessage();
        }
    }

    public String register(String... params) throws DataAccessException {
        if (params.length >= 3) {
            var username=params[0];
            var password=params[1];
            var email= params[2];
            RegisterRequest request=new RegisterRequest(username, password, email);
            RegisterResult result=server.register(request);
            return ("Registration Successful");
        }
        return String.format("Error in Registration. Make sure you follow requested format");
    }

    public String login(String... params) throws DataAccessException{

        if (params.length>=2){
            var username=params[0];
            var password=params[1];
            LoginRequest request=new LoginRequest(username,password);
            LoginResult result= server.login(request);
            return String.format("Welcome, %s!", request.username());
        }
        return ("Not a registered user");
    }

    public String help(){
        return """
                - register <username password email>
                - login <username password>
                - quit
                - help
                """;
    }
}
