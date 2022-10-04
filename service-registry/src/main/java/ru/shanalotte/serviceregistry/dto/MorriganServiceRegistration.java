package ru.shanalotte.serviceregistry.dto;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;

@Value
@ToString
@RequiredArgsConstructor
public class MorriganServiceRegistration {
  String name;
  String host;
  int port;

  public static MorriganServiceRegistration of(ServiceLaunchRecord dto) {
    return new MorriganServiceRegistration(dto.getName(), dto.getHost(), dto.getPort());
  }
}
