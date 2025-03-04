package service.gamerecords;

public record CreateGameResult(int statusCode,
                               Integer gameID,
                               String message) {
}
