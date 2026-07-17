package service;

import com.mysql.cj.log.Log;
import dataaccess.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import server.LoginRequest;
import server.RegisterRequest;

public class UserServiceTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerSuccess() throws Exception {
        var result = userService.register(new RegisterRequest("gavin", "password", "email@email.com"));
        assertEquals("gavin", result.username());
        assertNotNull(result.authToken());
        assertNotNull(userDAO.getUser("gavin"));
    }

    @Test
    public void registerFailsWhenUsernameTaken() throws Exception {
        userService.register(new RegisterRequest("gavin", "password", "email@email.com"));
        assertThrows(AlreadyTakenException.class, () ->
                userService.register(new RegisterRequest("gavin", "otherPassword", "differentemail@email.com")));
    }

    @Test
    public void loginSuccess() throws Exception {
        userService.register(new RegisterRequest("gavin", "password", "email@email.com"));
        var result = userService.login(new LoginRequest("gavin", "password"));
        assertEquals("gavin", result.username());
        assertNotNull(result.authToken());
    }

    @Test
    public void loginFailsWithWrongPassword() throws Exception {
        userService.register(new RegisterRequest("gavin", "password", "email@email.com"));
        assertThrows(UnauthorizedException.class, () ->
                userService.login(new LoginRequest("gavin", "wrongPassword")));
    }

    @Test
    public void logoutSuccess() throws Exception {
        var registerResult = userService.register(new RegisterRequest("gavin", "password", "email@email.com"));
        userService.logout(registerResult.authToken());
        assertNull(authDAO.getAuth(registerResult.authToken()));
    }

    @Test
    public void logoutFailsWithInvalidAuth() {
        assertThrows(UnauthorizedException.class, () -> userService.logout("badToken"));
    }
}

