package ru.shanalotte.serviceregistry.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;

@Component
@Scope("prototype")
public class ServiceRegistrationServiceImpl implements ServiceRegistrationService {

  private final ServiceIdGrantingListener serviceIdGrantingListener;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();
  @Value("${kafka.topic.launch}")
  private String launchTopic;
  @Value("${kafka.topic.replyto}")
  private String replyToTopic;

  public ServiceRegistrationServiceImpl(ServiceIdGrantingListener serviceIdGrantingListener, KafkaTemplate<String, String> kafkaTemplate) {
    this.serviceIdGrantingListener = serviceIdGrantingListener;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void registerService(String host, String serviceName, int port) {
    ServiceLaunchRecord serviceLaunchRecord = new ServiceLaunchRecord();
    serviceLaunchRecord.setName(serviceName);
    serviceLaunchRecord.setHost(host);
    serviceLaunchRecord.setPort(port);
    serviceLaunchRecord.setReplyTo(replyToTopic);
    try {
      String json = objectMapper.writeValueAsString(serviceLaunchRecord);
      serviceIdGrantingListener.setOriginalId(serviceLaunchRecord.getRecordId());
      kafkaTemplate.send(launchTopic, serviceLaunchRecord.getRecordId(), json);
      System.out.println("Sent");
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

}
