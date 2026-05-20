package com.igloo.attendance.service;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.attendance.model.request.AttendanceCreateRequest;
import com.igloo.attendance.model.response.AttendanceCreateResponse;
import com.igloo.attendance.model.response.AttendanceSelectResponse;
import com.igloo.attendance.repository.AttendanceRepository;
import com.igloo.common.attendance.util.AttendanceOptions;
import com.igloo.common.attendance.util.MemberNames;
import com.igloo.common.attendance.util.TimeSelections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceSelectResponse select(){
        return AttendanceSelectResponse.from(
            attendanceRepository.findAttendancesByAttendanceTime(TimeSelections.OPTION1),
            attendanceRepository.findAttendancesByAttendanceTime(TimeSelections.OPTION2),
            attendanceRepository.findAttendancesByAttendanceTime(TimeSelections.OPTION3),
            attendanceRepository.findAttendancesByAttendanceTime(TimeSelections.NONE),
            attendanceRepository.countOfAttendances(TimeSelections.OPTION1),
            attendanceRepository.countOfAttendances(TimeSelections.OPTION2),
            attendanceRepository.countOfAttendances(TimeSelections.OPTION3),
            attendanceRepository.countOfAttendances(TimeSelections.NONE)
        );
    }

    @Transactional
    public AttendanceCreateResponse save(AttendanceCreateRequest attendanceCreateRequest) {

        Attendance attendance = attendanceRepository.findByAttendanceName(
                MemberNames.from(attendanceCreateRequest.attendanceName())
        );

        if(attendance == null){
            attendance = attendanceRepository.save(
                    Attendance.create(
                    attendanceCreateRequest.attendanceName(),
                    attendanceCreateRequest.attendanceStatus(),
                    attendanceCreateRequest.attendanceTime(),
                    attendanceCreateRequest.reason()
                )
            );
        }else {
            attendance = attendance.update(
                    attendanceCreateRequest.attendanceStatus(),
                    attendanceCreateRequest.attendanceTime(),
                    attendanceCreateRequest.reason()
            );

            attendanceRepository.save(attendance);
        }

        return AttendanceCreateResponse.from(attendance);
    }

}
