package com.igloo.common.attendance.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum TimeSelections {
    ATTENDANCE_8(LocalTime.of(8, 0), 8),
    ATTENDANCE_930(LocalTime.of(9, 30), 930),
    ATTENDANCE_10(LocalTime.of(10, 0), 10),
    ATTENDANCE_NONE(null, -1);

    private final LocalTime localTime;
    private final long time;

    //long to timeSelection
    public static TimeSelections to(Long time){

        if(time != null){
            for (TimeSelections timeSelection : values()) {
                if (timeSelection.time == time) {
                    return timeSelection;
                }
            }
        }

        return TimeSelections.ATTENDANCE_NONE;
    }

    //long to local time
    public static LocalTime from(Long time){

        TimeSelections returnValue = null;

        for (TimeSelections timeSelection : values()) {
            if (timeSelection.time == time) {
                returnValue = timeSelection;
            }
        }

        return returnValue.localTime;
    }

    //timeSelection to local time
    public static LocalTime of(TimeSelections option){

        for (TimeSelections timeSelection : values()) {
            if (timeSelection.name().equals(option.name())) {
                return timeSelection.localTime;
            }
        }

        return TimeSelections.ATTENDANCE_NONE.localTime;
    }

    public static LocalTime of(String option){

        for (TimeSelections timeSelection : values()) {
            if (timeSelection.name().equals(option)) {
                return timeSelection.localTime;
            }
        }

        return TimeSelections.ATTENDANCE_NONE.localTime;
    }
}
