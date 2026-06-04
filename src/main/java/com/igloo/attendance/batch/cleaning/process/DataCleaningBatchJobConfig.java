package com.igloo.attendance.batch.cleaning.process;

import com.igloo.attendance.repository.AttendanceRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataCleaningBatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    private final AttendanceRepository attendanceRepository;

    private final String BATCH_JOB_NAME = "dataCleaningBatchJob";
    private final String BATCH_STEP_NAME = "dataCleaningBatchStep";
    private final String BATCH_STEP_READER = "dataCleaningBatchStepReader";
    private final String BATCH_READER_PARAMETER_ATTENDANCE_STATUS_CANT_ATTENDANCE = "N";
    private final String BATCH_READER_PARAMETER_ATTENDANCE_STATUS_CAN_ATTENDANCE = "Y";

    @Bean
    public Job dataCleaningBatchJob() {
        return new JobBuilder(BATCH_JOB_NAME, jobRepository)
                .start(dataCleaningBatchStep())
                .build();
    }

    @Bean
    public Step dataCleaningBatchStep() {
        return new StepBuilder(BATCH_STEP_NAME, jobRepository)
                .<DataCleaningBatchRecord, DataCleaningBatchRecord>chunk(5, transactionManager)
                .reader(dataCleaningBatchReader(dataSource))
                .writer(dataCleaningBatchWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<DataCleaningBatchRecord> dataCleaningBatchReader(DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<DataCleaningBatchRecord>()
                .name(BATCH_STEP_READER)
                .dataSource(dataSource)
                .queryProvider(pagingQueryProvider(dataSource))
                .parameterValues(Map.of(
                        "attendanceStatus1", BATCH_READER_PARAMETER_ATTENDANCE_STATUS_CAN_ATTENDANCE,
                        "attendanceStatus2", BATCH_READER_PARAMETER_ATTENDANCE_STATUS_CANT_ATTENDANCE
                        //"createdAt", LocalDateTime.now().minusDays(1))
                        )
                )
                .pageSize(5)
                .rowMapper(new DataClassRowMapper<>(DataCleaningBatchRecord.class))
                .build();
    }

    private PagingQueryProvider pagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean queryProviderFactory = new SqlPagingQueryProviderFactoryBean();

        //native query
        //with parameter & dataSource
        queryProviderFactory.setDataSource(dataSource);
        queryProviderFactory.setSelectClause("SELECT attendance_id, attendance_name, attendance_status, attendance_time, reason, created_at, updated_at");
        queryProviderFactory.setFromClause("FROM attendance");
        queryProviderFactory.setWhereClause("WHERE attendance_status IN (:attendanceStatus1, :attendanceStatus2)");
        queryProviderFactory.setSortKeys(Map.of("created_at", Order.ASCENDING));

        try {

            return queryProviderFactory.getObject();

        } catch (Exception e) {
            log.error("[DataCleaningBatchJobConfig.pagingQueryProvider][ERROR] ERROR OCCURED : ", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ItemWriter<DataCleaningBatchRecord> dataCleaningBatchWriter() {
        return dataCleaningBatchRecords -> {
            for (DataCleaningBatchRecord dataCleaningBatchRecord : dataCleaningBatchRecords) {
                log.info("[DataCleaningBatchJobConfig.dataCleaningBatchWriter][INFO] attendance : {}", dataCleaningBatchRecord);
                attendanceRepository.deleteById(dataCleaningBatchRecord.attendanceId());
            }
        };
    }

}
