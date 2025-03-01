package service;
import dataaccess.GameDAO;

public class GameService {
    private final GameDAO dataAccess;
    public GameService(GameDAO dataAccess){
        this.dataAccess=dataAccess;
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        String gameName=createGameRequest.gameName();
        if (gameName==null){
        }
        return dataAccess.createGame(gameName);
    }
    public ListGamesResult listGames(ListGamesRequest listGameRequest){}
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest){}
}
