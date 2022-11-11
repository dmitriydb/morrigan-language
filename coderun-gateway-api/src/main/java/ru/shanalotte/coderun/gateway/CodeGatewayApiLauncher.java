package ru.shanalotte.coderun.gateway;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.shanalotte.serviceregistry.client.Application;
import ru.shanalotte.serviceregistry.client.ServiceRegistryClient;

import java.net.InetAddress;

@EnableScheduling
@SpringBootApplication
@ComponentScan("ru.shanalotte.coderun.gateway")
public class CodeGatewayApiLauncher implements CommandLineRunner {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${server.port}")
  private int serverPort;

  public static void main(String[] args) {
    SpringApplication.run(CodeGatewayApiLauncher.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    ServiceRegistryClient serviceRegistryClient =
        Application.initializeContext(args).getBean(ServiceRegistryClient.class);
    serviceRegistryClient.startWorking(
        InetAddress.getLocalHost().getHostName(), applicationName, serverPort);
  }
}
