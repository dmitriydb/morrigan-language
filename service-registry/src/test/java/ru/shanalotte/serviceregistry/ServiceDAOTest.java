package ru.shanalotte.serviceregistry;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformServiceUptime;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;

@SpringBootTest
@ActiveProfiles("dev")
public class ServiceDAOTest {

  @Autowired
  ServicesDAO dao;

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
  public void should_create() throws IOException {
    dao.create(registration);
  }

  @Test
  public void should_createDifferentNumbers() {
    long id1 = dao.create(registration);
    randomizeHost();
    registration = new MorriganServiceRegistration(serviceName, serviceHost, 99);
    long id2 = dao.create(registration);
    MorriganPlatformService service1 = dao.findById(id1);
    MorriganPlatformService service2 = dao.findById(id2);
    assertThat(service2.getId()).isEqualTo(service1.getId() + 1);
  }

  @Test
  public void dao_createMethod_shouldReturnId() {
    long id = dao.create(registration);
    var serviceAfterCreation = dao.findById(id);
    assertThat(serviceAfterCreation.getHost()).isEqualTo(registration.getHost());
    assertThat(serviceAfterCreation.getName()).isEqualTo(registration.getName());
    assertThat(serviceAfterCreation.getPort()).isEqualTo(registration.getPort());
    assertThat(serviceAfterCreation.getId()).isEqualTo(id);
  }

  @Test
  public void should_SetNumber1ByDefault() {
    long id = dao.create(registration);
    var serviceAfterCreation = dao.findById(id);
    assertThat(serviceAfterCreation.getNumber()).isEqualTo(1);
  }

  @Test
  public void service_ShouldBeActiveByDefault() {
    long id = dao.create(registration);
    var serviceAfterCreation = dao.findById(id);
    assertThat(serviceAfterCreation.isActive()).isTrue();
  }

  @Test
  public void abandonedServiceShouldBeInactive() {
    long id = dao.create(registration);
    dao.abandon(id);
    var serviceAfterCreation = dao.findById(id);
    assertThat(serviceAfterCreation.isActive()).isFalse();
    assertThat(serviceAfterCreation.getAbandonTs()).isNotNull();
  }

  @Test
  public void shouldFindOnlyActiveServices() {
    var entries = IntStream.rangeClosed(1, 6).mapToObj(i -> registration(serviceName, serviceHost + i, 912)).collect(Collectors.toList());
    var ids = entries.stream().map(entry -> dao.create(entry)).collect(Collectors.toList());
    dao.abandon(ids.get(0));
    dao.abandon(ids.get(2));
    dao.abandon(ids.get(4));
    var foundServices = dao.findActiveServicesByName(serviceName);
    assertThat(foundServices.size()).isEqualTo(3);
    var foundServicesIds = foundServices.stream().map(MorriganPlatformService::getId).collect(Collectors.toList());
    var inactiveServiceIds = List.of(ids.get(0), ids.get(2), ids.get(4));
    assertThat(foundServicesIds).doesNotContainAnyElementsOf(inactiveServiceIds);
  }

  @Test
  public void should_createUptimeAfterCreationService() {
    var currentTs = LocalDateTime.now();
    long id = dao.create(registration);
    MorriganPlatformServiceUptime uptime = dao.getUptime(id);
    assertThat(uptime.getServiceId()).isEqualTo(id);
    assertThat(uptime.getLag()).isEqualTo(0);
    assertThat(uptime.getUptime()).isEqualTo(0);
    assertThat(uptime.getLastHeartbeatTs()).isAfter(currentTs);
  }

  @Test
  public void shouldRefreshUptime() {
    var currentTs = LocalDateTime.now();
    long id = dao.create(registration);
    var uptimeBefore = dao.getUptime(id);
    var tsBefore = uptimeBefore.getLastHeartbeatTs();
    dao.refreshUptime(id);
    MorriganPlatformServiceUptime uptime = dao.getUptime(id);
    assertThat(uptime.getServiceId()).isEqualTo(id);
    assertThat(uptime.getLag()).isNotEqualTo(0);
    assertThat(uptime.getUptime()).isNotEqualTo(0);
    assertThat(uptime.getLastHeartbeatTs()).isAfter(currentTs);
    assertThat(uptime.getLastHeartbeatTs()).isAfter(tsBefore);
  }

  @Test
  public void uptimeAndLagCalculationTest() throws InterruptedException {
    long id = dao.create(registration);
    Thread.sleep(2000);
    dao.refreshUptime(id);
    var uptime = dao.getUptime(id);
    assertThat(uptime.getUptime()).isLessThan(2200);
    assertThat(uptime.getLag()).isLessThan(2200);
  }

  @Test
  public void uptimeSimulation() throws InterruptedException {
    long id = dao.create(registration);
    for (int i = 0; i < 20; i++) {
      Thread.sleep(150);
      dao.refreshUptime(id);
      var uptime = dao.getUptime(id);
      System.out.println(uptime);
    }
  }

  @Test
  public void should_makeServiceActiveAfterHeartbeat() {
    long id = dao.create(registration);
    dao.abandon(id);
    var service = dao.findById(id);
    assertThat(service.isActive()).isFalse();
    dao.refreshUptime(id);
    service = dao.findById(id);
    assertThat(service.isActive()).isTrue();
  }

  private MorriganServiceRegistration registration(String name, String host, int port) {
    return new MorriganServiceRegistration(name, host, port);
  }




}
