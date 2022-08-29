package ru.shanalotte.coderun.cache;

import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import ru.shanalotte.coderun.CodeRunResult;
import ru.shanalotte.coderun.api.CodeRunRequest;

public class RedisCodeRunCache implements CodeRunCache {

  private final Jedis jedis;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public RedisCodeRunCache(String host, int port) {
    jedis = new Jedis(host, port);
  }

  public RedisCodeRunCache() {
    jedis = new Jedis();
  }

  @Override
  public void cache(CodeRunRequest request, CodeRunResult result) {
    try {
      String key = objectMapper.writeValueAsString(request);
      String value = objectMapper.writeValueAsString(result);
      jedis.set(key, value);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean contains(CodeRunRequest request) {
    try {
      return jedis.exists(objectMapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Optional<CodeRunResult> get(CodeRunRequest request) {
    try {
      String key = objectMapper.writeValueAsString(request);
      String value = jedis.get(key);
      CodeRunResult result = objectMapper.readValue(value, CodeRunResult.class);
      return Optional.of(result);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
