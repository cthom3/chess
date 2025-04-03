package messages;
import com.google.gson.Gson;

public record Action (Type type, String currentUser){
    public enum Type {
        PLAYER,
        OBSERVER,
        MOVE,
        LEFT,
        RESIGN,
        CHECK,
        CHECKMATE
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
