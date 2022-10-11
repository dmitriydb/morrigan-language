package ru.shanalotte.serviceregistry;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.shanalotte.kafka.api.schemas.GrantedIdRecord;
import ru.shanalotte.kafka.api.schemas.ServiceLaunchRecord;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformService;
import ru.shanalotte.serviceregistry.domain.MorriganPlatformServiceUptime;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceMapper;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceUptimeMapper;

@SpringBootTest
@ActiveProfiles("dev")
@Testcontainers
public class KafkaAndDaoTest {

  @Autowired
  ServicesDAO dao;

  @Value("${session.timeout.ms}")
  private String value;

  @Autowired
  private KafkaTestConsumer kafkaTestConsumer;

  @Autowired
  private KafkaTestConsumerWithReply kafkaTestConsumerWithReply;

  @Autowired
  private KafkaTemplate<String, String> template;

  private String serviceName;
  private String serviceHost;
  MorriganServiceRegistration registration;

  @Container
  protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:11")
          .withInitScript("schema.sql");

  @Container
  protected static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
  }

  protected DataSource getDataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
    dataSource.setUser(POSTGRESQL_CONTAINER.getUsername());
    dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
    return dataSource;
  }

  @BeforeEach
  protected void prepareDatasource() {
    DataSource dataSource = getDataSource();
    TransactionFactory trxFactory = new JdbcTransactionFactory();
    Environment env = new Environment("dev", trxFactory, dataSource);
    Configuration config = new Configuration(env);
    TypeAliasRegistry aliases = config.getTypeAliasRegistry();
    config.addMapper(ServiceMapper.class);
    config.addMapper(ServiceUptimeMapper.class);
    SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(config);
    dao.setSqlSessionFactory(factory);
  }

  @Test
  public void should_ReceiveALaunchMessage() throws JsonProcessingException, InterruptedException {
    ServiceLaunchRecord serviceLaunchRecord = new ServiceLaunchRecord();
    serviceLaunchRecord.setReplyTo("service.grantedids.444111222");
    serviceLaunchRecord.setHost("3");
    serviceLaunchRecord.setPort(4);
    serviceLaunchRecord.setName("name");
    String json = new ObjectMapper().writeValueAsString(serviceLaunchRecord);
    template.send("service.launches", serviceLaunchRecord.getRecordId(), json);
    boolean messageConsumed = kafkaTestConsumer.getLatch().await(10, TimeUnit.SECONDS);
    assertTrue(messageConsumed);
  }

  @Test
  public void should_WriteIntoDatabase() throws JsonProcessingException, InterruptedException {
    ServiceLaunchRecord serviceLaunchRecord = new ServiceLaunchRecord();
    serviceLaunchRecord.setReplyTo("service.grantedids.1234");
    serviceLaunchRecord.setHost("33");
    serviceLaunchRecord.setPort(44);
    serviceLaunchRecord.setName("name2");
    String json = new ObjectMapper().writeValueAsString(serviceLaunchRecord);
    template.send("service.launches", serviceLaunchRecord.getRecordId(), json);
    boolean messageConsumed = kafkaTestConsumerWithReply.getLatch().await(10, TimeUnit.SECONDS);
    assertTrue(messageConsumed);
    String payload = kafkaTestConsumerWithReply.getPayload().get();
    GrantedIdRecord grantedIdRecord = new ObjectMapper().readValue(payload, GrantedIdRecord.class);
    long id = grantedIdRecord.getGrantedId();
    MorriganPlatformService service = dao.findById(id);
    assertThat(service.getId()).isEqualTo(id);
    assertThat(service.getName()).isEqualTo("name2");
    assertThat(service.getHost()).isEqualTo("33");
    assertThat(service.getPort()).isEqualTo(44);
  }

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
    long id = dao.create(registration(host(), name(), port()));
    MorriganPlatformServiceUptime uptime = dao.getUptime(id);
    assertThat(uptime.getServiceId()).isEqualTo(id);
    assertThat(uptime.getLag()).isEqualTo(0);
    assertThat(uptime.getUptime()).isEqualTo(0);
    assertThat(Duration.between(currentTs, uptime.getLastHeartbeatTs()).toSeconds()).isLessThan(1);
  }

  @Test
  public void shouldRefreshUptime() {
    var currentTs = LocalDateTime.now();
    long id = dao.create(registration(host(), name(), port()));
    var uptimeBefore = dao.getUptime(id);
    var tsBefore = uptimeBefore.getLastHeartbeatTs();
    dao.refreshUptime(id);
    MorriganPlatformServiceUptime uptime = dao.getUptime(id);
    assertThat(uptime.getServiceId()).isEqualTo(id);
    assertThat(uptime.getLag()).isNotEqualTo(0);
    assertThat(uptime.getUptime()).isNotEqualTo(0);
    assertThat(Duration.between(currentTs, uptime.getLastHeartbeatTs()).toSeconds()).isLessThan(1);
    assertThat(Duration.between(tsBefore, uptime.getLastHeartbeatTs()).toSeconds()).isLessThan(1);
  }

  @Test
  public void uptimeAndLagCalculationTest() throws InterruptedException {
    long id = dao.create(registration);
    Thread.sleep(2000);
    dao.refreshUptime(id);
    var uptime = dao.getUptime(id);
    assertThat(uptime.getUptime()).isLessThan(2500);
    assertThat(uptime.getLag()).isLessThan(2500);
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

  private String name() {
    return "name" + LocalDateTime.now();
  }

  private String host() {
    return "host" + LocalDateTime.now();
  }

  private int port() {
    return ThreadLocalRandom.current().nextInt(65535);
  }


}
