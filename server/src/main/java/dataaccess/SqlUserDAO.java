package dataaccess;
import model.UserData;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlUserDAO implements UserDAO {
    public SqlUserDAO() throws DataAccessException,SQLException {
        configureDatabase();
    }
    public void createUser(String username, String password, String email) throws DataAccessException {
        try (var connection=DatabaseManager.getConnection()){
            var statement="INSERT INTO user (username,password,email) VALUES (?,?,?)";
            try (var ps=connection.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser (String username) throws DataAccessException{
        try (var connection =DatabaseManager.getConnection()){
            var command="SELECT * FROM user WHERE username=?";
            try (var ps= connection.prepareStatement(command)) {
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
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public void clear() throws DataAccessException{
        try (var connection=DatabaseManager.getConnection()){
            var statement="TRUNCATE user";
            try (var ps=connection.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e){
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
