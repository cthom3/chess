package dataaccess;
import model.GameData;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextId = 1;
    public int createGame(String gameName){
        ChessGame newGame= new ChessGame();
        GameData game=new GameData(nextId, null,null,gameName, newGame);
        games.put(game.gameID(),game);
        nextId+=1;
        return game.gameID();
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Collection<GameData> listGames(){
        return games.values();
    }

    public void updateGame (GameData gameData){
        int gameID=gameData.gameID();
        games.remove(gameID);
        games.put(gameID,gameData);
    }

    public void clear (){
        games.clear();
    }

}
