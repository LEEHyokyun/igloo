package com.igloo.attendance.model.request;

import lombok.Getter;
import lombok.ToString;

public record AttendanceCreateRequest (
        String attendanceName,
        String attendanceStatus,
        long attendanceTime,
        String reason
) {

}
