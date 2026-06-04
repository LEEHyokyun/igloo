package com.igloo.attendance.batch.summarizing.process;

import java.time.LocalDateTime;

public record DataSummarizingBatchRecord(
        Long attendanceId,
        String attendanceName,
        String attendanceOption,
        String attendanceTime,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
