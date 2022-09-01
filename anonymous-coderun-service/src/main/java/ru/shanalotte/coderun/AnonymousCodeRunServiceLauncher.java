package ru.shanalotte.coderun;


import java.io.IOException;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import ru.shanalotte.Morrigan;
import ru.shanalotte.coderun.cache.RedisCodeRunCache;

@Slf4j
public class AnonymousCodeRunServiceLauncher {

  public static void main(String[] args) {
    loadProperties();
    printProperties();
    new WebServer().start();
  }



  private static void printProperties() {
    CommonProperties.printProperties();
  }

  private static void loadProperties() {
    log.info("Loading properties...");
    try {
      CommonProperties.loadProperties();
    } catch (IOException e) {
      log.warn("Couldn't load properties from application.properties, using default ones");
      e.printStackTrace();
    }
  }


}
