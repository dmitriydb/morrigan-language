package ru.shanalotte.coderun;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonProperties {

  public static final String REDIS_HOST = "redis.host";
  public static final String REDIS_PORT = "redis.port";
  public static final String SERVER_PORT = "server.port";


  private static Map<String, Object> propertiesMap = new HashMap<>();

  static {
    propertiesMap.put("cache.limit.per.user", 100);
    propertiesMap.put("cache.expiry.time.hours", 2);
    propertiesMap.put("server.port", 8003);
    propertiesMap.put("redis.port", 6379);
    propertiesMap.put("redis.host", "localhost");
  }

  public static void setProperty(String propertyName, Object propertyValue) {
    propertiesMap.put(propertyName, propertyValue);
  }

  public static void loadProperties() throws IOException {
    Properties properties = new Properties();
    properties.load(CommonProperties.class.getClassLoader().getResourceAsStream(
        "service.properties"));
    for (String property : properties.stringPropertyNames()) {
      log.debug("Loaded property {} = {}", property, properties.get(property));
      Object obj = properties.get(property);
      propertiesMap.put(property, obj);
    }
    convertPropertiesToIntegers();
  }

  private static void convertPropertiesToIntegers() {
    var propertiesToConvert = List.of(
        "cache.limit.per.user", "cache.expiry.time.hours", "server.port", "redis.port");
    for (String property : propertiesToConvert) {
      String string = (String) propertiesMap.get(property);
      if (isParseable(string)) {
        int value = Integer.parseInt((String) string);
        propertiesMap.put(property, value);
      }
    }
  }

  private static boolean isParseable(String value) {
    try {
      Integer.parseInt((String) value);
      return true;
    } catch (Throwable t) {
      return false;
    }
  }

  public static void printProperties() {
    System.out.println("=== Using properties ===");
    propertiesMap
        .forEach((key, value) -> System.out.printf("%s = %s \n", key, value));
  }

  public static Object property(String property) {
    return propertiesMap.get(property);
  }

}
