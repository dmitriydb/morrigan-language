package ru.shanalotte.coderun.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.CommonProperties;
import ru.shanalotte.coderun.api.CodeRunRequest;

@Slf4j
public class RedisCodeRunCache implements CodeRunCache {

  private final ThreadLocal<Jedis> jedis;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public RedisCodeRunCache(String host, int port) {
    jedis = ThreadLocal.withInitial(() -> new Jedis(host, port));
  }

  ;

  public RedisCodeRunCache() {
    jedis = ThreadLocal.withInitial(() -> new Jedis());
  }

  @Override
  public void cache(CodeRunRequest request, CodeRunResult result) {
    try {
      String key = buildRedisKey(request);
      String value = objectMapper.writeValueAsString(result);
      jedis().set(key, value);
      log.debug("Cached {} = {}", key, value);
      long ttl = 60L * 60 * (Integer) CommonProperties.property("cache.expiry.time.hours");
      jedis().expire(key, ttl);
      log.debug("EXPIRY {} FOR {}", key, ttl);
    } catch (JsonProcessingException e) {
      log.error("Error while caching", e);
    }
  }

  private Jedis jedis() {
    return jedis.get();
  }

  private String buildRedisKey(CodeRunRequest request) throws JsonProcessingException {
    return "user." + request.username() + "." + objectMapper.writeValueAsString(request);
  }

  @Override
  public boolean contains(CodeRunRequest request) {
    try {
      log.debug("Searching {} in redis", request);
      return jedis().exists(buildRedisKey(request));
    } catch (JsonProcessingException e) {
      log.error("Error when checking if cache contains request " + request, e);
      return false;
    }
  }

  @Override
  public Optional<CodeRunResult> get(CodeRunRequest request) {
    try {
      String key = buildRedisKey(request);
      String value = jedis().get(key);
      CodeRunResult result = objectMapper.readValue(value, CodeRunResult.class);
      return Optional.of(result);
    } catch (JsonProcessingException e) {
      log.error("Error when retrieving cached value for request " + request, e);
      return Optional.empty();
    }
  }
}
