package service.userrecords;

public record LoginResult(
        int statusCode,
        String username,
        String authToken,
        String message) {
}
