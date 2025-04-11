package ui;
import ui.websocket.WebSocketFacade;
import websocket.messages.NotificationHandler;

import java.util.Scanner;

import static ui.State.*;

public class Repl {
    private final PreLoginClient loginClient;
    private final PostLoginClient loggedinClient;
    private GamePlayClient gameClient;
    private State state= SIGNEDOUT;
    private final NotificationHandler notificationHandler;
    private final String serverUrl;

    public Repl(String serverUrl) throws Exception {
        loginClient=new PreLoginClient(serverUrl);
        notificationHandler=new NotificationHandler();
        loggedinClient=new PostLoginClient(serverUrl);
        this.serverUrl=serverUrl;
    }

    public void run() {
        System.out.println("Welcome to Chess. Options:");
        System.out.println(loginClient.help());
        Scanner scanner = new Scanner(System.in);
        var result="";
        while (!result.equals("quit")){
            printPrompt();
            String line=scanner.nextLine();
            if (state==SIGNEDOUT){
                try {
                    result=loginClient.eval(line);
                    if (result.contains("Welcome")){
                        var tokens=result.split(" ");
                        String authTokenNew = tokens[2];
                        loggedinClient.setAuthToken(authTokenNew);
                        System.out.print(tokens[0]+" "+tokens[1]);
                        setState(SIGNEDIN);
                    } else {
                        System.out.print(result);
                    }
                } catch (Throwable e){
                    System.out.print(e.toString());
                }
            } else if (state==SIGNEDIN){
                try {
                    result=loggedinClient.eval(line);
                    System.out.print(result);
                    if (result.contains("Successfully")){
                        String authToken=loggedinClient.getAuthToken();
                        Integer gameID=loggedinClient.getGameID();
                        WebSocketFacade webSocketFacade=new WebSocketFacade(serverUrl, notificationHandler);
                        webSocketFacade.connect(authToken,gameID);
                        gameClient=new GamePlayClient(serverUrl, webSocketFacade);
                    }
                    if (result.contains("black")& result.contains("Successfully")){
                        CreateGameBoard.main("black");
                        setState(INGAME);
                    } else if (result.contains("white")& result.contains("Successfully")) {
                        CreateGameBoard.main("white");
                        setState(INGAME);
                    } else if (result.contains("observer")& result.contains("Successfully")) {
                        CreateGameBoard.main("white");
                        setState(INGAME);
                    }
                    if (result.contains("Logout")){
                        setState(SIGNEDOUT);
                    }
                } catch (Throwable e){
                    System.out.print(e.toString());
                }
            } else {
                try{
                    result=gameClient.eval(line);
                    System.out.print(result);
                    if (result.contains("left")){
                        setState(SIGNEDIN);
                    }
                } catch (Throwable e) {
                    System.out.print(e.toString());
                }
            }
        }
        System.out.println();

    }

    private void printPrompt(){
        System.out.print("\n" + ">>>");
    }

    public void setState(State newState){
        this.state=newState;
    }
}
