package ru.shanalotte.serviceregistry;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;
import ru.shanalotte.serviceregistry.service.monitoring.InactiveServicesMonitoringService;
import ru.shanalotte.serviceregistry.service.monitoring.InactiveServicesMonitoringServiceImpl;

@SpringBootTest
public class InactiveServicesMonitoringServiceTest {

  @Value("${session.timeout.ms}")
  private int sessionTimeout;

  @Autowired
  private InactiveServicesMonitoringService service;

  @Autowired
  private ServicesDAO servicesDAO;

  private String serviceName;
  private String serviceHost;
  MorriganServiceRegistration registration;

  @BeforeEach
  public void randomizeTestNames() {
    randomizeHost();
    serviceName = "name" + UUID.randomUUID();
    registration = new MorriganServiceRegistration(serviceName, serviceHost, 99);
  }

  private void randomizeHost() {
    serviceHost = "host" + UUID.randomUUID();
  }

  @Test
  public void test() throws InterruptedException {
    long id1 = servicesDAO.create(registration(serviceName, serviceHost + 1, 99));
    long id2 = servicesDAO.create(registration(serviceName, serviceHost + 2, 99));
    long id3 = servicesDAO.create(registration(serviceName, serviceHost + 3, 99));
    service.startMonitoring();
    Thread.sleep((long) (sessionTimeout * 1.3));
    var service1 = servicesDAO.findById(id1);
    var service2 = servicesDAO.findById(id2);
    var service3 = servicesDAO.findById(id3);
    assertThat(service1.getAbandonTs()).isNotNull();
    assertThat(service2.getAbandonTs()).isNotNull();
    assertThat(service3.getAbandonTs()).isNotNull();
    assertThat(service1.isActive()).isFalse();
    assertThat(service2.isActive()).isFalse();
    assertThat(service3.isActive()).isFalse();
  }

  private MorriganServiceRegistration registration(String name, String host, int port) {
    return new MorriganServiceRegistration(name, host, port);
  }

}
