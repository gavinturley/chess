package model;

public class UserData {
    public record UserDataRecord(string username, string password, string email) {
        UserDataRecord rename(string username) {
            return new UserDataRecord(username, password, email);
        }
    }
}
