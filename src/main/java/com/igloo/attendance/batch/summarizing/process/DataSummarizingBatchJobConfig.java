package com.igloo.attendance.batch.summarizing.process;

import com.igloo.attendance.batch.cleaning.process.DataCleaningBatchRecord;
import com.igloo.attendance.model.entity.Attendance;
import com.igloo.attendance.repository.AttendanceRepository;
import com.igloo.common.attendance.util.AttendanceOptions;
import com.igloo.common.attendance.util.AttendanceStatus;
import com.igloo.common.attendance.util.TimeSelections;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSummarizingBatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    private final AttendanceRepository attendanceRepository;

    private final String BATCH_JOB_NAME = "dataSummarizingBatchJob";
    private final String BATCH_STEP_NAME = "dataSummarizingBatchStep";
    private final String BATCH_STEP_READER = "dataSummarizingBatchStepReader";
    private final String BATCH_READER_PARAMETER_ATTENDANCE_STATUS_CANT_ATTENDANCE = "N";
    private final String BATCH_READER_PARAMETER_ATTENDANCE_STATUS_CAN_ATTENDANCE = "Y";

    @Bean
    public Job dataSummarizingBatchJob() {
        return new JobBuilder(BATCH_JOB_NAME, jobRepository)
                .start(dataSummarizingBatchStep())
                .build();
    }

    @Bean
    public Step dataSummarizingBatchStep() {
        return new StepBuilder(BATCH_STEP_NAME, jobRepository)
                .<DataSummarizingBatchRecord, DataSummarizingBatchRecord>chunk(5, transactionManager)
                .reader(dataSummarizingBatchReader(dataSource))
                .writer(dataSummarizingBatchWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<DataSummarizingBatchRecord> dataSummarizingBatchReader(DataSource dataSource) {
        return new JdbcPagingItemReaderBuilder<DataSummarizingBatchRecord>()
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
                .rowMapper(new DataClassRowMapper<>(DataSummarizingBatchRecord.class))
                .build();
    }

    private PagingQueryProvider pagingQueryProvider(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean queryProviderFactory = new SqlPagingQueryProviderFactoryBean();

        //native query
        //with parameter & dataSource
        queryProviderFactory.setDataSource(dataSource);
        queryProviderFactory.setSelectClause("SELECT attendance_id, attendance_name, attendance_option, attendance_time, reason, created_at, updated_at");
        queryProviderFactory.setFromClause("FROM attendance");
        queryProviderFactory.setWhereClause("WHERE attendance_option IN (:attendanceStatus1, :attendanceStatus2)");
        queryProviderFactory.setSortKeys(Map.of("created_at", Order.ASCENDING));

        try {

            return queryProviderFactory.getObject();

        } catch (Exception e) {
            log.error("[DataSummarizingBatchJobConfig.pagingQueryProvider][ERROR] ERROR OCCURED : ", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ItemWriter<DataSummarizingBatchRecord> dataSummarizingBatchWriter() {
        return dataSummarizingBatchRecords -> {
            for (DataSummarizingBatchRecord dataSummarizingBatchRecord : dataSummarizingBatchRecords) {
                log.info("[DataCleaningBatchJobConfig.dataSummarizingBatchWriter][INFO] attendance : {}", dataSummarizingBatchRecord);

                long attendanceId = dataSummarizingBatchRecord.attendanceId();
                String attendanceOption = dataSummarizingBatchRecord.attendanceOption();
                LocalTime attendanceTime = TimeSelections.of(
                        dataSummarizingBatchRecord.attendanceTime()
                        );
                LocalTime standardTime = dataSummarizingBatchRecord.createdAt().toLocalTime();

                /*
                * 옵션 비교는 Enum 기반으로 진행하여 결합도를 낮춘다.
                * */

                /*
                * 참석(Y) 이면서 시간 준수 시 출석
                * 참석(Y) 이면서 시간초과 시 지각
                * */
                if(
                    attendanceOption.equals(
                            AttendanceOptions.Y.name()
                    )
                ) {
                    if (standardTime.isAfter(attendanceTime)) {
                        //지각
                        attendanceRepository.save(
                                Attendance.summarize(
                                        attendanceId,
                                        AttendanceStatus.지각
                                )
                        );
                    }

                    //출석
                    else {
                        attendanceRepository.save(
                                Attendance.summarize(
                                        attendanceId,
                                        AttendanceStatus.출석
                                )
                        );
                    }
                }

                /*
                 * 불참(N)
                 * */
                if(
                        attendanceOption.equals(
                                AttendanceOptions.N.name()
                        )
                ){
                    //결석
                    attendanceRepository.save(
                            Attendance.summarize(
                                    attendanceId,
                                    AttendanceStatus.불참
                            )
                    );
                }

                /*
                 * 출석/불참 데이터가 아에 존재하지 않을 경우 미응답/결석
                 * */

            }
        };
    }

}
