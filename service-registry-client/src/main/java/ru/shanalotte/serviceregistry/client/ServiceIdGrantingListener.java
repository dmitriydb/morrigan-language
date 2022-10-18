package ru.shanalotte.serviceregistry.client;

import java.util.concurrent.atomic.AtomicInteger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.shanalotte.kafka.api.schemas.GrantedIdRecord;

@Component
public class ServiceIdGrantingListener {

  private String originalId;
  private ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(id = "IdGrantingListener", topics = "${service.replies}")
  public void processIdGranting(String payload) {
    try {
      GrantedIdRecord grantedIdRecord = objectMapper.readValue(payload, GrantedIdRecord.class);
      if (grantedIdRecord.getOriginalRecordId().equals(originalId)) {
        System.out.println("GOT ID " + grantedIdRecord.getGrantedId());
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  public synchronized void setOriginalId(String id) {
    this.originalId = id;
  }

}
