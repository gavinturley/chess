package dataaccess;

import java.sql.SQLException;

public class SqlHelp {
    public static void clear(String dataType) throws DataAccessException {
        String statement = "TRUNCATE " + dataType;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (
                SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
}
