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

    /*
    * JPQL
    * */
    @Query(
                    " select a.attendanceId " +
                    " from Attendance a " +
                    " where a.attendanceTime = :attendanceTime"
    )
    List<Attendance> findAttendancesByAttendanceTime(
            @Param("attendanceTime") TimeSelections attendanceTime
    );

    /*
     * JPQL
     * */
    @Query(
            value = " select count(*) " +
                    " from Attendance a " +
                    " where a.attendanceTime = :attendanceTime"
    )
    long countOfAttendances(
            @Param("attendanceTime") TimeSelections attendanceTime
    );
}
