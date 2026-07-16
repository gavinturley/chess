package server;

import dataaccess.DataAccessException;
import io.javalin.*;

import javax.xml.crypto.Data;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.post("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);
        javalin.delete("/db", this::clear);
    }

    private void register(){

    }

    private void clear(){
        try {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException e) {
            ctx.status(500);
            var serializer = new Gson();
            ctx.result(serializer.toJson(Map.of("message", e.getMessage())));
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
