package com.igloo.attendance.model.entity;

import com.igloo.attendance.model.request.AttendanceCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "attendance")
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private String attenderName;
    private String attenderStatus;
    private String reason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Attendance create(AttendanceCreateRequest attendanceCreateRequest) {

        Attendance attendance = new Attendance();

        attendance.attenderName = attendanceCreateRequest.attenderName();
        attendance.attenderStatus = attendanceCreateRequest.attenderStatus();
        attendance.reason = attendanceCreateRequest.reason();
        attendance.createdAt = LocalDateTime.now();
        attendance.updatedAt = attendance.createdAt;

        return attendance;
    }

}
