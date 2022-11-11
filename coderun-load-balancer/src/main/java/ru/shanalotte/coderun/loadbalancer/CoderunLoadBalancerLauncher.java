package ru.shanalotte.coderun.loadbalancer;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.shanalotte.coderun.loadbalancer.grpc.CoderunServiceImpl;
import ru.shanalotte.serviceregistry.client.Application;
import ru.shanalotte.serviceregistry.client.ServiceRegistryClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

@SpringBootApplication
@ComponentScan("ru.shanalotte.coderun.loadbalancer")
@EnableScheduling
public class CoderunLoadBalancerLauncher implements CommandLineRunner {

  @Autowired
  private CoderunServiceImpl coderunService;

  @Value("${grpc.service.port}")
  private int grpcServicePort;

  @Value("${spring.application.name}")
  private String applicationName;
  @Autowired
  private Environment env;

  public static void main(String[] args) throws IOException, InterruptedException {
    SpringApplication.run(CoderunLoadBalancerLauncher.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    ServiceRegistryClient serviceRegistryClient =
        Application.initializeContext(args).getBean(ServiceRegistryClient.class);
    serviceRegistryClient.startWorking(
        InetAddress.getLocalHost().getHostName(), applicationName, grpcServicePort);

    Server server = ServerBuilder
        .forPort(grpcServicePort)
        .addService(coderunService).build();
    server.start();
    server.awaitTermination();
  }
}
