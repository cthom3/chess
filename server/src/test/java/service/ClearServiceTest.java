package service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import dataaccess.*;
import service.ClearService;

import java.util.UUID;

public class ClearServiceTest {
    private final UserDAO userAccess=new MemoryUserDAO();
    private final GameDAO gameAccess=new MemoryGameDAO();
    private final AuthDAO authAccess=new MemoryAuthDAO();
    final ClearService service = new ClearService(userAccess,authAccess,gameAccess);

    @BeforeEach
    void clear() throws DataAccessException{
        userAccess.clear();
        gameAccess.clear();
        authAccess.clear();
    }

    @Test
    public void clearPositiveTest() throws DataAccessException{
        userAccess.createUser("username","password", "username@email.com");
        String authToken= UUID.randomUUID().toString();
        authAccess.createAuth(authToken,"username");
        gameAccess.createGame("gameName");
        ClearResult actual=service.clear(new ClearRequest());
        ClearResult expected=new ClearResult(200,null);
        assertEquals(expected,actual);

    }
}
