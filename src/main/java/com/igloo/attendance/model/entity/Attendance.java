package com.igloo.attendance.model.entity;

import com.igloo.common.attendance.util.AttendanceOptions;
import com.igloo.common.attendance.util.AttendanceStatus;
import com.igloo.common.attendance.util.MemberNames;
import com.igloo.common.attendance.util.TimeSelections;
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

    @Enumerated(EnumType.STRING)
    private MemberNames attendanceName;

    @Enumerated(EnumType.STRING)
    private AttendanceOptions attendanceOption;

    @Enumerated(EnumType.STRING)
    private TimeSelections attendanceTime;
    private String reason;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Attendance create(String attendanceName, String attendanceOption, Long attendanceTime, String reason) {

        Attendance attendance = new Attendance();

        attendance.attendanceName = MemberNames.from(attendanceName);
        attendance.attendanceOption = AttendanceOptions.from(attendanceOption);
        attendance.attendanceTime = TimeSelections.to(attendanceTime);
        attendance.reason = reason;
        attendance.createdAt = LocalDateTime.now();
        attendance.updatedAt = attendance.createdAt;

        return attendance;
    }

    public Attendance update(String attendanceOption, Long attendanceTime, String reason){
        this.attendanceOption = AttendanceOptions.from(attendanceOption);;
        this.attendanceTime = TimeSelections.to(attendanceTime);
        this.reason = reason;
        this.updatedAt = LocalDateTime.now();

        return this;
    }

    public static Attendance summarize(Long attendanceId, AttendanceStatus attendanceStatus){

        Attendance attendance = new Attendance();

        attendance.attendanceId = attendanceId;
        attendance.attendanceStatus = attendanceStatus;

        return attendance;
    }
}
