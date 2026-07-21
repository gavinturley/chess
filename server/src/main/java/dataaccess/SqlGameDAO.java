package dataaccess;


import java.sql.SQLException;

public class SqlGameDAO {
    private final String[] gameStatement = {
            """

            """
    };

    public SqlGameDAO() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement()) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        }
    }
}
