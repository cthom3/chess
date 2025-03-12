package dataaccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.sql.SQLException;

public class SqlAuthDAOTest {
    private final AuthDAO authAccess=new SqlAuthDAO();

    public SqlAuthDAOTest() throws SQLException,DataAccessException{
    }

    @BeforeEach
    void clearAll() throws DataAccessException{
        authAccess.clear();
    }

    @Test
    public void createAuthPositiveTest () throws DataAccessException{
        AuthData expectedAuth=new AuthData("authToken","username");
        authAccess.createAuth("authToken","username");
        AuthData actualAuth=authAccess.getAuth("authToken");
        assertEquals(expectedAuth,actualAuth);
    }

    @Test
    public void createAuthNegativeTest() throws DataAccessException{

    }

    @Test
    public void getAuthPositiveTest(){

    }

    @Test
    public void getAuthNegativeTest(){

    }

    @Test
    public void deleteAuthPositiveTest(){

    }

    @Test
    public void deleteAuthNegativeTest(){

    }
}
