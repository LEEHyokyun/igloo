package com.igloo.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum TimeSelections {
    OPTION1(1, 8),
    OPTION2(2, 930),
    OPTION3(3, 10),
    NONE(-1, -1)
    ;

    private final long option;
    private final long time;

    public static TimeSelections from(long time){

        for (TimeSelections timeSelection : values()) {
            if (timeSelection.time == time) {
                return timeSelection;
            }
        }

        log.error("[ERROR][TimeSelections.from] No time options found={}", time);
        return TimeSelections.NONE;

    }
}
