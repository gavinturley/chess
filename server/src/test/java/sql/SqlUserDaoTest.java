package sql;

import dataaccess.DataAccessException;
import dataaccess.SqlUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SqlUserDaoTest {
    private SqlUserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO = new SqlUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserPositive() throws DataAccessException {
        var user = new UserData("gavin", "password", "email@email.com");
        userDAO.createUser(user);

        var retrieved = userDAO.getUser("gavin");
        assertNotNull(retrieved);
        assertEquals("gavin", retrieved.username());
    }

    @Test
    public void createUserNegative() throws DataAccessException {
        var user = new UserData("gavin", "password", "email@email.com");
        userDAO.createUser(user);

        var duplicate = new UserData("gavin", "differentPassword", "differentEmail@email.com");
        assertThrows(DataAccessException.class, () -> userDAO.createUser(duplicate));
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        var user = new UserData("saylor", "pass", "say@email.com");
        userDAO.createUser(user);

        var retrieved = userDAO.getUser("saylor");
        assertNotNull(retrieved);
        assertEquals("saylor", retieved.username());
        assertEquals("say@email.com", retrieved.email());
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        var retrieved = userDAO.getUser("nonExistantUser");
        assertNull(retrieved);
    }

    @Test
    public void clearPositive() throws DataAccessException {
        userDAO.createUser(new UserData("nivag", "password", "email@email.com"));
        userDAO.clear();

        assertNull(userDAO.getUser("nivag"));
    }
}
