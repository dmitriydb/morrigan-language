package ru.shanalotte.serviceregistry.service.monitoring;

import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shanalotte.serviceregistry.dao.ServicesDao;
import ru.shanalotte.serviceregistry.service.worker.ServiceAbandonWorker;

@Service
public class InactiveServicesMonitoringServiceImpl implements InactiveServicesMonitoringService {

  @Value("${session.timeout.ms}")
  private int sessionTimeout;

  private final ServicesDao servicesDao;
  private final ServiceAbandonWorker serviceAbandonWorker;

  public InactiveServicesMonitoringServiceImpl(ServicesDao servicesDao,
                                               ServiceAbandonWorker serviceAbandonWorker) {
    this.servicesDao = servicesDao;
    this.serviceAbandonWorker = serviceAbandonWorker;
  }

  @Override
  public void startMonitoring() {
    Runnable r = () -> {
      try {
        while (true) {
          var currentMs = LocalDateTime.now()
              .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
          var activeServices = servicesDao.findAllActive();
          for (var service : activeServices) {
            var uptime = servicesDao.getUptime(service.getId());
            var serviceLastHeartbeatMs = uptime
                .getLastHeartbeatTs()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
            if (currentMs - serviceLastHeartbeatMs > sessionTimeout) {
              System.out.println("Abandoning " + service.getId());
              serviceAbandonWorker.abandonService(service.getId());
            }
          }
          Thread.sleep(sessionTimeout);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
    Thread workerThread = new Thread(r);
    workerThread.setDaemon(true);
    workerThread.start();
  }

}
