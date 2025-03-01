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
            userAccess.getUser(username);
            // check to make sure this is returning null
            try {
                userAccess.createUser(username, password, email);
                try {
                    String authToken = generateToken();
                    authAccess.createAuth(authToken,username);
                    return new RegisterResult(username, authToken, null);
                } catch (DataAccessException ex) {
                    return new RegisterResult(null,null,ex.getMessage());
                }
            } catch (DataAccessException ex) {
                return new RegisterResult(null,null,ex.getMessage());
            }
        } catch (DataAccessException ex){
            return new RegisterResult(null,null,ex.getMessage());
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
                    return new LoginResult(username, authToken, null);
                } catch (DataAccessException ex){
                    return new LoginResult (null,null,ex.getMessage());
                }
            } else {
                return new LoginResult (null,null,"Error: unauthorized");
            }
        } catch (DataAccessException ex){
            return new LoginResult(null,null,ex.getMessage());
        }
    }
    public LogoutResult logout (LogoutRequest logoutRequest){
        String authToken=logoutRequest.authToken();
        try {
            authAccess.deleteAuth(authToken);
            return new LogoutResult(null);
        } catch (DataAccessException ex){
            return new LogoutResult(ex.getMessage());
        }
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
