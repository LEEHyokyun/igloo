package com.igloo.attendance.batch.summarizing.process;

import com.igloo.attendance.batch.cleaning.process.DataCleaningBatchJobConfig;
import com.igloo.attendance.batch.cleaning.process.DataCleaningBatchRecord;
import com.igloo.attendance.model.entity.Attendance;
import com.igloo.attendance.repository.AttendanceRepository;
import com.igloo.common.attendance.util.AttendanceStatus;
import com.igloo.util.PostgreSQLTestContainerSupportUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DataSummarizingBatchJobConfigTest extends PostgreSQLTestContainerSupportUtil {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @DisplayName("[단위테스트] 데이터 요약 배치 Reader 테스트")
    @Test
    public void dataCleaningBatchJobReaderTest() throws Exception {

        //given
        DataSummarizingBatchJobConfig config =
                new DataSummarizingBatchJobConfig(
                        Mockito.mock(JobRepository.class),
                        transactionManager,
                        dataSource,
                        attendanceRepository
                );

        JdbcPagingItemReader<DataSummarizingBatchRecord> reader =
                config.dataSummarizingBatchReader(dataSource);

        reader.afterPropertiesSet();
        reader.open(new ExecutionContext());

        int count = 0;

        //when
        log.info("into reader test");
        //while (reader.read() != null) { //cursor 이동하므로 정확한 결과 확보 불가
        while(true){
            DataSummarizingBatchRecord item = reader.read();

            if(item == null) break;

            log.info("명단 확인");

            assertEquals("Y", item.attendanceOption());

            count++;
        }

        //then
        assertEquals(5, count);
    }

    @DisplayName("[단위테스트] 데이터 요약 배치 Writer 테스트")
    @Test
    public void dataCleaningBatchJobWriterTest() throws Exception {

        //given
        //String[] resList = new String[]{"출석", "결석", "불참", "지각"};
//        List<String> resList = List.of(
//                "출석",
//                "결석",
//                "불참",
//                "지각"
//        );

        DataSummarizingBatchJobConfig config =
                new DataSummarizingBatchJobConfig(
                        Mockito.mock(JobRepository.class),
                        transactionManager,
                        dataSource,
                        attendanceRepository
                );

        ItemWriter<DataSummarizingBatchRecord> writer =
                config.dataSummarizingBatchWriter();

        List<DataSummarizingBatchRecord> itemList =
                attendanceRepository.findAll()
                        .stream()
                        .map(attendance -> new DataSummarizingBatchRecord(
                                attendance.getAttendanceId(),
                                attendance.getAttendanceName().name(),
                                attendance.getAttendanceOption().name(),
                                attendance.getAttendanceTime().name(),
                                attendance.getReason(),
                                attendance.getCreatedAt(),
                                attendance.getUpdatedAt()
                        ))
                        .toList();

        int count = 0;

        //when
        log.info("into writer test");
        writer.write(new Chunk<>(itemList));

        //then
        //assertEquals(5, itemList.size());
        List<Attendance> resultList =
                attendanceRepository.findAll().stream().toList();

        for(Attendance result : resultList){
            log.info("CHECK RESULT : {}", result.getAttendanceStatus().name());

            if(result.getAttendanceStatus() != AttendanceStatus.NO_STATUS) count++;
        }

        assertEquals(5, attendanceRepository.count());
    }

}