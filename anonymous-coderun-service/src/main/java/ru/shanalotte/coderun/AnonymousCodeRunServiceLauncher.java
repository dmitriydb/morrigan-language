package ru.shanalotte.coderun;


import java.io.IOException;
import io.javalin.Javalin;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.cache.RedisCodeRunCache;

public class AnonymousCodeRunServiceLauncher {

  public static void main(String[] args) {
    loadProperties();
    printProperties();
    startWebServer();
  }

  private static void startWebServer() {
    String redisHost = (String) CommonProperties.property("redis.host");
    int redisPort = (int)CommonProperties.property("redis.port");
    CodeRunController.setCodeRunService(new AnonymousMorriganCodeRunService(new Morrigan(), new RedisCodeRunCache(redisHost, redisPort)));
    Javalin app = Javalin
        .create()
        .start((int)CommonProperties.property("server.port"));
    app.get("/run", CodeRunController.handleRunRequest);
  }

  private static void printProperties() {
    CommonProperties.printProperties();
  }

  private static void loadProperties() {
    try {
      CommonProperties.loadProperties();
    } catch (IOException e) {
      System.out.println("Couldn't load properties from application.properties");
      e.printStackTrace();
    }
  }


}
