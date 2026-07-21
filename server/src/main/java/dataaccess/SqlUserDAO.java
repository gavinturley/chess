package dataaccess;

import java.sql.SQLException;

public class SqlUserDAO {
    public SqlUserDAO() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            String[] userStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
               username VARCHAR(255) NOT NULL PRIMARY KEY,
               password VARCHAR(255) NOT NULL,
               email VARCHAR(255) NOT NULL
            );
            """
            };
            for (var statement : userStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    var rs = preparedStatement.executeQuery();
                    rs.next();
                    System.out.println(rs.getInt(1));
                }
            }
        }
    }
}
