package ru.shanalotte.coderun;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import lombok.Setter;
import ru.shanalotte.coderun.api.AnonymousCodeRunRequest;
import ru.shanalotte.coderun.api.CodeRunRequest;
import ru.shanalotte.coderun.api.UserCodeRunRequest;

public class CodeRunController {

  @Setter
  private static CodeRunService codeRunService;
  private static ObjectMapper objectMapper = new ObjectMapper();

  static Handler handleRunRequest = ctx -> {
    try {
      CodeRunRequest codeRunRequest = objectMapper.readValue(ctx.body(), UserCodeRunRequest.class);
      CodeRunResult result = codeRunService.run(codeRunRequest);
      String response = objectMapper.writeValueAsString(result);
      System.out.println(response);
      ctx.status(200);
      ctx.json(response);
    } catch (Throwable t) {
      t.printStackTrace();
      ctx.status(HttpCode.BAD_REQUEST);
    }
  };
}
