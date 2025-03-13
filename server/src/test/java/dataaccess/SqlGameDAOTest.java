package dataaccess;
import model.GameData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.sql.SQLException;

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

    }

    @Test
    public void createGameNegativeTest() throws DataAccessException{

    }

    @Test
    public void getGamePositiveTest() throws DataAccessException{

    }

    @Test
    public void getGameNegativeTest() throws DataAccessException{

    }

    @Test
    public void listGamesPositiveTest() throws DataAccessException{

    }

    @Test
    public void listGamesNegativeTest() throws DataAccessException{

    }

    @Test
    public void updateGamePositiveTest() throws DataAccessException{

    }

    @Test
    public void updateGameNegativeTest() throws DataAccessException{

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
