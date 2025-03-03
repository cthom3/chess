package service;
import java.util.Collection;
import model.GameData;
public record ListGamesResult(
        int statusCode,
        Collection<GameData> games,
        String message) {
}
