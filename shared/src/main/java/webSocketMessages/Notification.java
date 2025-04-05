package webSocketMessages;
import com.google.gson.Gson;

public record Notification(Type type, String message) {
    public enum Type{
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
