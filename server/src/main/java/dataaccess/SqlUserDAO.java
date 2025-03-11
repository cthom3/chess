package dataaccess;
import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlUserDAO implements UserDAO {
    public SqlUserDAO() {

    }
    public void createUser(String username, String password, String email) throws DataAccessException,SQLException {
        try (var conn=DatabaseManager.getConnection()){
            var statement="INSERT INTO user(username,password,email) VALUES (?,?,?)";
            var id=executeUpdate(statement,username,password,email);
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser (String username) throws DataAccessException,SQLException{
        try (var conn =DatabaseManager.getConnection()){
            var statement="SELECT id, json FROM user WHERE id=?";
            try (var ps= conn.prepareStatement(statement)) {
                ps.setString(1,username);
                try (var rs=ps.executeQuery()){
                    if (rs.next()){
                        return readUser(rs));
                    }
                }
            } catch (SQLException e){
                throw new DataAccessException(e.getMessage());
            }
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() {

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
}
