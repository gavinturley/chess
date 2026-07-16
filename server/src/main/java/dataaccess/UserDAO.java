package dataaccess;

import model.UserData;

public interface UserDao {
    void createUser(UserData u) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
