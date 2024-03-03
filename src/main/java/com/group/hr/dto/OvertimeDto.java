package com.group.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OvertimeDto {
    private Long id;
    private String name;
    private int overtimeMinutes;
}
