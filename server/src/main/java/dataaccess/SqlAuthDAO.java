package dataaccess;
import model.AuthData;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO{
    public SqlAuthDAO() {
        //
    }
    public void createAuth(String authToken, String username) throws DataAccessException, SQLException {
        try(var conn=DatabaseManager.getConnection()){
            var insertStatement="INSERT INTO auth (username,authToken) VALUES (?,?)";
            var id=executeUpdate(insertStatement,authToken,username);
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException,SQLException{
        try(var conn=DatabaseManager.getConnection()){
           var statement="SELECT * FROM auth WHERE id=?";
           try (var ps=conn.prepareStatement(statement)){
               ps.setString(1,authToken);
               ps.setString(2,authToken);
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
    }

    public void deleteAuth(String authToken) throws DataAccessException,SQLException{
        try(var conn=DatabaseManager.getConnection()){
            var statement="DELETE FROM auth WHERE authToken=?";
            executeUpdate(statement,authToken);
        } catch (DataAccessException | SQLException e){

    }

    public void clear() throws DataAccessException {
            var statement="TRUNCATE auth";
            executeUpdate(statement);
        }

    }

    private final String[] createStatements={
            """
            CREATE TABLE IF NOT EXISTS auth(
            authToken 
            username VARCHAR(255) NOT NULL
            )
            """
    };

    private void configureDatabase() throws DataAccessException,SQLException{
        DatabaseManager.createDatabase(); //do I need this line of code? don't I just want to initialize this once
        try (var conn=DatabaseManager.getConnection()){
            for (var statement:createStatements){

            }
        }
    }
}
