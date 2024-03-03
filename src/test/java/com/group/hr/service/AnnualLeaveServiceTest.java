package com.group.hr.service;

import com.group.hr.domain.AnnualLeave;
import com.group.hr.domain.Employee;
import com.group.hr.repository.AnnualLeaveRepository;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AnnualLeaveServiceTest {

    @Mock
    private AnnualLeaveRepository annualLeaveRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private AnnualLeaveService annualLeaveService;


    @Test
    void successGetTotalLeaves() {
        // Given
        Long employeeId = 1L;
        Employee employee = new Employee();
        AnnualLeave annualLeave = new AnnualLeave(employeeId, employee,
                LocalDate.parse("2024-01-01"), 10, 1, false);

        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        given(annualLeaveRepository.findByEmployee(employee)).willReturn(Optional.of(annualLeave));

        // When
        int totalLeaves = annualLeaveService.getTotalLeaves(employeeId);

        // Then
        assertEquals(10, totalLeaves);
    }
}