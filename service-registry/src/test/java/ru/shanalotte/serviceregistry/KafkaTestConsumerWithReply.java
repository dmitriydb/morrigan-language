package ru.shanalotte.serviceregistry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class KafkaTestConsumerWithReply {

  @Getter
  private final AtomicReference<String> payload = new AtomicReference<>();

  @Getter
  private CountDownLatch latch = new CountDownLatch(1);

  @KafkaListener(id = "test-consumer-with-reply", topics = "service.grantedids.1234", properties = "auto.offset.reset=earliest")
  public void receive(ConsumerRecord<String, String> consumerRecord) {
    String payload = consumerRecord.value();
    this.payload.set(payload);
    latch.countDown();
  }

  public void resetLatch() {
    latch = new CountDownLatch(1);
  }

}