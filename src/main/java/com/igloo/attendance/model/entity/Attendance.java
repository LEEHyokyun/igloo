package com.igloo.attendance.model.entity;

import com.igloo.attendance.model.request.AttendanceCreateRequest;
import com.igloo.common.util.TimeSelections;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Entity
@Table(name = "attendance")
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private String attendanceName;
    private String attendanceStatus;

    @Enumerated(EnumType.STRING)
    private TimeSelections attendanceTime;
    private String reason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Attendance create(AttendanceCreateRequest attendanceCreateRequest) {

        log.info("attendanceCreateRequest attendance time 확인 : {}", attendanceCreateRequest.attendanceTime());

        Attendance attendance = new Attendance();

        attendance.attendanceName = attendanceCreateRequest.attendanceName();
        attendance.attendanceStatus = attendanceCreateRequest.attendanceStatus();
        attendance.attendanceTime = TimeSelections.from(attendanceCreateRequest.attendanceTime());
        attendance.reason = attendanceCreateRequest.reason();
        attendance.createdAt = LocalDateTime.now();
        attendance.updatedAt = attendance.createdAt;

        return attendance;
    }

}
