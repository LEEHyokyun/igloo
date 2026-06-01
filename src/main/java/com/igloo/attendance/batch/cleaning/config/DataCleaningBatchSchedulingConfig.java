package com.igloo.attendance.batch.cleaning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableAsync
@EnableScheduling
@Configuration
public class DataCleaningBatchSchedulingConfig {

    //비동기 스레드 풀 환경 구성 : 단일 스레드 풀을 통해 순차적으로 데이터 클렌징 진행
    //Single Thread
    @Bean
    public ThreadPoolTaskScheduler dataCleaningBatchSchedulingExecutor() {

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1); //single thread
        threadPoolTaskScheduler.setThreadNamePrefix("dataCleaningBatchSchedulingExecutor-");
        threadPoolTaskScheduler.initialize();

        return threadPoolTaskScheduler;
    }

}
