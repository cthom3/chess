package dataaccess;
import model.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public class SqlUserDAOTest {
    private final UserDAO userAccess=new SqlUserDAO ();

    public SqlUserDAOTest() throws SQLException, DataAccessException {
    }

    @BeforeEach
    void clearAll() throws DataAccessException{
        userAccess.clear();
    }

    @Test
    public void createUserPositiveTest() throws DataAccessException {
        UserData expectedUser=new UserData("username","password","username@email.com");
        userAccess.createUser("username","password","username@email.com");
        UserData actualUser=userAccess.getUser("username");
        assertEquals(expectedUser,actualUser);
    }

    @Test
    public void createUserNegativeTest() throws DataAccessException {
        userAccess.createUser("username",null,"username@email.com");
        try {
            userAccess.createUser("username",null,"username@email.com");
            DataAccessException actual=null;
            assertNotNull(actual);
        } catch (DataAccessException e){
            DataAccessException actual=new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void getUserPositiveTest() throws DataAccessException{
        UserData expectedUser=new UserData("username","password","username@email.com");
        userAccess.createUser("username","password","username@email.com");
        UserData actualUser=userAccess.getUser("username");
        assertEquals(expectedUser,actualUser);
    }

    @Test
    public void getUserNegativeTest() throws DataAccessException{
        try {
            userAccess.getUser("username");
            DataAccessException actual=null;
            assertNotNull(actual);
        } catch (DataAccessException e){
            DataAccessException actual=new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void clear() throws DataAccessException{
        UserData expectedUser=new UserData("username","password","username@email.com");
        userAccess.createUser("username","password","username@email.com");
        UserData actualUser=userAccess.getUser("username");
        userAccess.clear();

        assertEquals(expectedUser,actualUser);
    }
}
