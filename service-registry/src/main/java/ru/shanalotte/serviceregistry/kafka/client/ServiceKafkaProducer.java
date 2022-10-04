package ru.shanalotte.serviceregistry.kafka.client;

import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;

public interface ServiceKafkaProducer {

  void sendGrantedId(ServiceLaunchRecord originalRecord, Long grantedId);

}
