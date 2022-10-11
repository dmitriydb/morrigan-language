package ru.shanalotte.serviceregistry.cloudconfig;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:fake-cloud.properties")
public class DisabledSpringCloudConfigTest {

  @Value("${session.timeout.ms}")
  private int sessionTimeout;

  @Value("${test}")
  private String test;

  @Test
  public void shouldWork() {
      assertThat(test).isEqualTo("abc");
      assertThat(sessionTimeout).isEqualTo(1234);
  }
}
