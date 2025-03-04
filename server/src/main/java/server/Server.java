package server;
import com.google.gson.Gson;

import dataaccess.DataAccessException;
import dataaccess.*;
import service.*;
import spark.*;

public class Server {
    private final UserDAO userDAO=new MemoryUserDAO();
    private final GameDAO gameDAO=new MemoryGameDAO();
    private final AuthDAO authDAO= new MemoryAuthDAO();
    private final UserService userService=new UserService(userDAO,authDAO);
    private final GameService gameService=new GameService(gameDAO,authDAO);
    private final ClearService clearService= new ClearService(userDAO,authDAO,gameDAO);

    public Server (){}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user",this::register);
        Spark.post("/session",this::login);
        Spark.delete("/session",this::logout);
        Spark.post("/game",this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game",this::joinGame);
        Spark.delete("/db",this::clear);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//    private void exceptionHandler(DataAccessException ex, Request req, Response res){
//        res.status(ex.StatusCode());
//        res.body(ex.toJson());
//    }

    private Object register(Request req, Response res) throws DataAccessException{
        RegisterRequest user= new Gson().fromJson(req.body(),RegisterRequest.class);
        RegisterResult newUser=userService.register(user);
        res.status(newUser.statusCode());
        return new Gson().toJson(newUser);
    }

    private Object login (Request req, Response res) throws DataAccessException{
        LoginRequest loginData=new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult newLogin=userService.login(loginData);
        res.status(newLogin.statusCode());
        return new Gson().toJson(newLogin);
    }

    private Object logout (Request req, Response res) throws DataAccessException{
        LogoutRequest logoutData=new LogoutRequest (req.headers("Authorization"));
        LogoutResult newLogout=userService.logout(logoutData);
        res.status(newLogout.statusCode());
        return new Gson().toJson(newLogout);
    }

    private Object createGame (Request req, Response res) throws DataAccessException{
        CreateGameRequest bodyData=new Gson().fromJson(req.body(),CreateGameRequest.class);
        CreateGameRequest gameData=new CreateGameRequest(bodyData.gameName(),req.headers("Authorization"));
        CreateGameResult newGame=gameService.createGame(gameData);
        res.status(newGame.statusCode());
        return new Gson().toJson(newGame);
    }

    private Object listGames (Request req, Response res) throws DataAccessException{
        ListGamesRequest gameData=new ListGamesRequest(req.headers("Authorization"));
        ListGamesResult allGames=gameService.listGames(gameData);
        res.status(allGames.statusCode());
        return new Gson().toJson(allGames);
    }

    private Object joinGame (Request req, Response res) throws DataAccessException{
        JoinGameRequest bodyData=new Gson().fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest gameData=new JoinGameRequest(bodyData.playerColor(),bodyData.gameID(),req.headers("Authorization"));
        JoinGameResult updatedGame=gameService.joinGame(gameData);
        res.status(updatedGame.statusCode());
        return new Gson().toJson(updatedGame);
    }

    private Object clear (Request req, Response res) throws DataAccessException{
        ClearRequest clearAll=new Gson().fromJson(req.body(), ClearRequest.class);
        ClearResult allCleared=clearService.clear(clearAll);
        res.status(allCleared.statusCode());
        return new Gson().toJson(allCleared);
    }


}
