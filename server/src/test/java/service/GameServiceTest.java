package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import server.CreateGameRequest;
import server.JoinGameRequest;

public class GameServiceTest {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private GameService gameService;
    private String authToken;

    @BeforeEach
    public void setup() throws Exception {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        gameService = new GameService(gameDAO, authDAO);

        authToken = "testToken";
        authDAO.createAuth(new AuthData(authToken, "gavin"));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        gameService.createGame(authToken, new CreateGameRequest("gameOne"));
        gameService.createGame(authToken, new CreateGameRequest("gameTwo"));
        var result = gameService.listGames(authToken);
        assertEquals(2, result.games().size());
    }

    @Test
    public void listGamesFailsWithBadAuthentification() {
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("badAuthToken"));
    }

    @Test
    public void createGameSuccess() throws Exception {
        var result = gameService.createGame(authToken, new CreateGameRequest("thisGame"));
        assertTrue(result.gameID() > 0);
    }

    @Test
    public void joinGameSuccess() throws Exception {
        var createResult = gameService.createGame(authToken, new CreateGameRequest("thisGame"));
        gameService.joinGame(authToken, new JoinGameRequest("WHITE", createResult.gameID()));
        assertEquals("gavin", gameDAO.getGame(createResult.gameID()).whiteUsername());
    }

    @Test
    public void joinGameFailsWhenColorAlreadyTaken() throws Exception {
        var createResult = gameService.createGame(authToken, new CreateGameRequest("thisGame"));
        gameService.joinGame(authToken, new JoinGameRequest("WHITE", createResult.gameID()));

        authDAO.createAuth(new AuthData("thatToken", "otherUser"));

        assertThrows(AlreadyTakenException.class, () ->
                gameService.joinGame("thatToken", new JoinGameRequest("WHITE", createResult.gameID())));
    }

}
