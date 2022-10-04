package ru.shanalotte.serviceregistry.kafka.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.shanalotte.kafka.api.schemas.GrantedIdRecord;
import ru.shanalotte.kafka.api.schemas.ServiceHeartbeatRecord;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;
import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;
import ru.shanalotte.serviceregistry.service.listener.ServiceHeartbeatListener;
import ru.shanalotte.serviceregistry.service.listener.ServiceRegistrationListener;

@Service
public class SpringBootServiceKafkaListener implements ServiceKafkaListener {

  private final ObjectMapper objectMapper;
  private final ServiceRegistrationListener serviceRegistrationListener;
  private final ServiceHeartbeatListener serviceHeartbeatListener;
  private final KafkaTemplate<String, String> template;

  public SpringBootServiceKafkaListener(ObjectMapper objectMapper, ServiceRegistrationListener serviceRegistrationListener, ServiceHeartbeatListener serviceHeartbeatListener, KafkaTemplate<String, String> template) {
    this.objectMapper = objectMapper;
    this.serviceRegistrationListener = serviceRegistrationListener;
    this.serviceHeartbeatListener = serviceHeartbeatListener;
    this.template = template;
  }

  @Override
  @KafkaListener(id = "launchRecordsListener", topics = "service.launches")
  public void processLaunchRecord(String record) {
    try {
      ServiceLaunchRecord dto = objectMapper.readValue(record, ServiceLaunchRecord.class);
      MorriganServiceRegistration registration = MorriganServiceRegistration.of(dto);
      long id = serviceRegistrationListener.processRegistration(registration);
      GrantedIdRecord grantedIdRecord = new GrantedIdRecord();
      grantedIdRecord.setGrantedId(id);
      grantedIdRecord.setOriginalRecordId(dto.getRecordId());
      String replyJson = objectMapper.writeValueAsString(grantedIdRecord);
      template.send(dto.getReplyTo(), grantedIdRecord.getRecordId(), replyJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  @Override
  @KafkaListener(id = "heartbeatListener", topics = "service.heartbeats")
  public void processHeartbeatRecord(String record) {
    try {
      ServiceHeartbeatRecord dto = objectMapper.readValue(record, ServiceHeartbeatRecord.class);
      MorriganServiceHeartbeat heartbeat = MorriganServiceHeartbeat.of(dto);
      serviceHeartbeatListener.processHeartbeat(heartbeat);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
