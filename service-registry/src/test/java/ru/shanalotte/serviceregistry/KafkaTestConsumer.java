package ru.shanalotte.serviceregistry;

import java.util.concurrent.CountDownLatch;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@Profile("test")
public class KafkaTestConsumer {

  @Getter
  private CountDownLatch latch = new CountDownLatch(1);

  @KafkaListener(id = "test-consumer", topics = "service.grantedids.444111222", properties = "auto.offset.reset=earliest")
  public void receive(ConsumerRecord<?, ?> consumerRecord) {
    String payload = consumerRecord.toString();
    System.out.println("PAYLOAD = " + payload);
    latch.countDown();
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }

}