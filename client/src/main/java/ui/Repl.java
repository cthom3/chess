package ui;
import java.util.Scanner;

public class Repl {
    private final PreLoginClient loginClient;
//    private final PostLoginClient loggedinClient;

    public Repl(String serverUrl){
        loginClient=new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess. Options:");
        System.out.println(loginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result="";
        while (!result.equals("quit")){
            printPrompt();
            String line=scanner.nextLine();
            try {
                result=loginClient.eval(line);
                System.out.print(result);
            } catch (Throwable e){
                System.out.print(e.toString());
            }
        }
        System.out.println();

    }

    private void printPrompt(){
        System.out.print("\n" + ">>>");
    }
}
