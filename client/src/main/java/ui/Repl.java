package ui;
import java.util.Scanner;

import static ui.State.SIGNEDIN;
import static ui.State.SIGNEDOUT;

public class Repl {
    private final PreLoginClient loginClient;
    private final PostLoginClient loggedinClient;
    private State state= SIGNEDOUT;

    public Repl(String serverUrl){
        loginClient=new PreLoginClient(serverUrl);
        loggedinClient=new PostLoginClient(serverUrl);
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
                    setState(SIGNEDIN);
                    System.out.print(result);
                } catch (Throwable e){
                    System.out.print(e.toString());
                }
            } else {
                try {
                    result=loggedinClient.eval(line);
                    System.out.print(result);
                } catch (Throwable e){
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
