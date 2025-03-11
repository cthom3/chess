package dataaccess;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlUserDAO implements UserDAO {
    public SqlUserDAO() throws DataAccessException,SQLException {
        configureDatabase();
    }
    public void createUser(String username, String password, String email) throws DataAccessException,SQLException {
        try (var conn=DatabaseManager.getConnection()){
            var statement="INSERT INTO user(username,password,email) VALUES (?,?,?)";
            var id=executeUpdate(statement,username,password,email);
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser (String username) throws DataAccessException, SQLException{
        try (var conn =DatabaseManager.getConnection()){
            var statement="SELECT id, json FROM user WHERE id=?";
            try (var ps= conn.prepareStatement(statement)) {
                ps.setString(1,username);
                try (var rs=ps.executeQuery()){
                    if (rs.next()){
                        var usernameDB=rs.getString("username");
                        var password=rs.getString("password");
                        var email=rs.getString("email");
                        UserData userInfo=new UserData(usernameDB,password,email);
                        return userInfo;
                    }
                }
            } catch (SQLException e){
                throw new DataAccessException(e.getMessage());
            }
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException,SQLException{
        var statement="TRUNCATE user";
        executeUpdate(statement);
    }


    private int executeUpdate(String statement, Object... params) throws DataAccessException,SQLException{
        try (var conn=DatabaseManager.getConnection()){
            try (var ps=conn.prepareStatement(statement,RETURN_GENERATED_KEYS)){
                for (var i=0; i<params.length; i++){
                    var param=params[i];
                    if (param instanceof String p){
                        ps.setString(i+1,p);
                    } else if (param instanceof Integer p){
                        ps.setInt(i+1,p);
                    } else if (param instanceof UserData p){
                        ps.setString(i+1,p.toString());
                    } else if (param==null){
                        ps.setNull(i+1,NULL);
                    }
                }
                ps.executeUpdate();
                var rs=ps.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }
                return 0;
            }catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements={
            """
            CREATE TABLE IF NOT EXISTS user(
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL
            )
            """
    };

    private void configureDatabase() throws DataAccessException,SQLException {
        DatabaseManager.createDatabase();
        try (var conn=DatabaseManager.getConnection()){
            for (var statement:createStatements){
                try (var preparedStatement= conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }
}
