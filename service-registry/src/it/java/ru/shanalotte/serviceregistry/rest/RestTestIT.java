package ru.shanalotte.serviceregistry.rest;


import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import ru.shanalotte.serviceregistry.KafkaTestConsumer;
import ru.shanalotte.serviceregistry.KafkaTestConsumerWithReply;
import ru.shanalotte.serviceregistry.api.KnownService;
import ru.shanalotte.serviceregistry.dao.ServicesDao;
import ru.shanalotte.serviceregistry.dto.MorriganServiceRegistration;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceMapper;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceUptimeMapper;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:fake-cloud.properties")
@Testcontainers
@AutoConfigureMockMvc
public class RestTestIT {

  @Autowired
  ServicesDao dao;

  @Value("${session.timeout.ms}")
  private String value;

  @Autowired
  private KafkaTestConsumer kafkaTestConsumer;

  @Autowired
  private KafkaTestConsumerWithReply kafkaTestConsumerWithReply;

  @Autowired
  private KafkaTemplate<String, String> template;

  @Autowired
  private MockMvc mockMvc;

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

  @BeforeEach
  public void randomizeTestNames() {
    randomizeHost();
    serviceName = "name" + UUID.randomUUID();
    registration = new MorriganServiceRegistration(serviceName, serviceHost, 99);
  }

  @Test
  public void service_shouldBeAccessibleViaRest_afterCreation() throws Exception {
    long id = dao.create(registration);
    String content = mockMvc.perform(MockMvcRequestBuilders.get("/service").param("name", registration.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(containsString(registration.getName())))
        .andExpect(MockMvcResultMatchers.content().string(containsString(registration.getHost())))
        .andExpect(MockMvcResultMatchers.content().string(containsString(String.valueOf(registration.getPort()))))
        .andReturn().getResponse().getContentAsString();
    dao.abandon(id);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/service").param("name", registration.getName()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    KnownService[] knownServices = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), KnownService[].class);
    assertThat(knownServices.length).isZero();
  }

  private void randomizeHost() {
    serviceHost = "host" + UUID.randomUUID();
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
