package service;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;

public class ClearService {
    private final UserDAO userAccess;
    private final AuthDAO authAccess;
    private final GameDAO gameAccess;
    public ClearService(UserDAO userAccess, AuthDAO authAccess, GameDAO gameAccess){
        this.userAccess=userAccess;
        this.authAccess=authAccess;
        this.gameAccess=gameAccess;
    }
    public ClearResult clear(ClearRequest clearrequest){
        try {
            gameAccess.clear();
            authAccess.clear();
            userAccess.clear();
            return new ClearResult (200,null);
        } catch (DataAccessException ex){
            return new ClearResult (500,ex.getMessage());
        }

    }
}
