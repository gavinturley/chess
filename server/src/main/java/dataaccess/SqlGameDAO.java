package dataaccess;


import model.GameData;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAO implements GameDAO{
    private final Gson gson = new Gson();

    public SqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public int createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());
            preparedStatement.setString(4, gson.toJson(game.game()));
            preparedStatement.executeUpdate();

            try (var keys = preparedStatement.getGeneratedKeys()){
                if (keys.next()){
                    return keys.getInt(1);
                }
            }
            throw new DataAccessException("Failed to generate game ID");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setInt(1, gameID);
                try (var returnStatement = preparedStatement.executeQuery()){
                    if (returnStatement.next()) {
                        return readGame(returnStatement);
                    }
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> result = new ArrayList<>();
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement);
             var returnStatement = preparedStatement.executeQuery()){
            while (returnStatement.next()){
                result.add(readGame(returnStatement));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void updateGame(GameData game) throws DataAccessException {
        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)){
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());
            preparedStatement.setString(4, gson.toJson(game.game()));
            preparedStatement.setInt(5, game.gameID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() throws DataAccessException {
        SqlHelp.clear("game");
    }

    private GameData readGame(java.sql.ResultSet resultSet) throws SQLException {
        var chessGame = gson.fromJson(resultSet.getString("game"), chess.ChessGame.class);
        return new GameData(
                resultSet.getInt("gameID"),
                resultSet.getString("whiteUsername"),
                resultSet.getString("blackUsername"),
                resultSet.getString("gameName"),
                chessGame
        );
    }

    public void configureDatabase() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL
            );
            """;
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
