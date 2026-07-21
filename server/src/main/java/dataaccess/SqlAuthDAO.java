package dataaccess;

import java.sql.SQLException;

public class SqlAuthDAO {
    public SqlAuthDAO() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            String[] authStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(255) NOT NULL PRIMARY KEY,
                username VARCHAR(255) NOT NULL,
                FOREIGN KEY (username) REFERENCES user(username)
            );
            """
            };
            for (var statement : authStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    var rs = preparedStatement.executeQuery();
                    rs.next();
                    System.out.println(rs.getInt(1));
                }
            }
        }
    }
}
