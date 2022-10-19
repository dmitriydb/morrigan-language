package ru.shanalotte.serviceregistry.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.shanalotte.serviceregistry.client")
public class Application {

  public static ApplicationContext initializeContext(final String[] args) {
    return SpringApplication.run(Application.class, args);
  }


}
