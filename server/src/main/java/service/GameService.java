package service;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.GameData;
import model.AuthData;
import java.util.UUID;
import java.util.Collection;
import chess.ChessGame;

public class GameService {
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;
    public GameService(GameDAO gameAccess, AuthDAO authAccess){
        this.gameAccess=gameAccess;
        this.authAccess=authAccess;
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        String gameName=createGameRequest.gameName();
        String authToken=createGameRequest.authToken();
        try {
            AuthData authorize=authAccess.getAuth(authToken);
            try {
                int gameID=gameAccess.createGame(gameName);
                return new CreateGameResult(gameID, null);
            } catch (DataAccessException ex){
                return new CreateGameResult(null, ex.getMessage());
            }
        } catch (DataAccessException ex){
            return new CreateGameResult(null, ex.getMessage());
        }
    }
    public ListGamesResult listGames(ListGamesRequest listGameRequest){
        String authToken=listGameRequest.authToken();
        try {
           AuthData authorize=authAccess.getAuth(authToken);
           try {
                Collection<GameData> allGames=gameAccess.listGames();
                return new ListGamesResult(allGames,null);
           } catch (DataAccessException ex){
               return new ListGamesResult(null, ex.getMessage());
           }
        } catch (DataAccessException ex){
            return new ListGamesResult(null,ex.getMessage());
        }
    }
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest){
        String authToken=joinGameRequest.authToken();
        int gameID=joinGameRequest.gameID();
        String playerColor=joinGameRequest.playerColor();
        try {
            AuthData authData=authAccess.getAuth(authToken);
            try {
                GameData gameData=gameAccess.getGame(gameID);
                if (isColorAvailable(playerColor,gameData)){
                    GameData updatedColorGame=updateColor(gameData,playerColor,authData.username());
                    try {
                        gameAccess.updateGame(updatedColorGame);
                        return new JoinGameResult(null);
                    } catch (DataAccessException ex){
                        return new JoinGameResult (ex.getMessage());
                    }
                }
            } catch (DataAccessException ex){
                return new JoinGameResult (ex.getMessage());
            }
        } catch (DataAccessException ex){
            return new JoinGameResult (ex.getMessage());
        }
    }

    public boolean isColorAvailable(String playerColor, GameData gameData){
        if (playerColor.equals("WHITE")){
            if (gameData.whiteUsername()==null){
                return true;
            } else{
                return false;
            }
        }
        if (playerColor.equals("BLACK")){
            if (gameData.blackUsername()==null){
                return true;
            } else{
                return false;
            }
        }
    }

    public GameData updateColor(GameData gameData, String playerColor, String username){
        int gameID=gameData.gameID();
        String whiteUsername=gameData.whiteUsername();
        String blackUsername=gameData.blackUsername();
        String gameName=gameData.gameName();
        ChessGame board=gameData.game();
        if (playerColor.equals("WHITE")){
            return new GameData(gameID,username, blackUsername, gameName, board);
        }
        if (playerColor.equals("BLACK")){
            return new GameData(gameID, whiteUsername, username, gameName, board);
        }
    }
}
