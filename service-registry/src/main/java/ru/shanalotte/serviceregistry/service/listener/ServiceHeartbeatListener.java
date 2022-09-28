package ru.shanalotte.serviceregistry.service.listener;

import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;

public interface ServiceHeartbeatListener {
  void processHeartbeat(MorriganServiceHeartbeat dto);
}
