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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceSelectResponse select(){

        List<Attendance> attendances = attendanceRepository.findAll();

        Map<TimeSelections,List<String>> groupedAttendances =
                attendances.stream()
                        .collect(Collectors.groupingBy(
                                Attendance::getAttendanceTime,
                                Collectors.mapping(
                                        attendance -> attendance.getAttendanceName().name(),
                                        Collectors.toList()
                                )
                        ));

        //응답
        Set<MemberNames> responsers = attendances.stream()
                .map(Attendance::getAttendanceName)
                .collect(Collectors.toSet());

        //미응답/지각
        List<String> noResponsers = Arrays.stream(MemberNames.values())
                .filter(member -> member != MemberNames.INVALIDATED_MEMBER)
                .filter(member -> !responsers.contains(member))
                .map(MemberNames::name)
                .toList();

        //summarized
        List<String> option1AttenderList =
                groupedAttendances.getOrDefault(TimeSelections.ATTENDANCE_8, List.of());

        List<String> option2AttenderList =
                groupedAttendances.getOrDefault(TimeSelections.ATTENDANCE_930, List.of());

        List<String> option3AttenderList =
                groupedAttendances.getOrDefault(TimeSelections.ATTENDANCE_10, List.of());

        List<String> absenceList =
                groupedAttendances.getOrDefault(TimeSelections.ATTENDANCE_NONE, List.of());


        return AttendanceSelectResponse.from(
                option1AttenderList,
                option2AttenderList,
                option3AttenderList,
                absenceList,
                noResponsers,
                option1AttenderList.size(),
                option2AttenderList.size(),
                option3AttenderList.size(),
                absenceList.size(),
                noResponsers.size()
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
