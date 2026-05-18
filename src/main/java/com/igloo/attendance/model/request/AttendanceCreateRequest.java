package com.igloo.attendance.model.request;

import lombok.Getter;
import lombok.ToString;

public record AttendanceCreateRequest (
        String attenderName,
        String attenderStatus,
        String reason
) {

}
