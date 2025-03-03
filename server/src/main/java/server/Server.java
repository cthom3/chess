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
//        res.status(ex.code());
//        res.body(ex.message());
//    }

    private Object register(Request req, Response res) throws DataAccessException{
        RegisterRequest user= new Gson().fromJson(req.body(),RegisterRequest.class);
        RegisterResult newUser=userService.register(user);
        return new Gson().toJson(newUser);
    }

    private Object login (Request req, Response res) throws DataAccessException{
        LoginRequest loginData=new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult newLogin=userService.login(loginData);
        return new Gson().toJson(newLogin);
    }

    private Object logout (Request req, Response res) throws DataAccessException{
        LogoutRequest logoutData=new Gson().fromJson(req.body(), LogoutRequest.class);
        LogoutResult newLogout=userService.logout(logoutData);
        return new Gson().toJson(newLogout);
    }

    private Object createGame (Request req, Response res) throws DataAccessException{
        CreateGameRequest gameData=new Gson().fromJson(req.body(), CreateGameRequest.class);
        CreateGameResult newGame=gameService.createGame(gameData);
        return new Gson().toJson(newGame);
    }

    private Object listGames (Request req, Response res) throws DataAccessException{
        ListGamesRequest gameData=new Gson().fromJson(req.body(), ListGamesRequest.class);
        ListGamesResult allGames=gameService.listGames(gameData);
        return new Gson().toJson(allGames);
    }

    private Object joinGame (Request req, Response res) throws DataAccessException{
        JoinGameRequest gameData=new Gson().fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult updatedGame=gameService.joinGame(gameData);
        return new Gson().toJson(updatedGame);
    }

    private Object clear (Request req, Response res) throws DataAccessException{
        ClearRequest clearAll=new Gson().fromJson(req.body(), ClearRequest.class);
        ClearResult allCleared=clearService.clear(clearAll);
        return new Gson().toJson(allCleared);
    }


}
