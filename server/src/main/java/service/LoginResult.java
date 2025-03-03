package service;

public record LoginResult(
        int statusCode,
        String username,
        String authToken,
        String message) {
}
