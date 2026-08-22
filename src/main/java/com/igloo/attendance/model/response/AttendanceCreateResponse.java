package com.igloo.attendance.model.response;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.common.util.TimeSelections;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record AttendanceCreateResponse (
        Long attendanceId,
        String attendanceName,
        String attendanceOption,
        LocalTime attendanceTime,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AttendanceCreateResponse from(Attendance attendance) {

        return new AttendanceCreateResponse(
                attendance.getAttendanceId(),
                attendance.getAttendanceName().name(),
                attendance.getAttendanceOption().name(),
                TimeSelections.of(attendance.getAttendanceTime()),
                attendance.getReason(),
                attendance.getCreatedAt(),
                attendance.getUpdatedAt()
        );
    }
}
