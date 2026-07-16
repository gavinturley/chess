package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    @Test
    public void clearRemovesAllData() throws Exception {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();

        userDAO.createUser(new UserData("gavin", "password", "example@email.com"));

        var clearService = new ClearService(gameDAO, userDAO, authDAO);
        clearService.clear();

        assertNull(userDAO.getUser("gavin"));
        assertTrue(gameDAO.listGames().isEmpty());
    }
}