package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SqlGameDaoTest {
    private SqlGameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO = new SqlGameDAO();
        gameDAO.clear();
    }

    @Test
    public void createGamePositive() throws DataAccessException {
        var game = new GameData(0, null, null, "friendly match", new ChessGame());
        int gameID = gameDAO.createGame(game);

        assertTrue(gameID > 0);
        assertNotNull(gameDAO.getGame(gameID));
    }

    @Test
    public void createGameNegative() {
        // Test missing game name
        var game = new GameData(0, null, null, "gameName", new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(game));
    }

    @Test
    public void getGamePositive() throws DataAccessException {
        var game = new GameData(0, null, null, "new match", new ChessGame());
        int gameID = gameDAO.createGame(game);

        var retrieved = gameDAO.getGame(gameID);
        assertNotNull(retrieved);
        assertEquals("new match", retrieved.gameName());
        assertNotNull(retrieved.game());
    }

    @Test
    public void getGameNegative() throws DataAccessException {
        // Test not found
        var retrieved = gameDAO.getGame(1239483);
        assertNull(retrieved);
    }

    @Test
    public void listGamesPositive() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "game one", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "game two", new ChessGame()));

        var games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    public void listGamesNegative() throws DataAccessException {
        var games = gameDAO.listGames();
        assertTrue(games.isEmpty());
    }

    @Test
    public void updateGamePositive() throws DataAccessException {
        var game = new GameData(0, null, null, "game", new ChessGame());
        int gameID = gameDAO.createGame(game);

        var updated = new GameData(gameID, "gavin", "saylor", "game", new ChessGame());
        gameDAO.updateGame(updated);

        var retrieved = gameDAO.getGame(gameID);
        assertEquals("gavin", retrieved.whiteUsername());
    }

    @Test
    public void updateGameNegative() throws DataAccessException {
        var ghostGame = new GameData(129382, "gavin", null, "ghost game", new ChessGame());
        assertDoesNotThrow(() -> gameDAO.updateGame(ghostGame));
        assertDoesNotThrow(() -> {
            var result = gameDAO.getGame(129382);
            assertNull(result);
        });
    }

    @Test
    public void clearPositive() throws DataAccessException {
        gameDAO.createGame(new GameData(0, null, null, "match", new ChessGame()));
        gameDAO.clear();

        assertTrue(gameDAO.listGames().isEmpty());
    }
}
