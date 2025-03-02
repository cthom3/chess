package dataaccess;
import model.AuthData;

public interface AuthDAO {
    public void createAuth(String authToken, String username) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;
}
