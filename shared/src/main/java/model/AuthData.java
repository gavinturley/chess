package model;

public class AuthData {
    public record AuthDataRecord(string authToken, string username) {
        AuthDataRecord rename(string authToken) {
            return new AuthDataRecord(authToken, username);
        }
    }
}
