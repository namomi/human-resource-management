package com.group.hr.service;

import com.group.hr.domain.AnnualLeave;
import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.dto.ErrorResponse;
import com.group.hr.exception.EmployeeException;
import com.group.hr.repository.AnnualLeaveRepository;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

import static com.group.hr.domain.CacheKey.KEY_EMPLOYEE;
import static com.group.hr.type.ErrorCode.*;
import static com.group.hr.type.Role.*;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final AnnualLeaveRepository annualLeaveRepository;

    public EmployeeDto save(EmployeeDto employeeDto) {
        boolean exists = employeeRepository.existsByName(employeeDto.getName());

        checkEmployee(exists);

        Employee savedEmployee = new Employee(employeeDto);
        addTeam(employeeDto, savedEmployee, exists);

        setAnnualLeaves(savedEmployee);

        employeeRepository.save(savedEmployee);
        return EmployeeDto.builder()
                            .name(savedEmployee.getName())
                            .teamName(savedEmployee.getTeamName())
                            .role(savedEmployee.getRole())
                            .birthday(savedEmployee.getBirthday())
                            .workStartDate(savedEmployee.getWorkStartDate())
                            .build();
    }

    public void assignEmployeeToTeam(Long employeeId, EmployeeDto employeeDto) {
        Employee employee = getEmployee(employeeId);
        Team team = getTeam(employeeDto);
        team.addEmployee(employee);
        employeeRepository.save(employee);
    }

    @Cacheable(key = "#pageable", value = KEY_EMPLOYEE)
    @Transactional(readOnly = true)
    public Page<EmployeeDto> getAllEmployee(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(Employee::toDto);
    }

    private static void checkEmployee(boolean exists) {
        if(exists) throw new EmployeeException(EMPLOYEE_ALREADY_EXIST);
    }

    private void addTeam(EmployeeDto employeeDto, Employee savedEmployee, boolean exists) {
        boolean existsByTeamName = teamRepository.existsByName(employeeDto.getTeamName());
        if (existsByTeamName) {
            Team team = getTeam(employeeDto);
            savedEmployee.setTeam(team);
            if(exists && savedEmployee.getRole() == MANAGER){
                team.setManager(savedEmployee.getName());
            }
        }
    }


    private Employee getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
        return employee;
    }

    private Team getTeam(EmployeeDto employeeDto) {
        Team team = teamRepository.findByName(employeeDto.getTeamName())
                .orElseThrow(() -> new EmployeeException(TEAM_NOT_FOUND));
        return team;
    }

    private void setAnnualLeaves(Employee employee) {
        int totalLeaves = employee.setTotalLeaves(employee.getWorkStartDate().getYear());

        annualLeaveRepository.save(new AnnualLeave().builder()
                        .employee(employee)
                        .leaveDate(null)
                        .totalLeaves(totalLeaves)
                        .usedLeaves(0)
                        .used(false)
                        .build());
    }

}
