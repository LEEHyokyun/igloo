package com.igloo.attendance.batch.cleaning.process;

import lombok.Data;

import java.time.LocalDateTime;

public record DataCleaningBatchRecord(
        Long attendanceId,
        String attendanceName,
        String attendanceStatus,
        String attendanceTime,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
