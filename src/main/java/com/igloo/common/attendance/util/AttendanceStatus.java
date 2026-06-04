
package com.igloo.common.attendance.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    출석("출석"),
    불참("불참"),
    지각("지각"),
    결석("결석"),
    NO_STATUS("NO_STATUS")
    ;

    private final String attendanceStatus;

    public static AttendanceStatus from(String option){

        for (AttendanceStatus attendanceStatus : values()) {
            if (attendanceStatus.attendanceStatus.equals(option)) {
                return attendanceStatus;
            }
        }

        log.error("[ERROR][TimeSelections.from] No attendance options found={}", option);
        return AttendanceStatus.NO_STATUS;

    }
}
