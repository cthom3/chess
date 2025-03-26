package service;
import dataaccess.*;
import gamerecords.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

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
        String authToken= UUID.randomUUID().toString();
        authAccess.createAuth(authToken,"username");
        CreateGameResult actual=service.createGame(new CreateGameRequest("gameName", authToken));
        CreateGameResult expected=new CreateGameResult(200,actual.gameID(),null);
        assertEquals(expected,actual);
        assertNotNull(actual.gameID());

    }
    @Test
    public void createGameNegativeTest() throws DataAccessException {
        CreateGameResult actual=service.createGame(new CreateGameRequest("gameName", null));
        CreateGameResult expected=new CreateGameResult(401,null,"Error: unauthorized");
        assertEquals(expected,actual);
    }
    @Test
    public void listGamesPositiveTest() throws DataAccessException {
        String authToken= UUID.randomUUID().toString();
        authAccess.createAuth(authToken,"username");
        CreateGameResult newGame=service.createGame(new CreateGameRequest("gameName", authToken));
        ListGamesResult actual=service.listGames(new ListGamesRequest(authToken));
        assertNotNull(actual.games());
    }
    @Test
    public void listGamesNegativeTest() throws DataAccessException {
        ListGamesResult actual=service.listGames(new ListGamesRequest(null));
        ListGamesResult expected=new ListGamesResult(401,null ,"Error: unauthorized");
        assertEquals(expected,actual);
    }
    @Test
    public void joinGamePositiveTest() throws DataAccessException {
        String authToken= UUID.randomUUID().toString();
        authAccess.createAuth(authToken,"username");
        CreateGameResult newGame=service.createGame(new CreateGameRequest("gameName", authToken));
        JoinGameResult actual=service.joinGame(new JoinGameRequest("WHITE",newGame.gameID(),authToken));
        JoinGameResult expected= new JoinGameResult(200,null);
        assertEquals(expected,actual);
    }
    @Test
    public void joinGameNegativeTest() throws DataAccessException {
        String authToken= UUID.randomUUID().toString();
        authAccess.createAuth(authToken,"username");
        String authToken2= UUID.randomUUID().toString();
        authAccess.createAuth(authToken2,"username2");
        CreateGameResult newGame=service.createGame(new CreateGameRequest("gameName", authToken));
        service.joinGame(new JoinGameRequest("WHITE", newGame.gameID(), authToken2));
        JoinGameResult actual=service.joinGame(new JoinGameRequest("WHITE",newGame.gameID(),authToken));
        JoinGameResult expected= new JoinGameResult(403,"Error: already taken");
        assertEquals(expected,actual);

    }
}
