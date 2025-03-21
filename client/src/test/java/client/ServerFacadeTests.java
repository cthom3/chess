package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import service.clearrecords.ClearRequest;
import service.userrecords.*;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade=new ServerFacade(String.valueOf(port));
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
        var authData=facade.register(new RegisterRequest("username","password","username@email.com"));
        assertTrue(authData.authToken().length() <10);
    }

    @Test
    void registerNegative() throws Exception{
        facade.register(new RegisterRequest("username","password","username@email.com"));
        try {
            facade.register(new RegisterRequest("username", "password", "username@email.com"));
            assertTrue(false);
        } catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void loginPositive() throws Exception {
        facade.register(new RegisterRequest("username","password","username@email.com"));
        facade.login(new LoginRequest("username", "password"));

    }


}
