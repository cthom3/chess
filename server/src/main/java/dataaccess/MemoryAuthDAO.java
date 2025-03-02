package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap <String, AuthData> authentication = new HashMap<>();
    public void createAuth(String authToken, String username){
        AuthData authenticate= new AuthData(authToken,username);
        authentication.put(authToken, authenticate);
    }

    public AuthData getAuth(String authToken){
        return authentication.get(authToken);
    }

    public void deleteAuth(String authToken){
        authentication.remove(authToken);
    }

    public void clear (){
        authentication.clear();
    }
}
