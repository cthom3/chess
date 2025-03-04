package service.gamerecords;

public record CreateGameRequest(String gameName,
                                String authToken) {
}
