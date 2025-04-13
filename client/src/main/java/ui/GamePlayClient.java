package ui;


import chess.ChessMove;
import chess.ChessPosition;
import ui.websocket.WebSocketFacade;
import ui.websocket.NotificationHandler;

import java.io.IOException;
import java.util.*;

public class GamePlayClient {
    private final ServerFacade server;
    private final String serverUrl;
    private final WebSocketFacade webSocketFacade;
    private final NotificationHandler notificationHandler;
    private final Map<String, Integer> positionKey= Map.of("a",1, "b",2, "c",3,
            "d", 4,"e", 5, "f", 6, "g", 7, "h", 8);
    private String authToken;
    private String playerColor;


    public GamePlayClient(String serverUrl, WebSocketFacade webSocketFacade, NotificationHandler notificationHandler, String playerColor) {
        server=new ServerFacade(serverUrl);
        this.serverUrl=serverUrl;
        this.webSocketFacade=webSocketFacade;
        this.notificationHandler=notificationHandler;
        this.authToken=webSocketFacade.getAuthToken();
        Integer gameID=webSocketFacade.getGameID();
        this.playerColor=playerColor;

        DrawGameBoard drawGameBoard=new DrawGameBoard();
        if (Objects.equals(playerColor, "BLACK")){
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "black");
        } else {
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "white");
        }
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
        authToken=null;
        return "left game";
    }

    public String redraw()  {
        DrawGameBoard drawGameBoard=new DrawGameBoard();
        if (Objects.equals(playerColor, "BLACK")){
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "black");
        } else {
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "white");
        }
        return "";
    }

    public String movePiece() throws IOException {
        System.out.println("Move piece: <startSquare endSquare>");
        Scanner scanner= new Scanner(System.in);
        String input=scanner.nextLine();
        var positions=input.split(" ");
        var starting=positions[0].split("");
        var ending=positions[1].split("");
        ChessPosition start = new ChessPosition(Integer.parseInt(starting[1].trim()),positionKey.get(starting[0].trim()));
        ChessPosition end= new ChessPosition(Integer.parseInt(ending[1].trim()),positionKey.get(ending[0].trim()));
        ChessMove move=new ChessMove (start, end, null);
        webSocketFacade.makeMove(move);
//        DrawGameBoard drawGameBoard=new DrawGameBoard();
//        if (Objects.equals(playerColor, "BLACK")){
//            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "black");
//        } else {
//            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "white");
//        }
//        redraw();
        return "";
    }

    public String resign() throws IOException {
        System.out.println("Are you sure you want to resign the game? <yes | no>");
        Scanner scanner= new Scanner(System.in);
        String input=scanner.nextLine();
        if (Objects.equals(input, "yes")){
            webSocketFacade.resign();
            return "resigned";
        }
        return "continuing game";
    }

    public String highlight(){
        System.out.println("Which piece?: <startSquare>");
        Scanner scanner= new Scanner(System.in);
        String position=scanner.nextLine();
        var starting=position.split("");
        ChessPosition start = new ChessPosition(Integer.parseInt(starting[1]),positionKey.get(starting[0]));
        Collection<ChessMove> potentialMoves=notificationHandler.getChessGame().validMoves(start);
        HighlightGameBoard highlightGameBoard=new HighlightGameBoard();
        if (Objects.equals(playerColor, "BLACK")){
            highlightGameBoard.highlightAll(notificationHandler.getChessGame(), potentialMoves,start,"black");
        } else {
            highlightGameBoard.highlightAll(notificationHandler.getChessGame(), potentialMoves,start,"white");
        }
        return "";
    }

    public String help(){
        return """
                - redraw
                - move
                - highlight
                - resign
                - leave
                - quit
                - help
                """;
    }
}
