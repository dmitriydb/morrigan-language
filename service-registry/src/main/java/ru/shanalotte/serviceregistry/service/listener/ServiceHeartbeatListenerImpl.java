package ru.shanalotte.serviceregistry.service.listener;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDao;
import ru.shanalotte.serviceregistry.dto.MorriganServiceHeartbeat;

@Service
public class ServiceHeartbeatListenerImpl implements ServiceHeartbeatListener {

  private ServicesDao servicesDao;

  public ServiceHeartbeatListenerImpl(ServicesDao servicesDao) {
    this.servicesDao = servicesDao;
  }

  @Override
  public void processHeartbeat(MorriganServiceHeartbeat dto) {
    servicesDao.refreshUptime(dto.getId());
  }
}
