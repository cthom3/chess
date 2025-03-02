package service;
import java.util.Collection;
import model.GameData;
public record ListGamesResult(
        int code,
        Collection<GameData> games,
        String message) {
}
