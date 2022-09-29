package ru.shanalotte.serviceregistry.service.worker;

import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;

@Service
public class ServiceAbandonWorkerImpl implements ServiceAbandonWorker {

  private ServicesDAO servicesDAO;

  public ServiceAbandonWorkerImpl(ServicesDAO servicesDAO) {
    this.servicesDAO = servicesDAO;
  }

  @Override
  public void abandonService(Long id) {
    servicesDAO.abandon(id);
  }
}
