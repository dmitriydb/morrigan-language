package ru.shanalotte.serviceregistry.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.shanalotte.serviceregistry.client")
public class Application {

  public static ApplicationContext initializeContext(final String[] args) {
    return new SpringApplicationBuilder(Application.class)
        .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
        .run(args);
  }


}
