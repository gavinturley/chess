package dataaccess;

import java.sql.SQLException;

public class SqlHelp {
    public static void clear(String dataType) throws DataAccessException {
        var statement = "TRUNCATE " + dataType;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (
                SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
