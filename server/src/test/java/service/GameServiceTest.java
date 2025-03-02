package service;
import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class GameServiceTest {
    private final GameDAO gameAccess=new MemoryGameDAO();
    private final AuthDAO authAccess= new MemoryAuthDAO();
    final GameService service = new GameService(gameAccess,authAccess);



    @Test
    public void createGamePositiveTest() throws DataAccessException {

    }
    public void createGameNegativeTest() throws DataAccessException {

    }
    public void listGamesPositiveTest() throws DataAccessException {

    }
    public void listGamesNegativeTest() throws DataAccessException {

    }
    public void joinGamePositiveTest() throws DataAccessException {

    }
    public void joinGameNegativeTest() throws DataAccessException {

    }
}
