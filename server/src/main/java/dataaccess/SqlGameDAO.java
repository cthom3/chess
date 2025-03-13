package dataaccess;
import com.google.gson.Gson;
import model.GameData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import chess.ChessGame;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlGameDAO implements GameDAO{
    public SqlGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException|SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public int createGame(String gameName) throws DataAccessException{
        try(var connection=DatabaseManager.getConnection()){
            var insertStatement="INSERT INTO game(whiteUsername,blackUsername,gameName,game) VALUES(?,?,?,?)";
            try (var ps=connection.prepareStatement(insertStatement, RETURN_GENERATED_KEYS)){
                ps.setString(1,null);
                ps.setString(2,null);
                ps.setString(3,gameName);
                ChessGame newGame= new ChessGame();
                String gameString=new Gson().toJson(newGame,ChessGame.class);
                ps.setString(4,gameString);
                ps.executeUpdate();
                var resultSet=ps.getGeneratedKeys();
                var iD=0;
                if (resultSet.next()){
                    iD=resultSet.getInt(1);
                }
                return iD;
            }
        }catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{
        try(var connection=DatabaseManager.getConnection()){
            var statement="SELECT gameID,whiteUsername,blackUsername,gameName,game FROM game WHERE gameID=?";
            try (var ps=connection.prepareStatement(statement)){
                ps.setInt(1,gameID);
                try (var rs=ps.executeQuery()){
                    if (rs.next()){
                        var gameIDDB=rs.getInt("gameID");
                        var whiteUsername=rs.getString("whiteUsername");
                        var blackUsername=rs.getString("blackUsername");
                        var gameName= rs.getString("gameName");
                        var gameString=rs.getString("game");
                        ChessGame gameBoard=new Gson().fromJson(gameString,ChessGame.class);
                        GameData gameInfo=new GameData(gameIDDB,whiteUsername,blackUsername,gameName,gameBoard);
                        return gameInfo;
                    }
                }
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public Collection<GameData> listGames() throws DataAccessException{
        var result = new ArrayList<GameData>();
        try (var connection=DatabaseManager.getConnection()){
            var statement="SELECT * FROM game";
            try (var ps=connection.prepareStatement(statement)){
                try (var rs=ps.executeQuery()){
                    while (rs.next()){
                        var gameId=rs.getInt("gameID");
                        var whiteUsername=rs.getString("whiteUsername");
                        var blackUsername=rs.getString("blackUsername");
                        var gameName=rs.getString("gameName");
                        var gameString=rs.getString("game");
                        ChessGame gameBoard=new Gson().fromJson(gameString,ChessGame.class);
                        GameData currentGame=new GameData(gameId,whiteUsername,blackUsername,gameName,gameBoard);
                        result.add(currentGame);
                    }
                    return result;
                }
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(GameData gameData) throws DataAccessException{
        try (var connection=DatabaseManager.getConnection()){
            var statement="UPDATE game SET whiteUsername=?,blackUsername=?,game=? WHERE gameID=?";
            try (var ps=connection.prepareStatement(statement)){
                ps.setString(1, gameData.whiteUsername());
                ps.setString(2,gameData.blackUsername());
                String gameString=new Gson().toJson(gameData.game(),ChessGame.class);
                ps.setString(3,gameString);
                ps.setInt(4,gameData.gameID());
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear () throws DataAccessException{
        try (var connection=DatabaseManager.getConnection()){
            var statement="TRUNCATE game";
            try (var ps=connection.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements={
            """
            CREATE TABLE IF NOT EXISTS game(
            gameID INT NOT NULL AUTO_INCREMENT,
            whiteUsername VARCHAR(255),
            blackUsername VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            game TEXT NOT NULL,
            PRIMARY KEY(gameID)
            )
            """
    };

    private void configureDatabase() throws DataAccessException,SQLException{
        DatabaseManager.createDatabase();
        try (var connection=DatabaseManager.getConnection()){
            for (var statement:createStatements){
                try(var preparedStatement=connection.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

}
