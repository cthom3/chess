package ui;


import java.util.Arrays;

public class GamePlayClient {
    private final ServerFacade server;
    private final String serverUrl;

    public GamePlayClient(String serverUrl){
        server=new ServerFacade(serverUrl);
        this.serverUrl=serverUrl;
    }

    public String eval(String input){
        var tokens=input.split(" ");
        var command = (tokens.length > 0) ? tokens[0]: "help";
        var params= Arrays.copyOfRange(tokens,1,tokens.length);
        return switch (command) {
            case "leave" -> leave();
            case "quit" -> "quit";
            default -> help();
        };
    }
    public String leave(){
        return "left game";
    }

    public String help(){
        return """
                - leave
                - quit
                - help
                """;
    }
}
