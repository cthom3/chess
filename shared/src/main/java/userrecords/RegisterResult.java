package userrecords;

public record RegisterResult(
        int statusCode,
        String username,
        String authToken,
        String message) {
}
