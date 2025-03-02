package service;

public record LoginResult(
        int code,
        String username,
        String authToken,
        String message) {
}
