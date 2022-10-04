package ru.shanalotte.serviceregistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;

@SpringBootTest
public class KafkaTests {

  @Autowired
  private KafkaTemplate<String, String> template;

  @Test
  public void should_ReceiveALaunchMessage() throws JsonProcessingException {
    ServiceLaunchRecord serviceLaunchRecord = new ServiceLaunchRecord();
    serviceLaunchRecord.setReplyTo("service.grantedids.444111222");
    serviceLaunchRecord.setHost("3");
    serviceLaunchRecord.setPort(4);
    serviceLaunchRecord.setName("name");
    String json = new ObjectMapper().writeValueAsString(serviceLaunchRecord);
    template.send("service.launches", serviceLaunchRecord.getRecordId(), json);
  }

}
