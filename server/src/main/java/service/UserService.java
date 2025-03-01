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
        LoginResult loginResult=new LoginResult(username, authToken);
        return loginResult;
    }
    public void logout (LogoutRequest logoutRequest){

    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
