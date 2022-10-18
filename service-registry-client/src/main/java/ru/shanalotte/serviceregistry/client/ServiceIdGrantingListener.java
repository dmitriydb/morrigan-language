package ru.shanalotte.serviceregistry.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
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

  @KafkaListener(topics = "service.replies")
  public void processIdGranting(String payload) {
    System.out.println(payload);
    try {
      GrantedIdRecord grantedIdRecord = objectMapper.readValue(payload, GrantedIdRecord.class);
      if (grantedIdRecord.getOriginalRecordId().equals(originalId)) {
        Long grantedId = grantedIdRecord.getGrantedId();
        System.out.println("GOT ID = " + grantedId);
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
          ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(heartbeatTopic, serviceHeartbeatRecord.getRecordId(), json);
          future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
              System.out.println(123);
              ex.printStackTrace();
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
              System.out.println(result.getRecordMetadata().partition() + ":" + result.getRecordMetadata().offset());
            }
          });
          System.out.println("Heartbeat sent");
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  public synchronized void setOriginalId(String id) {
    this.originalId = id;
  }

}
