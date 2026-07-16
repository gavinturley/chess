package model;

public record AuthData(String authToken, String username) {
    AuthData rename(String authToken) {
        return new AuthData(authToken, username);
    }
}
