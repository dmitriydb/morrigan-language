package ru.shanalotte.coderun.loadbalancer;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.shanalotte.coderun.loadbalancer.grpc.CoderunServiceImpl;
import ru.shanalotte.coderun.loadbalancer.service.runner.CodeRunRequestRunner;

import java.io.IOException;

@SpringBootApplication
@ComponentScan("ru.shanalotte.coderun.loadbalancer")
@EnableScheduling
public class CoderunLoadBalancerLauncher implements CommandLineRunner {

  @Autowired
  private CoderunServiceImpl coderunService;

  @Value("${grpc.service.port")

  public static void main(String[] args) throws IOException, InterruptedException {
    SpringApplication.run(CoderunLoadBalancerLauncher.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Server server = ServerBuilder
        .forPort(8866)
        .addService(coderunService).build();
    server.start();
    server.awaitTermination();
  }
}
