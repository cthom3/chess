package ui;


import ui.websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Arrays;

public class GamePlayClient {
    private final ServerFacade server;
    private final String serverUrl;
    private final WebSocketFacade webSocketFacade;


    public GamePlayClient(String serverUrl, WebSocketFacade webSocketFacade){
        server=new ServerFacade(serverUrl);
        this.serverUrl=serverUrl;
        this.webSocketFacade=webSocketFacade;
    }

    public String eval(String input) throws IOException {
        var tokens=input.split(" ");
        var command = (tokens.length > 0) ? tokens[0]: "help";
        var params= Arrays.copyOfRange(tokens,1,tokens.length);
        return switch (command) {
            case "leave" -> leave();
            case "redraw" -> redraw();
            case "move" -> movePiece();
            case "resign" -> resign();
            case "highlight" -> highlight();
            case "quit" -> "quit";
            default -> help();
        };
    }
    public String leave() throws IOException {
        webSocketFacade.leave();
        return "left game";
    }

    public String redraw(){
        return "new board";
    }

    public String movePiece() throws IOException {
        webSocketFacade.makeMove();
        return "new location";
    }

    public String resign() throws IOException {
        System.out.println("Are you sure you want to resign the game?");
        // read input
        webSocketFacade.resign();
        return "resigned";
    }

    public String highlight(){
        return "highlighted";
    }

    public String help(){
        return """
                - leave
                - quit
                - help
                """;
    }
}
