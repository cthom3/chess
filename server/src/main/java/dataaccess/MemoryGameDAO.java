package dataaccess;
import model.GameData;
import chess.ChessGame;

public class MemoryGameDAO implements GameDAO {
    private int nextId = 1;
    public void createGame(String gameName){
        ChessGame newGame= new ChessGame();
        GameData game=new GameData(nextId, null,null,gameName, newGame);
        nextId+=1;
    }
}
