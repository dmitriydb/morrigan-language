package ru.shanalotte.serviceregistry.service.worker;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDao;

@Service
public class ServiceAbandonWorkerImpl implements ServiceAbandonWorker {

  private ServicesDao servicesDao;

  public ServiceAbandonWorkerImpl(ServicesDao servicesDao) {
    this.servicesDao = servicesDao;
  }

  @Override
  public void abandonService(Long id) {
    servicesDao.abandon(id);
  }
}
