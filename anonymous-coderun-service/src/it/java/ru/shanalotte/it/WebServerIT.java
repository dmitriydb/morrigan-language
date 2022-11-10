package ru.shanalotte.it;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.shanalotte.coderun.api.CodeRunResult;
import ru.shanalotte.coderun.CommonProperties;
import ru.shanalotte.coderun.TestUtils;
import ru.shanalotte.coderun.WebServer;
import ru.shanalotte.coderun.api.CodeRunRequest;

@Testcontainers
public class WebServerIT {

  private ObjectMapper objectMapper = new ObjectMapper();
  private WebServer webServer;
  private HttpClient httpClient;
  private LongAdder correctResponses = new LongAdder();

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

  class TaskRunner extends Thread {

    private LongAdder correctResponses;
    private AtomicLong threadNumber;
    private CountDownLatch countDownLatch;

    public TaskRunner(LongAdder correctResponses, AtomicLong threadNumber, CountDownLatch countDownLatch) {
      this.correctResponses = correctResponses;
      this.threadNumber = threadNumber;
      this.countDownLatch = countDownLatch;
    }

    public void run() {
      ObjectMapper localObjectMapper = new ObjectMapper();
      HttpClient localHttpClient = HttpClient.newBuilder()
          .build();
      CodeRunRequest codeRunRequest = TestUtils.randomCodeRequest("User" + threadNumber.incrementAndGet());
      String body = null;
      try {
        body = localObjectMapper.writeValueAsString(codeRunRequest);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      HttpRequest httpRequest = HttpRequest.newBuilder()
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .uri(URI.create("http://localhost:8003/run"))
          .build();

      HttpResponse<String> response = null;
      try {
        response = localHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

      CodeRunResult codeRunResult = null;
      try {
        codeRunResult = localObjectMapper.readValue(response.body(), CodeRunResult.class);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      try {

        assertThat(codeRunResult.getStdout()).hasSize(1);
        assertThat(body).contains(codeRunResult.getStdout().get(0));
        correctResponses.add(1L);
      }
      finally {
        countDownLatch.countDown();
      }
    }
  }

  @Test
  public void multithreadedTest() throws InterruptedException {
    final int TEST_THREADS = 100;
    final CountDownLatch countDownLatch = new CountDownLatch(TEST_THREADS);
    final AtomicLong threadNumber = new AtomicLong(0);
    correctResponses = new LongAdder();
    for (int i = 0; i < TEST_THREADS; i++) {
      new TaskRunner(correctResponses, threadNumber, countDownLatch).start();
    }
    countDownLatch.await();
    assertThat(correctResponses.intValue()).isEqualTo(TEST_THREADS);
  }

}
