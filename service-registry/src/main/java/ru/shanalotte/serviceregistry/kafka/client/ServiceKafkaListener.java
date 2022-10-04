package ru.shanalotte.serviceregistry.kafka.client;

import ru.shanalotte.kafka.api.schemas.ServiceHeartbeatRecord;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;

public interface ServiceKafkaListener {

  void processLaunchRecord(String record);

  void processHeartbeatRecord(String record);

}
