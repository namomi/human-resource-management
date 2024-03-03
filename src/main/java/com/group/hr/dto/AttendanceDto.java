package com.group.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AttendanceDto {
    private LocalDate date;
    private long workingMinutes;
    private boolean usingDayOff;
}
