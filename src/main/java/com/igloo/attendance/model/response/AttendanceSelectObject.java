package com.igloo.attendance.model.response;

import com.igloo.attendance.model.entity.Attendance;

public record AttendanceSelectObject(
        String attendanceName
) {
    public static AttendanceSelectObject from(Attendance attendance) {
        return new AttendanceSelectObject(
                attendance.getAttendanceName().name()
        );
    }
}
