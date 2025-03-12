package dataaccess;
import model.GameData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import chess.ChessGame;

public class SqlGameDAO implements GameDAO{
    public SqlGameDAO() throws DataAccessException,SQLException{
        configureDatabase();
    }

    public int createGame(String gameName) throws DataAccessException{
        try(var connection=DatabaseManager.getConnection()){
            var insertStatement="INSERT INTO game(gameID,whiteUsername,blackUsername,gameName,game) VALUES(?,?,?,?,?)";
            try (var ps=connection.prepareStatement(insertStatement)){
                ps.setInt(1,);
                ps.setString(2,null);
                ps.setString(3,null);
                ps.setString(4,gameName);
                //Serialize this one
                ChessGame newGame= new ChessGame();
                ps.setString(5,newGame);
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
                        var game=rs.getString("game");
                        // deSerialize game
                        ChessGame currentBoard=new ChessGame();
                        // fix this
                        GameData gameInfo=new GameData(gameIDDB,whiteUsername,blackUsername,gameName,currentBoard);
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
                        var gameBoard=rs.getString("game")
                        // deserialize
                        GameData currentGame=new GameData(gameId,whiteUsername,blackUsername,gameName,gameBoard);
                        result.add(currentGame);
                    }
                    return result;
                }
            }
        } catch (DataAccessException | SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public void updateGame(GameData gameData){

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
            whiteUsername VARCHAR(255) NOT NULL,
            blackUsername VARCHAR(255) NOT NULL,
            gameName VARCHAR(255) NOT NULL,
            game VARCHAR(255) NOT NULL
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
