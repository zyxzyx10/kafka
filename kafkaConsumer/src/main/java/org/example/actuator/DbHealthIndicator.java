package org.example.actuator;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class DbHealthIndicator implements HealthIndicator {
    private final HikariDataSource dataSource;

    public DbHealthIndicator(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try {
            if(dataSource.getConnection().isValid(1)){
                return Health.up().withDetail("DB","Up").build();
            } else {
                return Health.down().withDetail("DB","Down").build();
            }
        } catch (SQLException e) {
            return Health.down(e).withDetail("DB","Down").build();
        }
    }
}
