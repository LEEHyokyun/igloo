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
    NONE("NO OPTIONS");

    private final String option;

    public static AttendanceOptions from(String option){

        for (AttendanceOptions timeSelection : values()) {
            if (timeSelection.option.equals(option)) {
                return timeSelection;
            }
        }

        log.error("[ERROR][TimeSelections.from] No attendance options found={}", option);
        return AttendanceOptions.NONE;

    }
}
