package com.igloo.attendance.infra.render;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class EnvVariables {
    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(System.getenv("DATABASE_URL"));
        ds.setUsername(System.getenv("DATABASE_USER"));
        ds.setPassword(System.getenv("DATABASE_PASSWORD"));
        return ds;
    }
}
