package service;

public record CreateGameResult(int statusCode,
                               Integer gameID,
                               String message) {
}
