package com.group.hr.service;

import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.dto.TeamDto;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TeamRepository teamRepository;

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



        when(employeeRepository.existsByName("cosmo")).thenReturn(false);
        when(teamRepository.findByName("개발팀")).thenReturn(Optional.of(new Team(team)));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        EmployeeDto savedEmployeeDto = employeeService.save(employee);

        // then
        verify(employeeRepository).save(any(Employee.class));
        assertEquals("cosmo", savedEmployeeDto.getName());
        assertEquals("개발팀", savedEmployeeDto.getTeamName());
        assertEquals(MEMBER, savedEmployeeDto.getRole());
        assertEquals(LocalDate.parse("1990-01-01"), savedEmployeeDto.getBirthday());
        assertEquals(LocalDate.parse("2022-01-01"), savedEmployeeDto.getWorkStartDate());
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

    @Test
    public void successGetAllEmployee() throws Exception{
        //given

        List<EmployeeDto> employeeDtos = Arrays.asList(
                new EmployeeDto("히나", "stella", MEMBER, LocalDate.parse("1997-02-01"), LocalDate.parse("2023-05-01")),
                new EmployeeDto("시로", "stella", MEMBER, LocalDate.parse("1997-03-01"), LocalDate.parse("2023-03-01")),
                new EmployeeDto("타비", "stella", MEMBER, LocalDate.parse("1997-04-01"), LocalDate.parse("2023-02-07")),
                new EmployeeDto("칸나", "stella", MANAGER, LocalDate.parse("1997-05-01"), LocalDate.parse("2023-01-01"))
        );
        List<Employee> employees = employeeDtos.stream()
                .map(Employee::new)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> employeePage = new PageImpl<>(employees, pageable, employees.size());

        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        //when
        Page<EmployeeDto> result = employeeService.getAllEmployee(pageable);
        List<EmployeeDto> employeeList = result.getContent();

        //then
        assertThat(result.getContent().size()).isEqualTo(4);
        assertThat(employeeList.get(0).getName()).isEqualTo("히나");
        assertThat(employeeList.get(0).getTeamName()).isEqualTo("stella");
        assertThat(employeeList.get(0).getRole()).isEqualTo(MEMBER);
    }
}