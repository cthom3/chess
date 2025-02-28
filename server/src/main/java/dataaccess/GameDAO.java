package dataaccess;
import model.GameData;

public interface GameDAO {
    void createGame()
            throws DataAccessException;

    GameData getGame()
            throws DataAccessException;


}
