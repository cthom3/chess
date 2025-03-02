package service;
import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import model.UserData;

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
        service.register(new RegisterRequest("username", "password", "username@email.com"));
        LoginResult actual=service.login(new LoginRequest("username","password1"));
        LoginResult expected=new LoginResult(null,null, "Error: unauthorized");
        assertEquals(expected,actual);
    }
    @Test
    public void logoutPositiveTest() throws DataAccessException{
        service.register(new RegisterRequest("username", "password", "username@email.com"));
        LoginResult loggedIn=service.login(new LoginRequest("username","password"));
        LogoutResult actual=service.logout(new LogoutRequest(loggedIn.authToken()));
        LogoutResult expected=new LogoutResult(null);
        assertEquals(expected,actual);
        assertNull(authAccess.getAuth(loggedIn.authToken()));
    }
    @Test
    public void logoutNegativeTest() throws DataAccessException{
        service.register(new RegisterRequest("username", "password", "username@email.com"));
        LoginResult loggedIn=service.login(new LoginRequest("username","password"));
        LogoutResult actual=service.logout(new LogoutRequest(null));
        LogoutResult expected=new LogoutResult("Error: unauthorized");
        assertEquals(expected,actual);
        assertNotNull(authAccess.getAuth(loggedIn.authToken()));
    }
}
