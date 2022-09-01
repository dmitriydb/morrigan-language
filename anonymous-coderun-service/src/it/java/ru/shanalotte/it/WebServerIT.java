package ru.shanalotte.it;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.shanalotte.coderun.CodeRunResult;
import ru.shanalotte.coderun.CommonProperties;
import ru.shanalotte.coderun.TestUtils;
import ru.shanalotte.coderun.WebServer;
import ru.shanalotte.coderun.api.CodeRunRequest;

@Testcontainers
public class WebServerIT {

  private ObjectMapper objectMapper = new ObjectMapper();
  private WebServer webServer;
  private HttpClient httpClient;

  @Container
  public GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
      .withExposedPorts(6379);

  @BeforeEach
  public void refreshProperties() throws IOException {
    CommonProperties.loadProperties();
    CommonProperties.printProperties();
    CommonProperties.setProperty(CommonProperties.REDIS_HOST, redis.getHost());
    CommonProperties.setProperty(CommonProperties.REDIS_PORT, redis.getFirstMappedPort());
    webServer = new WebServer();
    httpClient = HttpClient.newBuilder()
        .build();
    webServer.start();
  }

  @AfterEach
  public void stop_Webserver() {
    webServer.stop();
  }

  @Test
  public void should_AcceptCodeAtRunEndpoint() throws IOException, InterruptedException {

    CodeRunRequest codeRunRequest = TestUtils.randomCodeRequest();
    String body = objectMapper.writeValueAsString(codeRunRequest);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .uri(URI.create("http://localhost:8003/run"))
        .build();

    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    CodeRunResult codeRunResult = objectMapper.readValue(response.body(), CodeRunResult.class);
    assertThat(codeRunResult.getStdout()).hasSize(1);
    assertThat(body).contains(codeRunResult.getStdout().get(0));
  }

  @Test
  public void should_AcceptInvalidCodeAtRunEndpoint() throws IOException, InterruptedException {

    CodeRunRequest codeRunRequest = TestUtils.invalidRequest();
    String body = objectMapper.writeValueAsString(codeRunRequest);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .uri(URI.create("http://localhost:8003/run"))
        .build();

    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    CodeRunResult codeRunResult = objectMapper.readValue(response.body(), CodeRunResult.class);
    assertThat(codeRunResult.getStdout()).hasSize(0);
    assertThat(codeRunResult.getStderr()).hasSize(1);
    System.out.println(codeRunResult);
  }

  @Test
  public void should_AcceptCodeAtBatchEndpoint() throws IOException, InterruptedException {

    List<CodeRunRequest> batch = TestUtils.prepareCodeRequestBatch();
    String body = objectMapper.writeValueAsString(batch);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .uri(URI.create("http://localhost:8003/batch"))
        .build();

    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    CodeRunResult[] codeRunResult = objectMapper.readValue(response.body(), CodeRunResult[].class);
    assertThat(codeRunResult[0].stdout()).hasSize(1);
    assertThat(codeRunResult[0].stderr()).hasSize(0);
    assertThat(codeRunResult[1].stdout()).hasSize(1);
    assertThat(codeRunResult[1].stderr()).hasSize(0);
    assertThat(codeRunResult[2].stdout()).hasSize(1);
    assertThat(codeRunResult[2].stderr()).hasSize(0);
  }

}
