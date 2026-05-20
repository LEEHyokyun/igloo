package com.igloo.attendance.model.response;

import com.igloo.attendance.model.entity.Attendance;

import java.util.List;

public record AttendanceSelectResponse(
        List<String> option1AttenderList,
        List<String> option2AttenderList,
        List<String> option3AttenderList,
        List<String> absenceList,
        long option1AttenderCount,
        long option2AttenderCount,
        long option3AttenderCount,
        long absenceCount
) {

    public static AttendanceSelectResponse from(
            List<String> option1AttenderList,
            List<String> option2AttenderList,
            List<String> option3AttenderList,
            List<String> absenceList,
            long option1AttenderCount,
            long option2AttenderCount,
            long option3AttenderCount,
            long absenceCount
    ) {

        return new AttendanceSelectResponse(
                option1AttenderList,
                option2AttenderList,
                option3AttenderList,
                absenceList,
                option1AttenderCount,
                option2AttenderCount,
                option3AttenderCount,
                absenceCount
        );
    }
}
