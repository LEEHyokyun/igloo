package com.igloo.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum AttendanceOptions {
    Y("출석"),
    N("불참"),
    N("NO OPTIONS");

    private final String option;

    public static AttendanceOptions from(String option){

        for (AttendanceOptions attendanceOption : values()) {
            if (attendanceOption.option.equals(option)) {
                return attendanceOption;
            }
        }

        log.error("[ERROR][TimeSelections.from] No attendance options found={}", option);
        return AttendanceOptions.N;

    }
}
