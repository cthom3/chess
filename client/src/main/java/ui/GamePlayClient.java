package ui;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.*;
import ui.websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationHandler;

import java.io.IOException;
import java.util.*;

public class GamePlayClient {
    private final ServerFacade server;
    private final String serverUrl;
    private final WebSocketFacade webSocketFacade;
    private final NotificationHandler notificationHandler;
    private final GameDAO gameDAO=new SqlGameDAO();
    private final AuthDAO authDAO= new SqlAuthDAO();
    private final Map<String, Integer> positionKey= Map.of("a",1, "b",2, "c",3,
            "d", 4,"e", 5, "f", 6, "g", 7, "h", 8);
    private String authToken;
    private String userName;
    private String blackPlayer;
    private String whitePlayer;


    public GamePlayClient(String serverUrl, WebSocketFacade webSocketFacade, NotificationHandler notificationHandler) throws DataAccessException {
        server=new ServerFacade(serverUrl);
        this.serverUrl=serverUrl;
        this.webSocketFacade=webSocketFacade;
        this.notificationHandler=notificationHandler;
        this.authToken=webSocketFacade.getAuthToken();
        this.userName=authDAO.getAuth(authToken).username();
        Integer gameID=webSocketFacade.getGameID();
        blackPlayer=gameDAO.getGame(gameID).blackUsername();
        whitePlayer=gameDAO.getGame(gameID).whiteUsername();
        DrawGameBoard drawGameBoard=new DrawGameBoard();
        if (Objects.equals(userName, blackPlayer)){
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "black");
        } else {
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "white");
        }
    }

    public String eval(String input) throws IOException, DataAccessException {
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

    public String redraw() throws DataAccessException {
        DrawGameBoard drawGameBoard=new DrawGameBoard();
        if (Objects.equals(userName, blackPlayer)){
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
        ChessPosition start = new ChessPosition(Integer.parseInt(starting[1]),positionKey.get(starting[0]));
        ChessPosition end= new ChessPosition(Integer.parseInt(ending[1]),positionKey.get(ending[0]));
        ChessMove move=new ChessMove (start, end, null);
        webSocketFacade.makeMove(move);
        DrawGameBoard drawGameBoard=new DrawGameBoard();
        if (Objects.equals(userName, blackPlayer)){
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "black");
        } else {
            drawGameBoard.drawWholeBoard(notificationHandler.getChessGame(), "white");
        }
        return "new location";
    }

    public String resign() throws IOException {
        System.out.println("Are you sure you want to resign the game? <yes | no>");
        Scanner scanner= new Scanner(System.in);
        String input=scanner.nextLine();
        if (Objects.equals(input, "yes")){
            webSocketFacade.resign();
            return "resigned";
        }
        return "continue game";
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
