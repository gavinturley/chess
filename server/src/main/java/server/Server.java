package server;

import dataAccess.*;
import service.ClearService;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final ClearService clearService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        var userDAO = new MemoryUserDAO();
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();

        clearService = new ClearService(gameDAO, userDAO, authDAO);

//        javalin.post("/user", this::register);
//        javalin.post("/session", this::login);
//        javalin.post("/session", this::logout);
//        javalin.get("/game", this::listGames);
//        javalin.post("/game", this::createGame);
//        javalin.put("/game", this::joinGame);
        javalin.delete("/db", this::clear);
    }

    private void clear(Context ctx){
        try {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException e) {
            ctx.status(500);
            var serializer = new Gson();
            ctx.result(serializer.toJson(Map.of("message", "Error: " + e.getMessage())));
        }
    }

//  import java.util.UUID;
//
//    public static String generateToken() {
//        return UUID.randomUUID().toString();
//    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
