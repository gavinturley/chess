package server;

import dataaccess.*;
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

        javalin.post("/user", this::register);
//        javalin.post("/session", this::login);
//        javalin.post("/session", this::logout);
//        javalin.get("/game", this::listGames);
//        javalin.post("/game", this::createGame);
//        javalin.put("/game", this::joinGame);
        javalin.delete("/db", this::clear);
    }

    private void register(Context ctx){
        try {
            registerService.register(ctx.body());
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException e){
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
        } catch (BadRequestException e){
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: bad request");
        } catch (AlreadyTakenException e){
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: already taken");
        }
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

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
