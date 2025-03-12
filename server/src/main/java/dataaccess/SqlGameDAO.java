package dataaccess;
import model.GameData;
import java.sql.SQLException;
import java.util.Collection;

public class SqlGameDAO implements GameDAO{
    public SqlGameDAO() throws DataAccessException,SQLException{
        configureDatabase();
    }

    public int createGame(String gameName) throws DataAccessException{

    }

    public GameData getGame(int gameID) throws DataAccessException{

    }

    public Collection<GameData> listGames() throws DataAccessException{

    }

    public void updateGame(GameData gameData){

    }

    public void clear (){

    }

}
