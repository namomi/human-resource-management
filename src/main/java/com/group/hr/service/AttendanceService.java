package com.group.hr.service;

import com.group.hr.domain.AnnualLeave;
import com.group.hr.domain.Attendance;
import com.group.hr.domain.Employee;
import com.group.hr.dto.AttendanceDto;
import com.group.hr.dto.AttendanceSummaryDto;
import com.group.hr.exception.EmployeeException;
import com.group.hr.repository.AnnualLeaveRepository;
import com.group.hr.repository.AttendanceRepository;
import com.group.hr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.group.hr.domain.CacheKey.KEY_EMPLOYEE;
import static com.group.hr.type.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AnnualLeaveRepository annualLeaveRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public void attendance(Long employeeId) {
        Employee employee = getEmployee(employeeId);

        LocalDate today = LocalDate.now();

        Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today);

        validationAttendance(existingAttendance);

        attendanceRepository.save(new Attendance().builder()
                .employee(employee)
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .build());
    }

    @Transactional
    public void gotOffWork(Long employeeId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = getAttendance(employeeId, today);

        attendance.setEndTime(LocalTime.now());

        attendanceRepository.save(attendance);
    }

    @Cacheable(key = "#employeeId", value = KEY_EMPLOYEE)
    @Transactional(readOnly = true)
    public AttendanceSummaryDto getMonthlyAttendance(Long employeeId, YearMonth yearMonth) {
        Employee employee = getEmployee(employeeId);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndPeriod(employeeId, startDate, endDate);

        List<AnnualLeave> annualLeaves = annualLeaveRepository.findByEmployeeAndPeriod(employee, startDate, endDate);


        List<AttendanceDto> details = getAttendanceDto(startDate, endDate, attendances, annualLeaves);

        return new AttendanceSummaryDto(details);
    }

    private List<AttendanceDto> getAttendanceDto(LocalDate startDate, LocalDate endDate, List<Attendance> attendances, List<AnnualLeave> annualLeaves) {
        List<AttendanceDto> details = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int workingMinutes = calculateWorkingMinutes(attendances, date);
            boolean usingDayOff = isUsingDayOff(annualLeaves, date);
            details.add(new AttendanceDto(date, workingMinutes, usingDayOff));
        }
        return details;
    }

    private Attendance getAttendance(Long employeeId, LocalDate today) {
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new EmployeeException(NO_ATTENDANCE_RECORD_TODAY));

        if (attendance.getEndTime() != null) {
            throw new EmployeeException(EMPLOYEE_ALREADY_GOT_OFF_WORK);
        }
        return attendance;
    }

    private static void validationAttendance(Optional<Attendance> existingAttendance) {
        if (existingAttendance.isPresent()) {
            Attendance attendance = existingAttendance.get();
            if (attendance.getStartTime() != null) {
                throw new EmployeeException(EMPLOYEE_ALREADY_ATTENDANCE);
            }
            if (attendance.getEndTime() != null) {
                throw new EmployeeException(EMPLOYEE_ALREADY_GOT_OFF_WORK);
            }
        }
    }

    private Employee getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
        return employee;
    }

    private int calculateWorkingMinutes(List<Attendance> attendances, LocalDate date) {
        return attendances.stream()
                .filter(a -> a.getDate().isEqual(date))
                .mapToInt(a -> Math.toIntExact(Duration.between(a.getStartTime(), a.getEndTime()).toMinutes()))
                .sum();
    }

    private boolean isUsingDayOff(List<AnnualLeave> annualLeaves, LocalDate date) {
        return annualLeaves.stream().anyMatch(al -> al.getLeaveDate().isEqual(date) && al.isUsed());
    }
}

