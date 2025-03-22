package ui;


import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.userrecords.*;
import service.gamerecords.*;
import service.clearrecords.*;

import java.net.*;
import java.io.*;


public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url){
        serverUrl = url;
    }
    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        var path="/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        var path="/session";
        return this.makeRequest("POST", path, request, LoginResult.class);

    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException {
        var path="/session";
        return this.makeRequest("DELETE", path, request, LogoutResult.class);
    }

    public CreateGameResult createGame (CreateGameRequest request) throws DataAccessException {
        var path="/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public JoinGameResult joinGame (JoinGameRequest request) throws DataAccessException {
        var path="/game";
        return this.makeRequest("PUT", path, request, JoinGameResult.class);
    }

    public ListGamesResult listGames (ListGamesRequest request) throws DataAccessException {
        var path="/game";
        return this.makeRequest("GET", path, request, ListGamesResult.class);
    }

    public ClearResult clear (ClearRequest request) throws DataAccessException {
        var path="/db";
        return this.makeRequest("DELETE", path, request, ClearResult.class);
    }

    private <T> T makeRequest(String method, String path,Object request, Class<T> responseClass) throws DataAccessException{
        try {
            URI uri=(new URI(serverUrl+path));
            HttpURLConnection http=(HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeRequestBody(request,http);
            http.connect();
            return readRequestBody(http,responseClass);
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }
    private static void writeRequestBody(Object request, HttpURLConnection http) throws IOException {
        if (request !=null){
            http.addRequestProperty("Content-Type", "application/json");
            String requestData=new Gson().toJson(request);
            try (OutputStream requestBody=http.getOutputStream()){
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status=http.getResponseCode();
        if (status !=200){
            try (InputStream respErr=http.getErrorStream()){
                if (respErr != null){
                    throw new DataAccessException(respErr.toString());
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
