package service.userrecords;

public record RegisterRequest(
        String username,
        String password,
        String email) {
}
