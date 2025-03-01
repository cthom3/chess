package dataaccess;
import model.GameData;
import chess.ChessGame;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextId = 1;
    public void createGame(String gameName){
        ChessGame newGame= new ChessGame();
        GameData game=new GameData(nextId, null,null,gameName, newGame);
        games.put(game.gameID(),game);
        nextId+=1;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Collection<GameData> listGames(){
        return games.values();
    }

    public GameData updateGame (int gameID){
        GameData originalGame=games.get(gameID);
        GameData updatedGame=new GameData(
                originalGame.gameID(),originalGame.whiteUsername(),
                originalGame.blackUsername(), originalGame.gameName(),
                originalGame.game());
        games.put(gameID,updatedGame);
        return updatedGame;
    }

}
