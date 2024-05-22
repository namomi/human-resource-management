package com.group.hr.service;

import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.dto.TeamDto;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.group.hr.type.Role.MEMBER;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void successCreateTeam() throws Exception{
        EmployeeDto employee = new EmployeeDto("cosmo",
                "개발팀",
                MEMBER,
                LocalDate.parse("1990-01-01"),
                LocalDate.parse("2022-01-01"));

        TeamDto teamDto = new TeamDto("stella", "str", 5);

        when(teamRepository.existsByName("stella")).thenReturn(false);
        when(employeeRepository.existsByName("str")).thenReturn(true);
        when(employeeRepository.findByName("str")).thenReturn(new Employee(employee));
        when(teamRepository.save(any(Team.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        teamService.save(teamDto);

        // then
        verify(teamRepository).save(any(Team.class));
    }

}
