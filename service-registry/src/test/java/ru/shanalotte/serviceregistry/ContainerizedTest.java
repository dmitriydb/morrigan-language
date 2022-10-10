package ru.shanalotte.serviceregistry;

import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.shanalotte.serviceregistry.dao.ServicesDAO;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceMapper;
import ru.shanalotte.serviceregistry.mybatis.mappers.ServiceUptimeMapper;

@SpringBootTest
@ActiveProfiles("dev")
@Testcontainers
public class ContainerizedTest {

  @Autowired
  ServicesDAO dao;

  @Container
  protected final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:11")
          .withInitScript("schema.sql");

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

}
