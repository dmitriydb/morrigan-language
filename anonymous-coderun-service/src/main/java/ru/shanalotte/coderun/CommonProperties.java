package ru.shanalotte.coderun;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
      Object obj = properties.get(property);
      propertiesMap.put(property, obj);
    }
    convertPropertiesToIntegers();
  }

  private static void convertPropertiesToIntegers() {
    var propertiesToConvert = List.of("cache.limit.per.user", "cache.expiry.time.hours", "server.port", "redis.port");
    for (String property : propertiesToConvert) {
      try {
        var string = propertiesMap.get(property);
        int value = Integer.parseInt((String) string);
        propertiesMap.put(property, value);
      } catch (Throwable t) {

      }
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
