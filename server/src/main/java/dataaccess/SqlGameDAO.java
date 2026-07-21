package dataaccess;


import java.sql.SQLException;

public class SqlGameDAO {

    public SqlGameDAO() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    var rs = preparedStatement.executeQuery();
                    rs.next();
                    System.out.println(rs.getInt(1));
                }
            }
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL
            );
            """
    };
}
