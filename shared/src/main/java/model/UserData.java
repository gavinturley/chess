package model;

public record UserData(String username, String password, String email) {
    UserData rename(String username) {
        return new UserData(username, password, email);
    }
}

