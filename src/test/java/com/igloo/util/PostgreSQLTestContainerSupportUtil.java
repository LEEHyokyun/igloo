package com.igloo.util;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.common.util.MemberNames;
import com.igloo.common.util.TimeSelections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Random;

/*
* TestContainer는 기본적으로 본인 스스로 컨테이너를 띄우고, 접속정보를 생성하여 테스트 컨테이너 환경을 생성한다.
* Spring은 이러한 접속정보를 datasource 객체에 주입하여, 테스트 시 해당 컨테이너 환경을 활용할 수 있도록 한다.
* 의존성 설정을 통해 container 환경을 구성할 수도 있지만, 좀 더 간편화하기 위함
* */
@Testcontainers
public class PostgreSQLTestContainerSupportUtil {

    protected static final Random RANDOM = new Random();

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("postgres")
            ;

    @Autowired
    protected TestEntityManager testEntityManager;

    @BeforeEach
    protected void insertData() {
        testEntityManager.getEntityManager()
                .createNativeQuery("TRUNCATE TABLE attendance")
                .executeUpdate();

        for (int i = 1; i <= 5; i++) {
            Attendance attendance = Attendance.create(
                    getMemberName(),
                    "출석",
                    getTimeSelection(),
                    "test reason " + i
            );
            testEntityManager.persist(attendance);
        }
        testEntityManager.flush();
        testEntityManager.clear();

    }

    @AfterEach
    void cleanData() {
        testEntityManager.getEntityManager()
                .createNativeQuery("TRUNCATE TABLE attendance")
                .executeUpdate();
    }

    protected long getTimeSelection() {

        long[] values = Arrays.stream(TimeSelections.values())
                .filter(timeSelections -> timeSelections != TimeSelections.ATTENDANCE_NONE)
                .map(timeSelections -> timeSelections.getTime())
                .mapToLong(Long::longValue)
                .toArray();

        return values[RANDOM.nextInt(values.length)];

    }

    protected String getMemberName(){

        String[] memberNames = new String[]{
                MemberNames.김민주.getNickName(),
                MemberNames.강래구.getNickName(),
                MemberNames.김평숙.getNickName(),
                MemberNames.송재연.getNickName(),
                MemberNames.나호준.getNickName()
        };

        return memberNames[RANDOM.nextInt(memberNames.length)];

    }

}
