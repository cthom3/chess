package ui;


import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.userrecords.*;
import service.gamerecords.*;
import service.clearrecords.*;

import java.net.*;
import java.io.*;
import java.util.Objects;


public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url){
        serverUrl = url;
    }
    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        var path="/user";
        return this.makeRequest("POST", path, request, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        var path="/session";
        return this.makeRequest("POST", path, request, LoginResult.class, null);

    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException {
        var path="/session";
        return this.makeRequest("DELETE", path, request, LogoutResult.class, request.authToken());
    }

    public CreateGameResult createGame (CreateGameRequest request) throws DataAccessException {
        var path="/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class, request.authToken());
    }

    public JoinGameResult joinGame (JoinGameRequest request) throws DataAccessException {
        var path="/game";
        return this.makeRequest("PUT", path, request, JoinGameResult.class, request.authToken());
    }

    public ListGamesResult listGames (ListGamesRequest listRequest) throws DataAccessException {
        var path="/game";
        return this.makeRequest("GET", path, listRequest, ListGamesResult.class, listRequest.authToken());
    }

    public ClearResult clear (ClearRequest request) throws DataAccessException {
        var path="/db";
        return this.makeRequest("DELETE", path, request, ClearResult.class, null);
    }

    private <T> T makeRequest(String method, String path,Object request, Class<T> responseClass, String authToken) throws DataAccessException{
        try {
            URI uri=(new URI(serverUrl+path));
            HttpURLConnection http=(HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            if (!Objects.equals(method, "GET")){
                http.setDoOutput(true);
            }
            writeRequestBody(path,request,http, authToken, method);
            http.connect();
            return readRequestBody(http,responseClass);
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
    private static void writeRequestBody(String path, Object request, HttpURLConnection http, String authToken, String method) throws IOException {
        if (request !=null){
            http.addRequestProperty("Content-Type", "application/json");
            if (!Objects.equals(path, "/user")){
                http.addRequestProperty("Authorization", authToken);
            }
            String requestData=new Gson().toJson(request);
            if (!Objects.equals(method,"GET")){
                try (OutputStream requestBody=http.getOutputStream()){
                    requestBody.write(requestData.getBytes());
                }
            }
        }
    }

    private static <T> T readRequestBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getResponseCode()==200){
            InputStream responseBody=http.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            if (responseClass != null){
                response= new Gson().fromJson(reader,responseClass);
            }
        } else {
            InputStream responseBody=http.getErrorStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            if (responseClass != null){
                response= new Gson().fromJson(reader,responseClass);
            }

        }
        return response;
    }

}
