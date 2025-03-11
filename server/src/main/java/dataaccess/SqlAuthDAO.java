package dataaccess;
import model.AuthData;

public class SqlAuthDAO implements AuthDAO{
    public SqlAuthDAO() {
        //
    }
    public void createAuth(String authToken, String username){
        var insertStatement="INSERT INTO auth (username,authToken) VALUES (?,?)";



    }

    public AuthData getAuth(String authToken){

    }

    public void deleteAuth(String authToken){

    }

    public void clear(){

    }
}
