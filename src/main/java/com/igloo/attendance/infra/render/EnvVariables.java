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

    /*
    * application-prod.yml을 읽을 수는 있으나 boot 시점이 아닌 객체 시점.
    * 따라서 환경변수 주입은 boot 시점에 이루어질 수 있도록 구성한다.
    * */

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(System.getenv("DATABASE_URL"));
        ds.setUsername(System.getenv("DATABASE_USER"));
        ds.setPassword(System.getenv("DATABASE_PASSWORD"));
        return ds;
    }
}
