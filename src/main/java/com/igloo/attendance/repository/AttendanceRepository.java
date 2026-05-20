package com.igloo.attendance.repository;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.attendance.model.response.AttendanceSelectObject;
import com.igloo.common.attendance.util.MemberNames;
import com.igloo.common.attendance.util.TimeSelections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    Attendance findByAttendanceName(MemberNames attendanceName);

    @Query(
            value = " select a.attendance_name " +
                    " from attendance a " +
                    " where 1=1 " +
                    " and a.attendance_time = :attendanceTime",
            nativeQuery = true
    )
    List<Attendance> findAttendancesByAttendanceTime(
            @Param("attendanceTime") TimeSelections attendanceTime
    );

    @Query(
            value = "select count(*) from ( " +
                    "   select a.attendance_id " +
                    "   from attendance a " +
                    "   where attendance_time = :attendanceTime" +
                    ")",
            nativeQuery = true
    )
    long countOfAttendances(
            @Param("attendanceTime") TimeSelections attendanceTime
    );

    @Query(
            value = " select a.attendance_name " +
                    " from attendance a " +
                    " where 1=1 " +
                    " and a.attendance_time = :attendanceTime",
            nativeQuery = true
    )
    List<Attendance> countOfOption1Attenders(
            @Param("attendanceTime") TimeSelections attendanceTime
    );
}
