package com.group.hr.dto;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group.hr.config.LocalDateDeserializer;
import com.group.hr.config.LocalDateSerializer;
import com.group.hr.domain.Employee;
import com.group.hr.type.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    @JsonIgnore
    private Long id;
    private String name;
    private String teamName;
    private Role role;

    private static ModelMapper modelMapper = new ModelMapper();

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate workStartDate;


    public EmployeeDto(String name, String teamName, Role role, LocalDate birthday, LocalDate workStartDate) {
        this.name = name;
        this.teamName = teamName;
        this.role = role;
        this.birthday = birthday;
        this.workStartDate = workStartDate;
    }

    public static EmployeeDto of(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }
}
