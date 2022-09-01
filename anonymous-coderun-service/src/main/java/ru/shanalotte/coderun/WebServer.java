package ru.shanalotte.coderun;

import io.javalin.Javalin;
import ru.shanalotte.coderun.cache.RedisCodeRunCache;

public class WebServer {

  private Javalin app;

  public synchronized void start() {
    String redisHost = (String) CommonProperties.property("redis.host");
    int redisPort = (int)CommonProperties.property("redis.port");
    CodeRunController.setCodeRunService(new AnonymousMorriganCodeRunService(new RedisCodeRunCache(redisHost, redisPort)));
    app = Javalin
        .create()
        .start((int)CommonProperties.property("server.port"));
    app.post("/run", CodeRunController.handleRunRequest);
    app.post("/batch", CodeRunController.handleBatchRequest);
  }

  public synchronized void stop() {
    app.stop();
  }
}
