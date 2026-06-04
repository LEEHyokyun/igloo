package com.igloo.attendance.batch.cleaning.process;

import com.igloo.attendance.repository.AttendanceRepository;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DataCleaningBatchJobConfigTest extends PostgreSQLTestContainerSupportUtil {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @DisplayName("[단위테스트] 배치 Reader 테스트")
    @Test
    public void dataCleaningBatchJobReaderTest() throws Exception {

        //given
        DataCleaningBatchJobConfig config =
                new DataCleaningBatchJobConfig(
                        Mockito.mock(JobRepository.class),
                        transactionManager,
                        dataSource,
                        attendanceRepository
                );

        JdbcPagingItemReader<DataCleaningBatchRecord> reader =
                config.dataCleaningBatchReader(dataSource);

        reader.afterPropertiesSet();
        reader.open(new ExecutionContext());

        int count = 0;

        //when
        log.info("into reader test");
        //while (reader.read() != null) { //cursor 이동하므로 정확한 결과 확보 불가
        while(true){
            DataCleaningBatchRecord item = reader.read();

            if(item == null) break;

            log.info("명단 확인");

            assertEquals("Y", item.attendanceStatus());

            count++;
        }

        //then
        assertEquals(5, count);
    }

    @DisplayName("[단위테스트] 배치 Writer 테스트")
    @Test
    public void dataCleaningBatchJobWriterTest() throws Exception {

        //given
        DataCleaningBatchJobConfig config =
                new DataCleaningBatchJobConfig(
                        Mockito.mock(JobRepository.class),
                        transactionManager,
                        dataSource,
                        attendanceRepository
                );

        ItemWriter<DataCleaningBatchRecord> writer =
                config.dataCleaningBatchWriter();

        List<DataCleaningBatchRecord> itemList =
                attendanceRepository.findAll()
                        .stream()
                        .map(attendance -> new DataCleaningBatchRecord(
                                attendance.getAttendanceId(),
                                attendance.getAttendanceName().name(),
                                attendance.getAttendanceStatus().name(),
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
        assertEquals(0, attendanceRepository.count());
    }
}