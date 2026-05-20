package com.igloo.attendance.repository;

import com.igloo.attendance.model.entity.Attendance;
import com.igloo.common.attendance.util.MemberNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    Attendance findByAttendanceName(MemberNames attendanceName);
}
