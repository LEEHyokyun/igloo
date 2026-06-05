package com.igloo.attendance.batch.summarizing.process.handler;

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
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSummarzingBatchSchedulingHandler {

    private final JobLauncher jobLauncher;
    private final Job dataSummarizingBatchJob;
    private final AttendanceRepository attendanceRepository;
    private final Job dataCleaningBatchJob;

    //    @Scheduled(
//        fixedDelay = 5, //5sec
//        initialDelay = 5,
//        timeUnit = TimeUnit.SECONDS,
//        scheduler = "dataCleaningBatchSchedulingExecutor"
//    )
    /*
    * 매주 토요일 10시 10분
    * */
    @Scheduled(cron = "0 10 10 ? * SAT")
    public void dataSummarizingBatchScheduling() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        log.info("[DataSummarizingBatchSchedulingHandler.dataSummarizingBatchScheduling][INFO] DataCleaning Batch Process Started.");

        jobLauncher.run(dataSummarizingBatchJob, new JobParametersBuilder()
                .addString("date", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
                .toJobParameters()
        );

    }
}
