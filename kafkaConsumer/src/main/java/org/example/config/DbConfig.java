package org.example.config;

import jakarta.persistence.EntityManagerFactory;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;

import java.sql.SQLException;

@Configuration
@EnableJpaRepositories(
        transactionManagerRef = "dbTransactionManager",
        basePackages = {"org.example.repo"}
)
public class DbConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9093");
    }



    @Bean("dbTransactionManager")
    public JpaTransactionManager dbTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
