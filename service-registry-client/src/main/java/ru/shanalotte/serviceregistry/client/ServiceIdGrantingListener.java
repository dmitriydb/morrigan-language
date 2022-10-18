package ru.shanalotte.serviceregistry.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.shanalotte.kafka.api.schemas.GrantedIdRecord;
import ru.shanalotte.kafka.api.schemas.ServiceHeartbeatRecord;

@Component
public class ServiceIdGrantingListener {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final KafkaTemplate<String, String> kafkaTemplate;
  private String originalId;
  @Value("${heartbeat.timeout.ms}")
  private long heartbeatTimeoutMs;

  @Value("${kafka.topic.heartbeat}")
  private String heartbeatTopic;


  public ServiceIdGrantingListener(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(id = "IdGrantingListener", topics = "${${kafka.topic.replyto}}")
  public void processIdGranting(String payload) {
    try {
      GrantedIdRecord grantedIdRecord = objectMapper.readValue(payload, GrantedIdRecord.class);
      if (grantedIdRecord.getOriginalRecordId().equals(originalId)) {
        Long grantedId = grantedIdRecord.getGrantedId();
        startSendingHeartbeats(grantedId);
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  private void startSendingHeartbeats(Long grantedId) {
    while (true) {
      System.out.println("Sleeping for " + heartbeatTimeoutMs);
      try {
        Thread.sleep(heartbeatTimeoutMs);
        ServiceHeartbeatRecord serviceHeartbeatRecord = new ServiceHeartbeatRecord();
        serviceHeartbeatRecord.setServiceId(grantedId);
        try {
          String json = objectMapper.writeValueAsString(serviceHeartbeatRecord);
          kafkaTemplate.send(heartbeatTopic, serviceHeartbeatRecord.getRecordId(), json);
          System.out.println("Heartbeat sent");
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public synchronized void setOriginalId(String id) {
    this.originalId = id;
  }

}
