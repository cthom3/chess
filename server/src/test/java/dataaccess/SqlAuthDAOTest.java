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
        authAccess.createAuth("authToken","username");
        try {
            authAccess.createAuth("authToken","username");
        } catch (DataAccessException e){
            DataAccessException actual= new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void getAuthPositiveTest() throws DataAccessException{
        AuthData expectedAuth=new AuthData("authToken","username");
        authAccess.createAuth("authToken","username");
        AuthData actualAuth=authAccess.getAuth("authToken");
        assertEquals(expectedAuth,actualAuth);
    }

    @Test
    public void getAuthNegativeTest() throws DataAccessException{
        try{
            authAccess.getAuth("authToken");
        } catch (DataAccessException e){
            DataAccessException actual=new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void deleteAuthPositiveTest() throws DataAccessException{
        authAccess.createAuth("authToken","username");
        authAccess.getAuth("authToken");
        authAccess.deleteAuth("authToken");
        try {
            authAccess.getAuth("authToken");
        } catch (DataAccessException e){
            DataAccessException actual= new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void deleteAuthNegativeTest() throws DataAccessException{
        try {
            authAccess.deleteAuth("authToken");
        } catch (DataAccessException e){
            DataAccessException actual= new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }
    }

    @Test
    public void clearTest() throws DataAccessException{
        authAccess.createAuth("authToken","username");
        authAccess.clear();
        try {
            authAccess.getAuth("authToken");
        } catch (DataAccessException e){
            DataAccessException actual= new DataAccessException(e.getMessage());
            assertNotNull(actual);
        }

    }
}
