package ui;
import userrecords.*;

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
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex){
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {
            var username=params[0];
            var password=params[1];
            var email= params[2];
            RegisterRequest request=new RegisterRequest(username, password, email);
            RegisterResult result=server.register(request);
            if (result.statusCode()==200){
                LoginRequest request1=new LoginRequest(username,password);
                LoginResult result1= server.login(request1);
                return String.format("Welcome, %s",result1.username()+" "+result1.authToken());
            } else {
                return (result.message());
            }
        }
        return String.format("Missing information: <username password email>");
    }

    public String login(String... params) throws Exception{
        if (params.length>=2){
            var username=params[0];
            var password=params[1];
            LoginRequest request=new LoginRequest(username,password);
            LoginResult result= server.login(request);
            if (result.statusCode()!=200){
                return result.message();
            }
            return String.format("Welcome, %s",result.username()+" "+result.authToken());
        }
        return ("Missing information: <username password>");
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
