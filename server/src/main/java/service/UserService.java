package service;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import service.userrecords.*;

import java.util.UUID;

public class UserService {
    private final UserDAO userAccess;
    private final AuthDAO authAccess;
    public UserService(UserDAO userAccess, AuthDAO authAccess){
        this.userAccess=userAccess;
        this.authAccess=authAccess;
    }
    public RegisterResult register(RegisterRequest registerRequest){
        String username=registerRequest.username();
        String password=registerRequest.password();
        String email=registerRequest.email();
        if (password!=null) {
            try {
                UserData givenUser = userAccess.getUser(username);
                if (givenUser == null) {
                    // check to make sure this is returning null
                    try {
                        userAccess.createUser(username, password, email);
                        String authToken = generateToken();
                        authAccess.createAuth(authToken, username);
                        return new RegisterResult(200, username, authToken, null);
                    } catch (DataAccessException ex) {
                        return new RegisterResult(500, null, null, ex.getMessage());
                    }
                } else {
                    return new RegisterResult(403, null, null, "Error: already taken");
                }
            } catch (DataAccessException ex) {
                return new RegisterResult(400, null, null, "Error: bad request");
            }
        } else {
            return new RegisterResult(400,null,null,"Error: bad request");
        }
    }
    public LoginResult login (LoginRequest loginRequest){
        String username=loginRequest.username();
        String password=loginRequest.password();
        try {
            UserData userData=userAccess.getUser(username);
            if (userData !=null && password.equals(userData.password())) {
                String userPassword = userData.password();
                try {
                    String authToken = generateToken();
                    authAccess.createAuth(authToken, username);
                    return new LoginResult(200, username, authToken, null);
                } catch (DataAccessException ex) {
                    return new LoginResult(500, null, null, ex.getMessage());
                }
            } else {
                return new LoginResult(401,null,null, "Error: unauthorized");
            }
        } catch (DataAccessException ex){
            return new LoginResult(400,null,null,"Error: bad request");
        }
    }
    public LogoutResult logout (LogoutRequest logoutRequest){
        if (logoutRequest!=null && logoutRequest.authToken()!=null) {
            String authToken = logoutRequest.authToken();
            try {
                AuthData retrievedToken = authAccess.getAuth(authToken);
                if (retrievedToken != null) {
                    try {
                        authAccess.deleteAuth(authToken);
                        return new LogoutResult(200, null);
                    } catch (DataAccessException ex) {
                        return new LogoutResult(500, ex.getMessage());
                    }
                } else {
                    return new LogoutResult(401, "Error: unauthorized");
                }
            } catch (DataAccessException ex) {
                return new LogoutResult(500, ex.getMessage());
            }
        } else {
            return new LogoutResult(401,"Error: unauthorized");
        }
    }
    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
