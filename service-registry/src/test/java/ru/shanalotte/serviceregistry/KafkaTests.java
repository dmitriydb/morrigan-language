package ru.shanalotte.serviceregistry;

import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import ru.shanalotte.kafka.api.schemas.GrantedIdRecord;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;

@SpringBootTest
public class KafkaTests {

  @Value("${session.timeout.ms}")
  private String value;

  @Autowired
  private KafkaTestConsumer kafkaTestConsumer;

  @Autowired
  private KafkaTestConsumerWithReply kafkaTestConsumerWithReply;

  @Autowired
  private KafkaTemplate<String, String> template;

  @Autowired
  private ServicesDAO servicesDAO;

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
  public void should_WriteIntoDatabase() throws JsonProcessingException, InterruptedException {
    ServiceLaunchRecord serviceLaunchRecord = new ServiceLaunchRecord();
    serviceLaunchRecord.setReplyTo("service.grantedids.1234");
    serviceLaunchRecord.setHost("33");
    serviceLaunchRecord.setPort(44);
    serviceLaunchRecord.setName("name2");
    String json = new ObjectMapper().writeValueAsString(serviceLaunchRecord);
    template.send("service.launches", serviceLaunchRecord.getRecordId(), json);
    boolean messageConsumed = kafkaTestConsumerWithReply.getLatch().await(10, TimeUnit.SECONDS);
    assertTrue(messageConsumed);
    String payload = kafkaTestConsumerWithReply.getPayload().get();
    GrantedIdRecord grantedIdRecord = new ObjectMapper().readValue(payload, GrantedIdRecord.class);
    long id = grantedIdRecord.getGrantedId();
    MorriganPlatformService service = servicesDAO.findById(id);
    assertThat(service.getId()).isEqualTo(id);
    assertThat(service.getName()).isEqualTo("name2");
    assertThat(service.getHost()).isEqualTo("33");
    assertThat(service.getPort()).isEqualTo(44);
  }

}
