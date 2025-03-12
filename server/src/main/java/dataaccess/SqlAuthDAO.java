package dataaccess;
import model.AuthData;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlAuthDAO implements AuthDAO{
    public SqlAuthDAO() throws DataAccessException,SQLException{
        configureDatabase();
    }
    public void createAuth(String authToken, String username) throws DataAccessException {
        try(var connection=DatabaseManager.getConnection()){
            var insertStatement="INSERT INTO auth (username,authToken) VALUES (?,?)";
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
           var statement="SELECT * FROM auth WHERE id=?";
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

    private final String[] createStatements={
            """
            CREATE TABLE IF NOT EXISTS auth(
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL
            )
            """
    };

    private void configureDatabase() throws DataAccessException,SQLException{
        DatabaseManager.createDatabase(); //do I need this line of code? don't I just want to initialize this once
        try (var conn=DatabaseManager.getConnection()){
            for (var statement:createStatements){
                try(var preparedStatement=conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
