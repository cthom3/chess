package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName)
            throws DataAccessException;

    GameData getGame(int gameID)
            throws DataAccessException;

    Collection <GameData> listGames()
            throws DataAccessException;

    GameData updateGame (int gameID)
            throws DataAccessException;

    void clear() throws DataAccessException;
}
