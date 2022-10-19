package ru.shanalotte.serviceregistry.service;

import java.util.UUID;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.shanalotte.serviceregistry.dao.ServicesDao;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceMapper;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceUptimeMapper;
import ru.shanalotte.serviceregistry.service.monitoring.InactiveServicesMonitoringService;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource("classpath:fake-cloud.properties")
public class InactiveServicesMonitoringServiceTestIT {

  @Autowired
  private ServicesDao dao;

  @Container
  protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:11")
          .withInitScript("schema.sql");

  @Value("${session.timeout.ms}")
  private int sessionTimeout;

  @Autowired
  private InactiveServicesMonitoringService service;

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
    long id1 = dao.create(registration(serviceName, serviceHost + 1, 99));
    long id2 = dao.create(registration(serviceName, serviceHost + 2, 99));
    long id3 = dao.create(registration(serviceName, serviceHost + 3, 99));
    service.startMonitoring();
    Thread.sleep((long) (sessionTimeout * 1.3));
    var service1 = dao.findById(id1);
    var service2 = dao.findById(id2);
    var service3 = dao.findById(id3);
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
