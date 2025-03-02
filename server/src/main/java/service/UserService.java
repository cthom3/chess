package service;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.UserData;
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
        try {
            UserData givenUser=userAccess.getUser(username);
            if (givenUser==null) {
                // check to make sure this is returning null
                try {
                    userAccess.createUser(username, password, email);
                    try {
                        String authToken = generateToken();
                        authAccess.createAuth(authToken, username);
                        return new RegisterResult(200,username, authToken, null);
                    } catch (DataAccessException ex) {
                        return new RegisterResult(500,null, null, ex.getMessage());
                    }
                } catch (DataAccessException ex) {
                    return new RegisterResult(500,null, null, ex.getMessage());
                }
            } else {
                return new RegisterResult(403,null,null,"Error: already taken");
            }
        } catch (DataAccessException ex){
            return new RegisterResult(400,null,null,"Error: bad request");
        }
    }
    public LoginResult login (LoginRequest loginRequest){
        String username=loginRequest.username();
        String password=loginRequest.password();
        try {
            UserData userData=userAccess.getUser(username);
            String userPassword=userData.password();
            if (password.equals(userPassword)){
                try {
                    String authToken = generateToken();
                    authAccess.createAuth(authToken, username);
                    return new LoginResult(200,username, authToken, null);
                } catch (DataAccessException ex){
                    return new LoginResult (500,null,null,ex.getMessage());
                }
            } else {
                return new LoginResult (401,null,null,"Error: unauthorized");
            }
        } catch (DataAccessException ex){
            return new LoginResult(500,null,null,ex.getMessage());
        }
    }
    public LogoutResult logout (LogoutRequest logoutRequest){
        String authToken=logoutRequest.authToken();
        if (authToken != null){
            try {
                authAccess.deleteAuth(authToken);
                return new LogoutResult(200,null);
            } catch (DataAccessException ex){
                return new LogoutResult(500,ex.getMessage());
            }
        } else {
            return new LogoutResult(401,"Error: unauthorized");
        }

    }
    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
