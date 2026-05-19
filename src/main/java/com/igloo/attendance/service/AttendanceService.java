package com.igloo.attendance.service;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.attendance.model.request.AttendanceCreateRequest;
import com.igloo.attendance.model.response.AttendanceCreateResponse;
import com.igloo.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public AttendanceCreateResponse save(AttendanceCreateRequest attendanceCreateRequest) {

        Attendance attendance = attendanceRepository.findByAttendanceName(attendanceCreateRequest.attendanceName());

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
