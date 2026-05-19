package com.igloo.attendance.model.entity;

import com.igloo.attendance.model.request.AttendanceCreateRequest;
import com.igloo.common.util.AttendanceOptions;
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

    @Enumerated(EnumType.STRING)
    private AttendanceOptions attendanceStatus;

    @Enumerated(EnumType.STRING)
    private TimeSelections attendanceTime;
    private String reason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Attendance create(String attendanceName, String attendanceStatus, Long attendanceTime, String reason) {

        Attendance attendance = new Attendance();

        attendance.attendanceName = attendanceName;
        attendance.attendanceStatus = AttendanceOptions.from(attendanceStatus);
        attendance.attendanceTime = TimeSelections.from(attendanceTime);
        attendance.reason = reason;
        attendance.createdAt = LocalDateTime.now();
        attendance.updatedAt = attendance.createdAt;

        return attendance;
    }

    public Attendance update(String attendanceStatus, Long attendanceTime, String reason){
        this.attendanceStatus = AttendanceOptions.from(attendanceStatus);;
        this.attendanceTime = TimeSelections.from(attendanceTime);
        this.reason = reason;
        this.updatedAt = LocalDateTime.now();

        return this;
    }
}
