package sql;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class SqlAuthDaoTest {
    private SqlAuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new SqlAuthDAO();
        SqlUserDAO userDAO = new SqlUserDAO();
        authDAO.clear();
        userDAO.clear();

        userDAO.createUser(new UserData("gavin", "password", "email@email.com"));
    }

    @Test
    public void createAuthPositive() throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        var auth = new AuthData(authToken, "gavin");
        authDAO.createAuth(auth);

        var retrieved = authDAO.getAuth(authToken);
        assertNotNull(retrieved);
        assertEquals("gavin", retrieved.username());
    }

    @Test
    public void createAuthNegativeDuplicateToken() throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, "gavin"));

        // same authToken, primary key violation
        assertThrows(DataAccessException.class,
                () -> authDAO.createAuth(new AuthData(authToken, "gavin")));
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, "gavin"));

        var retrieved = authDAO.getAuth(authToken);
        assertNotNull(retrieved);
        assertEquals(authToken, retrieved.authToken());
    }

    @Test
    public void getAuthNegativeNotFound() throws DataAccessException {
        var retrieved = authDAO.getAuth("nonExistentToken");
        assertNull(retrieved);
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException {
        var authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, "gavin"));

        authDAO.deleteAuth(authToken);

        assertNull(authDAO.getAuth(authToken));
    }

    @Test
    public void deleteAuthNegativeNonExistentTokenDoesNotThrow() {
        assertDoesNotThrow(() -> authDAO.deleteAuth("neverExistedAuth"));
    }

    @Test
    public void clearPositive() throws DataAccessException {
        authDAO.createAuth(new AuthData(UUID.randomUUID().toString(), "gavin"));
        authDAO.clear();

        var listedToken = authDAO.getAuth("anyAuth");
        assertNull(listedToken);
    }
}
