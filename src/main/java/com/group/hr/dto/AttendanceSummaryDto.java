package com.group.hr.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceSummaryDto {
    private List<AttendanceDto> details;
    private long sum;

    public AttendanceSummaryDto(List<AttendanceDto> details) {
        this.details = details;
        this.sum = details.stream()
                .mapToLong(AttendanceDto::getWorkingMinutes)
                .sum();
    }
}
