package ru.shanalotte.serviceregistry.cloudconfig;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class CloudConfigTest {

  @Value("${test}")
  private String test;

  @Test
  public void testProperty() {
    assertThat(test).isEqualTo("hello");
  }

}
