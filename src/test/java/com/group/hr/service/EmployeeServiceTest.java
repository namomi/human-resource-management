package com.group.hr.service;

import com.group.hr.domain.AnnualLeave;
import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.dto.TeamDto;
import com.group.hr.repository.AnnualLeaveRepository;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.repository.TeamRepository;
import com.group.hr.type.Role;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.group.hr.type.Role.MANAGER;
import static com.group.hr.type.Role.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private AnnualLeaveRepository annualLeaveRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void createEmployeeSave() throws Exception{
        EmployeeDto employee = new EmployeeDto("cosmo",
            "개발팀",
            MEMBER,
            LocalDate.parse("1990-01-01"),
            LocalDate.parse("2022-01-01"));
        TeamDto team = new TeamDto("stella", "str", 5);
        Team teamEntity = new Team(team);

        // 필요한 스텁 설정
        when(employeeRepository.existsByName("cosmo")).thenReturn(false);
        when(teamRepository.existsByName("개발팀")).thenReturn(true);
        when(teamRepository.findByName("개발팀")).thenReturn(Optional.of(teamEntity));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArguments()[0]);
        when(annualLeaveRepository.save(any(AnnualLeave.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        employeeService.save(employee);

        // then
        verify(employeeRepository).existsByName("cosmo");
        verify(teamRepository).existsByName("개발팀");
        verify(teamRepository).findByName("개발팀");
        verify(employeeRepository).save(any(Employee.class));
        verify(annualLeaveRepository).save(any(AnnualLeave.class));

    }

    @Test
    public void successAssignEmployeeToTeam() {
        // given
        Long employeeId = 1L;
        String teamName = "Test Team";
        EmployeeDto employeeDto = new EmployeeDto("Employee Name", teamName, Role.MEMBER, LocalDate.now(), LocalDate.now());

        Employee employee = new Employee(employeeDto);
        Team team = new Team(new TeamDto(teamName, "", 1));

        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        given(teamRepository.findByName(teamName)).willReturn(Optional.of(team));

        // when
        employeeService.assignEmployeeToTeam(employeeId, employeeDto);

        // then
        verify(employeeRepository).findById(employeeId);
        verify(teamRepository).findByName(teamName);
        verify(employeeRepository).save(employee);
        assertThat(employee.getTeam()).isEqualTo(team);
    }

}
