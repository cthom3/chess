package service;
import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import service.UserService;
import model.UserData;
import model.AuthData;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;

public class UserServiceTest {
    private final UserDAO userAccess=new MemoryUserDAO();
    private final AuthDAO authAccess=new MemoryAuthDAO();
    final UserService service = new UserService(userAccess, authAccess);
    @Test
    public void registerPositiveTest() throws DataAccessException{
        UserData expectedUser=new UserData("username", "password", "username@email.com");
        service.register(new RegisterRequest("username", "password", "username@email.com"));
        UserData actualUser=userAccess.getUser("username");
        assertEquals(expectedUser,actualUser);
    }
    @Test
    public void registerNegativeTest() throws DataAccessException{

    }
    @Test
    public void loginPositiveTest() throws DataAccessException{

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
