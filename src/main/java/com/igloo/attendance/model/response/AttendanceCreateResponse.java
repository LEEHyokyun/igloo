package com.igloo.attendance.model.response;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.common.util.TimeSelections;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public record AttendanceCreateResponse (
        Long attendanceId,
        String attendanceName,
        String attendanceStatus,
        Long attendanceTime,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AttendanceCreateResponse from(Attendance attendance) {

        return new AttendanceCreateResponse(
                attendance.getAttendanceId(),
                attendance.getAttendanceName(),
                attendance.getAttendanceStatus().getOption(),
                attendance.getAttendanceTime().getTime(),
                attendance.getReason(),
                attendance.getCreatedAt(),
                attendance.getUpdatedAt()
        );
    }
}
