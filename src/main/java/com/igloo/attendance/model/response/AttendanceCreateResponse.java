package com.igloo.attendance.model.response;

import com.igloo.attendance.model.entity.Attendance;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public record AttendanceCreateResponse (
        Long attendanceId,
        String attenderName,
        String attenderStatus,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AttendanceCreateResponse from(Attendance attendance) {

        return new AttendanceCreateResponse(
                attendance.getAttendanceId(),
                attendance.getAttenderName(),
                attendance.getAttenderStatus(),
                attendance.getReason(),
                attendance.getCreatedAt(),
                attendance.getUpdatedAt()
        );
    }
}
