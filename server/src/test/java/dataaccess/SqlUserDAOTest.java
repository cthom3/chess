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


    @Test
    public void createUserPositiveTest() {
        UserData expectedUser=new UserData("username","password","username@email.com");
        SqlUserDAO.createUser("username","password","username@email.com");
    }

    @Test
    public void createUserNegativeTest(){

    }

    @Test
    public void getUserPositiveTest(){

    }

    @Test
    public void getUserNegativeTest(){

    }

    @Test
    public void clear(){

    }
}
