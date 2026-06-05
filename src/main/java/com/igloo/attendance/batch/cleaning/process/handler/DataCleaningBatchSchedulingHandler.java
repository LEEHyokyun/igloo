package com.igloo.attendance.batch.cleaning.process.handler;

import com.igloo.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataCleaningBatchSchedulingHandler {

    private final JobLauncher jobLauncher;
    private final Job dataCleaningBatchJob;
    private final AttendanceRepository attendanceRepository;

//    @Scheduled(
//        fixedDelay = 5, //5sec
//        initialDelay = 5,
//        timeUnit = TimeUnit.SECONDS,
//        scheduler = "dataCleaningBatchSchedulingExecutor"
//    )
    /*
    * 매주 일요일 13시
    * */
    @Scheduled(cron = "0 0 13 ? * SUN")
    public void dataCleaningBatchScheduling() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        log.info("[DataCleaningBatchSchedulingHandler.dataCleaningBatchScheduling][INFO] DataCleaning Batch Process Started.");

        jobLauncher.run(dataCleaningBatchJob, new JobParametersBuilder()
                        .addString("date", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
                        .toJobParameters()
        );

    }
}
