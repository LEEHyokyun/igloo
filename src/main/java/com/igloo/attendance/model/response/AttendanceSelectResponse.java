package com.igloo.attendance.model.response;

import com.igloo.attendance.model.entity.Attendance;

import java.util.List;

public record AttendanceSelectResponse(
        List<AttendanceSelectObject> option1AttenderList,
        List<AttendanceSelectObject> option2AttenderList,
        List<AttendanceSelectObject> option3AttenderList,
        List<AttendanceSelectObject> absenceList,
        long option1AttenderCount,
        long option2AttenderCount,
        long option3AttenderCount,
        long absenceCount
) {

    public static AttendanceSelectResponse fromResponse(
            List<AttendanceSelectObject> option1AttenderList,
            List<AttendanceSelectObject> option2AttenderList,
            List<AttendanceSelectObject> option3AttenderList,
            List<AttendanceSelectObject> absenceList,
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

    public static AttendanceSelectResponse from(
            List<Attendance> option1AttenderList,
            List<Attendance> option2AttenderList,
            List<Attendance> option3AttenderList,
            List<Attendance> absenceList,
            long option1AttenderCount,
            long option2AttenderCount,
            long option3AttenderCount,
            long absenceCount
    ) {

        return fromResponse(
                option1AttenderList.stream().map(AttendanceSelectObject::from).toList(),
                option2AttenderList.stream().map(AttendanceSelectObject::from).toList(),
                option3AttenderList.stream().map(AttendanceSelectObject::from).toList(),
                absenceList.stream().map(AttendanceSelectObject::from).toList(),
                option1AttenderCount,
                option2AttenderCount,
                option3AttenderCount,
                absenceCount
        );
    }
}
