package service;

import dataaccess.*;
import model.UserData;
import model.AuthData;
import server.RegisterRequest;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthResult register(RegisterRequest request) throws DataAccessException {
        if (isBlank(request.username()) || isBlank(request.password()) || isBlank(request.email())){
            throw new BadRequestException("Error: bad request");
        }
        if (userDAO.getUser(request.username()) != null){
            throw new AlreadyTakenException("Error: already taken");
        }

        var user = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(user);

        return createAuth(user.username());
    }

    public authResult login(RegisterRequest request) throws DataAccessException {
        if (isBlank(request.username()) || isBlank(request.password()) || isBlank(request.email())){
            throw new BadRequestException("Error: bad request");
        }

        var user = userDAO.getUser(request.username());
        if (user == null || !user.password().equals(request.password())){
            throw new UnauthorizedException("Error: unauthorized");
        }

        return createAuth(user.username());
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
