package com.group.hr.service;

import com.group.hr.domain.AnnualLeave;
import com.group.hr.domain.Attendance;
import com.group.hr.domain.Employee;
import com.group.hr.dto.AttendanceSummaryDto;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.repository.AnnualLeaveRepository;
import com.group.hr.repository.AttendanceRepository;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.type.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    AttendanceRepository attendanceRepository;

    @Mock
    AnnualLeaveRepository annualLeaveRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    AttendanceService attendanceService;

    @Test
    public void successAttendance() {
        Long employeeId = 1L;
        Employee mockEmployee
                = new Employee(new EmployeeDto(1L, "str", "spring",
                Role.MEMBER, LocalDate.parse("2003-03-03"), LocalDate.parse("2019-04-01")));


        LocalDate today = LocalDate.now();

        given(employeeRepository.findById(1L)).willReturn(Optional.of(mockEmployee));
        given(attendanceRepository.findByEmployeeIdAndDate(employeeId, today)).willReturn(Optional.empty());

        // When
        attendanceService.attendance(employeeId);

        // Then
        verify(attendanceRepository).save(any(Attendance.class));
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    public void successGotOffWork() {
        // Given
        Long employeeId = 1L;
        LocalDate today = LocalDate.now();
        Attendance mockAttendance = new Attendance();

        given(attendanceRepository.findByEmployeeIdAndDate(employeeId, today)).willReturn(Optional.of(mockAttendance));

        // When
        attendanceService.gotOffWork(employeeId);

        // Then
        assertNotNull(mockAttendance.getEndTime());
        verify(attendanceRepository).save(mockAttendance);
        verify(attendanceRepository, times(1)).save(mockAttendance);
    }

    @Test
    public void getMonthlyAttendanceTest() {
        // Given
        Long employeeId = 1L;
        YearMonth yearMonth = YearMonth.of(2024, 2);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        Employee mockEmployee = new Employee();

        List<Attendance> mockAttendances = Collections.emptyList();
        List<AnnualLeave> mockAnnualLeaves = Collections.emptyList();

        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(mockEmployee));
        given(attendanceRepository.findByEmployeeIdAndPeriod(employeeId, startDate, endDate)).willReturn(mockAttendances);
        given(annualLeaveRepository.findByEmployeeAndPeriod(mockEmployee, startDate, endDate)).willReturn(mockAnnualLeaves);

        // When
        AttendanceSummaryDto result = attendanceService.getMonthlyAttendance(employeeId, yearMonth);

        // Then
        assertNotNull(result);
    }
}