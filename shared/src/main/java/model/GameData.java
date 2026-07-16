package model;

import chess.ChessGame;

public class GameData {
    public record GameDataRecord(int gameID, string whiteUsername, string blackUsername, ChessGame game) {
        GameDataRecord rename(int gameID) {
            return new GameDataRecord((gameID), whiteUsername, blackUsername, game);
        }
    }
}
