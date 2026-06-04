package com.igloo.attendance.service;

import com.igloo.attendance.model.request.AttendanceCreateRequest;
import com.igloo.attendance.model.response.AttendanceCreateResponse;
import com.igloo.attendance.model.response.AttendanceSelectResponse;
import com.igloo.common.attendance.util.MemberNames;
import com.igloo.util.PostgreSQLTestContainerSupportUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(AttendanceService.class)
class AttendanceServiceUnitTest extends PostgreSQLTestContainerSupportUtil {

    @Autowired
    private AttendanceService attendanceService;

    @DisplayName("[단위테스트] 출석 조회 테스트")
    @Test
    void attendanceSelectTest(){
        //given / when
        AttendanceSelectResponse attendanceSelectResponse = attendanceService.select();

        //then
        log.info(attendanceSelectResponse.toString());

        Assertions.assertEquals(5,
                attendanceSelectResponse.option1AttenderCount() + attendanceSelectResponse.option2AttenderCount() + attendanceSelectResponse.option3AttenderCount()
                );
    }

    @DisplayName("[단위테스트] 출석 반영 테스트")
    @Test
    void attendanceCreateTest(){
        //given / when
        AttendanceCreateResponse attendanceCreateResponse = attendanceService.save(
                new AttendanceCreateRequest(
                                    MemberNames.이효균.name(),
                                     "출석",
                                    getTimeSelection(),
                                    "test reason"
                )
        );

        //then
        Assertions.assertEquals(6,
                attendanceCreateResponse.attendanceId()
        );
        Assertions.assertEquals("이효균",
                attendanceCreateResponse.attendanceName()
        );
        Assertions.assertEquals("Y",
                attendanceCreateResponse.attendanceOption()
        );
    }

}