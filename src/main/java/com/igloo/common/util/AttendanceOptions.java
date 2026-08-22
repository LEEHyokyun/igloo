package com.igloo.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum AttendanceOptions {
    Y("출석"),
    N("불참")
    ;

    private final String option;

    //String > enum
    public static AttendanceOptions from(String option){

        AttendanceOptions returnValue = null;

        for (AttendanceOptions attendanceOption : values()) {
            if (attendanceOption.option.equals(option)) {
                return returnValue = attendanceOption;
            }
        }

        return returnValue;

    }

    //enum > String
    public static String from(AttendanceOptions option){

        String  returnValue = null;

        for (AttendanceOptions attendanceOption : values()) {
            if (attendanceOption.name().equals(option.name())) {
                returnValue = attendanceOption.option;
            }
        }

        return returnValue;

    }
}
