package dataaccess;
import model.AuthData;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlAuthDAO implements AuthDAO{
    public SqlAuthDAO() {
        try {
            String[] createStatements={
                    """
            CREATE TABLE IF NOT EXISTS auth(
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL
            )
            """
            };
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException|SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public void createAuth(String authToken, String username) throws DataAccessException {
        try(var connection=DatabaseManager.getConnection()){
            var insertStatement="INSERT INTO auth (authToken,username) VALUES (?,?)";
            try (var ps=connection.prepareStatement(insertStatement)) {
                ps.setString(1, authToken);
                ps.setString(2, username);
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        try(var connection=DatabaseManager.getConnection()){
           var statement="SELECT authToken,username FROM auth WHERE authToken=?";
           try (var ps=connection.prepareStatement(statement)){
               ps.setString(1,authToken);
               try(var rs=ps.executeQuery()){
                   if (rs.next()){
                       var authTokenDB=rs.getString("authToken");
                       var username=rs.getString("username");
                       AuthData authInfo=new AuthData(authTokenDB,username);
                       return authInfo;
                   }
               }
           }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        try(var connection=DatabaseManager.getConnection()){
            var statement="DELETE FROM auth WHERE authToken=?";
            try (var ps=connection.prepareStatement(statement)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    public void clear() throws DataAccessException {
        try (var connection=DatabaseManager.getConnection()){
            var statement="TRUNCATE auth";
            try (var ps=connection.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
