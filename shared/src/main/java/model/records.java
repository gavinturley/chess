package model;

import chess.ChessGame;

public record GameDataRecord(int gameID, string whiteUsername, string blackUsername, ChessGame game) {
    GameDataRecord rename(int gameID) {
        return new GameDataRecord((gameID), whiteUsername, blackUsername, game);
    }
}

public record UserDataRecord(string username, string password, string email) {
    UserDataRecord rename(string username) {
        return new UserDataRecord(username, password, email);
    }
}

public record AuthDataRecord(string authToken, string username) {
    AuthDataRecord rename(string authToken) {
        return new AuthDataRecord(authToken, username);
    }
}