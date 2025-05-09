package dataaccess;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAOTest {
    private final GameDAO gameAccess=new SqlGameDAO();

    public SqlGameDAOTest() throws SQLException,DataAccessException{
    }

    @BeforeEach
    void clearAll() throws DataAccessException{
        gameAccess.clear();
    }

    @Test
    public void createGamePositiveTest() throws DataAccessException{
        int gameID=gameAccess.createGame("game1");
        GameData newGame=gameAccess.getGame(gameID);
        assertEquals("game1",newGame.gameName());
    }

    @Test
    public void createGameNegativeTest() throws DataAccessException{
        gameAccess.createGame("game1");
        try {
            gameAccess.createGame("game1");
        } catch (DataAccessException e){
            DataAccessException actual= new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void getGamePositiveTest() throws DataAccessException{
        int gameID=gameAccess.createGame("game1");
        GameData expectedGame=new GameData(gameID,null,null,"game1",null);
        GameData gameData=gameAccess.getGame(gameID);
        assertEquals(expectedGame.gameID(),gameData.gameID());
        assertEquals(expectedGame.gameName(),gameData.gameName());
    }

    @Test
    public void getGameNegativeTest() throws DataAccessException{
        try{
            gameAccess.getGame(321);
        } catch(DataAccessException e){
            DataAccessException actual=new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void listGamesPositiveTest() throws DataAccessException{
        gameAccess.createGame("game1");
        gameAccess.createGame("game2");
        gameAccess.createGame("game3");
        Collection<GameData> list=gameAccess.listGames();
        assertEquals(3,list.size());
    }

    @Test
    public void listGamesNegativeTest() throws DataAccessException{
        Collection<GameData> actual=gameAccess.listGames();
        Collection<GameData> expected= new ArrayList<GameData>();
        assertEquals(expected,actual);
    }

    @Test
    public void updateGamePositiveTest() throws DataAccessException{
        int gameID=gameAccess.createGame("game1");
        GameData gameData=gameAccess.getGame(gameID);
        GameData expected=new GameData(gameID,"username",null,"game1", gameData.game());
        gameAccess.updateGame(expected);
        GameData actual=gameAccess.getGame(gameID);
        assertEquals(expected.gameID(),actual.gameID());
        assertEquals(expected.gameName(),actual.gameName());
        assertEquals(expected.whiteUsername(),actual.whiteUsername());
        assertEquals(expected.blackUsername(),actual.blackUsername());
    }

    @Test
    public void updateGameNegativeTest() throws DataAccessException{
        try {
            GameData expected=new GameData(321,"username",null,"game1", null);
            gameAccess.updateGame(expected);
        } catch (DataAccessException e){
            DataAccessException actual=new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }

    }

    @Test
    public void clearTest() throws DataAccessException {
        int gameID=gameAccess.createGame("game1");
        gameAccess.clear();
        try {
            gameAccess.getGame(gameID);
        } catch (DataAccessException e){
            DataAccessException actual=new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }
}
