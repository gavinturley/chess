package client;

import org.junit.jupiter.api.*;
import server.Server;
import model.*;
import static org.junit.jupiter.api.Assertions.*;



public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    void clearDatabase() throws Exception {
        serverFacade.clear();
    }


    @Test
    void registerPositive() throws Exception {
        var authData = serverFacade.register("gavin", "pw", "g@e.com");
        assertNotNull(authData.authToken());
        assertEquals("gavin", authData.username());
    }

    /* Duplicate username */
    @Test
    void registerNegative() throws Exception {
        var authData = serverFacade.register("gavin", "pw", "g@e.com");
        assertThrows(ResponseException.class, () ->
                serverFacade.register("gavin", "different", "w@e.com"));
    }

    @Test
    void loginPositive() throws Exception {
        serverFacade.register("gavin", "pw", "g@e.com");
        var authData = serverFacade.login("gavin", "pw");
        assertNotNull(authData.authToken());
        assertEquals("gavin", authData.username());
    }

    /* Wrong password */
    @Test
    void loginNegative() throws Exception {
        serverFacade.register("gavin", "pw", "g@e.com");
        assertThrows(ResponseException.class, () ->
                serverFacade.login("gavin", "wrong"));
    }

    @Test
    void logoutPositive() throws Exception {
        var authData = serverFacade.register("gavin", "pw", "g@e.com");
        assertDoesNotThrow(() -> serverFacade.logout(authData.authToken()));
    }

    /* Bad auth token */
    @Test
    void logoutNegative() throws Exception {
        assertThrows(ResponseException.class, () -> serverFacade.logout("gavin"));
    }

    @Test
    void createGamePositive() throws Exception {
        var authData = serverFacade.register("gavin", "pw", "g@e.com");
        int gameID = serverFacade.createGame(authData.authToken(), "chessGame");
        assertTrue(gameID > 0);
    }

    /* Bad auth token */
    @Test
    void createGameNegative() throws Exception {
        assertThrows(ResponseException.class, () -> serverFacade.createGame("badAuth", "badGame"));
    }

    @Test
    void listGamesPositive() throws Exception {
        var authData = serverFacade.register("gavin", "pw", "g@e.com");
        serverFacade.createGame(authData.authToken(), "chessGame1");
        serverFacade.createGame(authData.authToken(), "chessGame2");

        var games = serverFacade.listGames(authData.authToken());
        assertEquals(2, games.size());
    }

    /* Bad auth */
    @Test
    void listGamesNegative() throws Exception {
        assertThrows(ResponseException.class, () -> serverFacade.listGames("badAuth"));
    }

    @Test
    void joinGamePositive() throws Exception {
        var authData = serverFacade.register("gavin", "pw", "g@e.com");
        int gameID = serverFacade.createGame(authData.authToken(), "chessGame");
        assertDoesNotThrow(() -> serverFacade.joinGame(authData.authToken(), "WHITE", gameID));
    }

    /* Color already taken */
    @Test
    void joinGameNegative() throws Exception {
        var authData1 = serverFacade.register("gavin", "pw", "g@e.com");
        var authData2 = serverFacade.register("gsmitty", "pw", "s@e.com");
        int gameID = serverFacade.createGame(authData.authToken(), "chessGame");

        serverFacade.joinGame(authData1.authToken(), "WHITE", gameID);
        assertThrows(ResponseException.class, () ->
                serverFacade.joinGame(authData2.authToken(), "WHITE", gameID));
    }

    @Test
    void clearPositive() throws Exception {
        var authData = serverFacade.register("gavin", "pw", "g@e.com");
        serverFacade.createGame(authData.authToken(), "chessGame");

        assertDoesNotThrow(() -> serverFacade.clear());
        assertThrows(ResponseException.class, () -> serverFacade.listGames(authData.authToken()));
    }

    /* Empty database */
    @Test
    void clearNegative() throws Exception {
        assertDoesNotThrow(() -> serverFacade.clear());
        assertDoesNotThrow(() -> serverFacade.clear());
    }

}

