package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SqlUserDAO {
    public SqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, passwordEncryptor(user.password()));
            preparedStatement.setString(3, user.email());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                try (var returnStatement = preparedStatement.executeQuery()){
                    if (returnStatement.next()) {
                        return new UserData(returnStatement.getString("username"), returnStatement.getString("password"), returnStatement.getString("email"));
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    public void clear() throws DataAccessException {
        SqlHelp.clear("user");
    }

    public String passwordEncryptor(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void configureDatabase() throws DataAccessException {
        var createStatements = """
                CREATE TABLE IF NOT EXISTS user (
                   username VARCHAR(255) NOT NULL PRIMARY KEY,
                   password VARCHAR(255) NOT NULL,
                   email VARCHAR(255) NOT NULL
                );
                """;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(createStatements)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
