package ru.shanalotte.serviceregistry;

import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;

@SpringBootTest
public class KafkaTests {

  @Autowired
  private KafkaTestConsumer kafkaTestConsumer;

  @Autowired
  private KafkaTemplate<String, String> template;

  @Test
  public void should_ReceiveALaunchMessage() throws JsonProcessingException, InterruptedException {
    ServiceLaunchRecord serviceLaunchRecord = new ServiceLaunchRecord();
    serviceLaunchRecord.setReplyTo("service.grantedids.444111222");
    serviceLaunchRecord.setHost("3");
    serviceLaunchRecord.setPort(4);
    serviceLaunchRecord.setName("name");
    String json = new ObjectMapper().writeValueAsString(serviceLaunchRecord);
    template.send("service.launches", serviceLaunchRecord.getRecordId(), json);
    boolean messageConsumed = kafkaTestConsumer.getLatch().await(10, TimeUnit.SECONDS);
    assertTrue(messageConsumed);
  }

  @Test
  public void should_LeftRecordInDatabase() throws JsonProcessingException, InterruptedException {

  }

  @Test
  public void heartbeats_Should_Work() {

  }

}
