package ru.shanalotte.coderun;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.UserCodeRunRequest;

@Slf4j
public class CodeRunController {

  @Setter
  private static CodeRunService codeRunService;
  private static ObjectMapper objectMapper = new ObjectMapper();

  static Handler handleRunRequest = ctx -> {
    log.info("GET /run IP {} body {}", ctx.ip(), ctx.body());
    try {
      CodeRunRequest codeRunRequest = objectMapper.readValue(ctx.body(), UserCodeRunRequest.class);
      CodeRunResult result = codeRunService.run(codeRunRequest);
      String response = objectMapper.writeValueAsString(result);
      ctx.status(200);
      log.info("{} IP {} body {} response {}", ctx.status(), ctx.ip(), ctx.body(), response);
      ctx.json(response);
    } catch (Throwable t) {
      t.printStackTrace();
      ctx.status(HttpCode.BAD_REQUEST);
    }
  };

  static Handler handleBatchRequest = ctx -> {
    log.info("GET /batch IP {} body {}", ctx.ip(), ctx.body());
    try {
      CodeRunRequest[] codeRunRequest = objectMapper.readValue(ctx.body(), UserCodeRunRequest[].class);
      List<CodeRunResult> result = codeRunService.batchRun(Arrays.stream(codeRunRequest).collect(Collectors.toList()));
      String response = objectMapper.writeValueAsString(result);
      log.info("{} IP {} body {} response {}", ctx.status(), ctx.ip(), ctx.body(), response);
      ctx.status(200);
      ctx.json(response);
    } catch (Throwable t) {
      t.printStackTrace();
      ctx.status(HttpCode.BAD_REQUEST);
    }
  };
}
