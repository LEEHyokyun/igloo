package com.igloo.attendance.infra.render;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class EnvVariables {

    @PostConstruct
    public void check() {
        log.info("spring.batch.job.enabled={}",
                System.getProperty("spring.batch.job.enabled")
        );
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(System.getenv("DATABASE_URL"));
        ds.setUsername(System.getenv("DATABASE_USER"));
        ds.setPassword(System.getenv("DATABASE_PASSWORD"));
        return ds;
    }
}
