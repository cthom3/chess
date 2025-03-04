package service;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.GameData;
import model.AuthData;

import java.util.Collection;
import chess.ChessGame;
import service.gamerecords.*;

public class GameService {
    private final GameDAO gameAccess;
    private final AuthDAO authAccess;
    public GameService(GameDAO gameAccess, AuthDAO authAccess){
        this.gameAccess=gameAccess;
        this.authAccess=authAccess;
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        String gameName = createGameRequest.gameName();
        String authToken = createGameRequest.authToken();
        try {
            AuthData authorize = authAccess.getAuth(authToken);
            if (authorize != null) {
                try {
                    int gameID = gameAccess.createGame(gameName);
                    return new CreateGameResult(200, gameID, null);
                } catch (DataAccessException ex) {
                    return new CreateGameResult(500, null, ex.getMessage());
                }
            } else {
                return new CreateGameResult(401, null, "Error: unauthorized");
            }
        } catch (DataAccessException ex) {
            return new CreateGameResult(401, null, "Error: unauthorized");
        }
    }
    public ListGamesResult listGames(ListGamesRequest listGameRequest){
        String authToken = listGameRequest.authToken();
        try {
            AuthData authorize = authAccess.getAuth(authToken);
            if (authorize != null) {
                try {
                    Collection<GameData> allGames = gameAccess.listGames();
                    return new ListGamesResult(200, allGames, null);
                } catch (DataAccessException ex) {
                    return new ListGamesResult(500, null, ex.getMessage());
                }
            } else {
                return new ListGamesResult(401, null, "Error: unauthorized");
            }
        } catch (DataAccessException ex) {
            return new ListGamesResult(401, null, "Error: unauthorized");
        }
    }
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest){
        String authToken=joinGameRequest.authToken();
        int gameID=joinGameRequest.gameID();
        String playerColor=joinGameRequest.playerColor();
        if (playerColor!=null && authToken!=null) {
            try {
                AuthData authData = authAccess.getAuth(authToken);
                GameData gameData = gameAccess.getGame(gameID);
                return checkGame(playerColor,gameData,authData);
            } catch (DataAccessException ex) {
                return new JoinGameResult(401, "Error: unauthorized");
            }
        } else {
            return new JoinGameResult(400, "Error: bad request");
        }
    }

    private JoinGameResult checkGame (String playerColor, GameData gameData, AuthData authData){
        if (authData!=null) {
            if (gameData != null && (playerColor.equals("WHITE") || playerColor.equals("BLACK"))) {
                if (isColorAvailable(playerColor, gameData)) {
                    GameData updatedColorGame = updateColor(gameData, playerColor, authData.username());
                    try {
                        gameAccess.updateGame(updatedColorGame);
                        return new JoinGameResult(200, null);
                    } catch (DataAccessException ex) {
                        return new JoinGameResult(500, ex.getMessage());
                    }
                } else {
                    return new JoinGameResult(403, "Error: already taken");
                }
            } else {
                return new JoinGameResult(400, "Error: bad request");
            }
        } else {
            return new JoinGameResult(401,"Error: unauthorized");
        }
    }

    private boolean isColorAvailable(String playerColor, GameData gameData){
        if (playerColor.equals("WHITE")){
            if (gameData.whiteUsername()==null){
                return true;
            }
        }
        if (playerColor.equals("BLACK")){
            if (gameData.blackUsername()==null){
                return true;
            }
        }
        return false;
    }

    private GameData updateColor(GameData gameData, String playerColor, String username){
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
        return gameData;
    }
}
