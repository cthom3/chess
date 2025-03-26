package gamerecords;

public record CreateGameRequest(String gameName,
                                String authToken) {
}
