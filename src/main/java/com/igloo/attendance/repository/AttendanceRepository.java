package com.igloo.attendance.repository;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.common.util.MemberNames;
import com.igloo.common.util.TimeSelections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    Attendance findByAttendanceName(MemberNames attendanceName);

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
