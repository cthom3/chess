package service;
import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import model.GameData;

public class GameServiceTest {
    private final GameDAO gameAccess=new MemoryGameDAO();
    private final AuthDAO authAccess= new MemoryAuthDAO();
    final GameService service = new GameService(gameAccess,authAccess);

    @BeforeEach
    void clear() throws DataAccessException{
        gameAccess.clear();
        authAccess.clear();
    }

    @Test
    public void createGamePositiveTest() throws DataAccessException {
//        authAccess.createAuth();
//        CreateGameResult actual=service.createGame(new CreateGameRequest("gameName", authToken));

    }
    @Test
    public void createGameNegativeTest() throws DataAccessException {

    }
    @Test
    public void listGamesPositiveTest() throws DataAccessException {

    }
    @Test
    public void listGamesNegativeTest() throws DataAccessException {

    }
    @Test
    public void joinGamePositiveTest() throws DataAccessException {

    }
    @Test
    public void joinGameNegativeTest() throws DataAccessException {

    }
}
