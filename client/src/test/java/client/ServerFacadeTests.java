package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import service.clearrecords.*;
import service.gamerecords.*;
import service.userrecords.*;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(1786);
        System.out.println("Started test HTTP server on " + port);
        facade=new ServerFacade("http://localhost:"+String.valueOf(port));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearAll() throws DataAccessException {
        ClearRequest request=new ClearRequest();
        facade.clear(request);
    }


    @Test
    void registerPositive() throws Exception{
        RegisterResult expected=facade.register(new RegisterRequest("username","password","username@email.com"));
        assertTrue(expected.statusCode()==200);
//        assertTrue(expected.authToken().length() >10);
    }

    @Test
    void registerNegative() throws Exception{
        facade.register(new RegisterRequest("username","password","username@email.com"));
        RegisterResult expected=facade.register(new RegisterRequest("username", "password", "username@email.com"));
        assertTrue(expected.statusCode()!=200);
    }

    @Test
    void loginPositive() throws Exception {
        facade.register(new RegisterRequest("username","password","username@email.com"));
        LoginResult expected=facade.login(new LoginRequest("username", "password"));
        assertTrue(expected.authToken().length()>10);
    }

    @Test
    void loginNegative() throws Exception {
        LoginResult expected=facade.login(new LoginRequest("username", "password"));
        assertTrue(expected.statusCode() !=200);
    }

    @Test
    void logoutPositive() throws Exception {
        facade.register(new RegisterRequest("username","password","username@email.com"));
        LoginResult loggedin=facade.login(new LoginRequest("username", "password"));
        LogoutResult actual=facade.logout(new LogoutRequest(loggedin.authToken()));
        assertTrue(actual.statusCode() == 200);
    }

    @Test
    void logoutNegative() throws Exception {
        LogoutResult actual=facade.logout(new LogoutRequest("2000"));
        assertTrue(actual.statusCode() != 200);
    }

    @Test
    void createGamePositive() throws Exception {
        facade.register(new RegisterRequest("username","password","username@email.com"));
        LoginResult loggedin=facade.login(new LoginRequest("username", "password"));
        CreateGameResult actual=facade.createGame(new CreateGameRequest("game1", loggedin.authToken()));
        assertNotNull(actual.gameID());
    }

    @Test
    void createGameNegative() throws Exception {
        CreateGameResult actual=facade.createGame(new CreateGameRequest("game1", "32393"));
        assertTrue(actual.statusCode()!=200);
    }

    @Test
    void joinGamePositive() throws Exception {
        facade.register(new RegisterRequest("username","password","username@email.com"));
        LoginResult loggedin=facade.login(new LoginRequest("username", "password"));
        CreateGameResult newGame=facade.createGame(new CreateGameRequest("game1", loggedin.authToken()));
        JoinGameResult joinedGame=facade.joinGame(new JoinGameRequest("BLACK", newGame.gameID(),loggedin.authToken()));
        assertTrue(joinedGame.statusCode()==200);
    }

    @Test
    void joinGameNegative() throws Exception {
        JoinGameResult joinedGame=facade.joinGame(new JoinGameRequest("BLACK", 3,"34234"));
        assertTrue(joinedGame.statusCode()!=200);
    }

    @Test
    void listGamesPositive() throws Exception {
        facade.register(new RegisterRequest("username","password","username@email.com"));
        LoginResult loggedin=facade.login(new LoginRequest("username", "password"));
        facade.createGame(new CreateGameRequest("game1", loggedin.authToken()));
        facade.createGame(new CreateGameRequest("game2", loggedin.authToken()));
        ListGamesResult listedGames=facade.listGames(new ListGamesRequest(loggedin.authToken()));
        assertTrue(listedGames.statusCode()==200);
    }

    @Test
    void listGamesNegative() throws Exception {
        ListGamesResult listedGames=facade.listGames(new ListGamesRequest("234234"));
        assertTrue(listedGames.statusCode()!=200);
    }

    @Test
    void clearPositive() throws Exception {
        facade.register(new RegisterRequest("username","password","username@email.com"));
        LoginResult loggedin=facade.login(new LoginRequest("username", "password"));
        CreateGameResult newGame=facade.createGame(new CreateGameRequest("game1", loggedin.authToken()));
        JoinGameResult joinedGame=facade.joinGame(new JoinGameRequest("BLACK", newGame.gameID(),loggedin.authToken()));
        ClearResult cleared=facade.clear(new ClearRequest());
        assertTrue(cleared.statusCode()==200);
    }


}
