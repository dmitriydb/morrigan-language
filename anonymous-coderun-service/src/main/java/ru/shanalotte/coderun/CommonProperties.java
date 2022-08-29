package ru.shanalotte.coderun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommonProperties {

  private static Map<String, Object> propertiesMap = new HashMap<>();

  static {
    propertiesMap.put("cache.limit.per.user", 100);
    propertiesMap.put("cache.expiry.time.hours", 2);
    propertiesMap.put("server.port", 8003);
    propertiesMap.put("redis.port", 6379);
    propertiesMap.put("redis.host", "localhost");
  }


  public static void loadProperties() throws IOException {
    Properties properties = new Properties();
    properties.load(CommonProperties.class.getClassLoader().getResourceAsStream("application.properties"));
    for (String property : properties.stringPropertyNames()) {
      propertiesMap.put(property, properties.get(property));
    }
  }

  public static void printProperties() {
    System.out.println("=== Using properties ===");
    propertiesMap.entrySet().forEach(p -> System.out.printf("%s = %s \n", p.getKey(), p.getValue()));
  }

  public static Object property(String property) {
    return propertiesMap.get(property);
  }

}
