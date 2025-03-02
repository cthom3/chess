package service;
import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import service.UserService;
import model.UserData;
import model.AuthData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class UserServiceTest {
    private final UserDAO userAccess=new MemoryUserDAO();
    private final AuthDAO authAccess=new MemoryAuthDAO();
    final UserService service = new UserService(userAccess, authAccess);

    @BeforeEach
    void clear() throws DataAccessException{
        userAccess.clear();
        authAccess.clear();
    }
    @Test
    public void registerPositiveTest() throws DataAccessException{
        UserData expectedUser=new UserData("username", "password", "username@email.com");
        service.register(new RegisterRequest("username", "password", "username@email.com"));
        UserData actualUser=userAccess.getUser("username");
        assertEquals(expectedUser,actualUser);
    }
    @Test
    public void registerNegativeTest() throws DataAccessException{
        service.register(new RegisterRequest("username", "password", "username@email.com"));
        // try to register with an already existing username
        RegisterResult actual=service.register(new RegisterRequest("username", "password", "username@email.com"));
        RegisterResult expected=new RegisterResult(null,null,"Error: already taken");
        assertEquals(expected,actual);
    }
    @Test
    public void loginPositiveTest() throws DataAccessException{
        service.register(new RegisterRequest("username", "password", "username@email.com"));
        LoginResult actual=service.login(new LoginRequest("username","password"));
        LoginResult expected=new LoginResult("username", actual.authToken(), null);
        assertEquals(expected,actual);
        assertNotNull(actual.authToken());
    }
    @Test
    public void loginNegativeTest() throws DataAccessException{

    }
    @Test
    public void logoutPositiveTest() throws DataAccessException{

    }
    @Test
    public void logoutNegativeTest() throws DataAccessException{

    }
}
