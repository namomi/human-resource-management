package com.group.hr.service;

import com.group.hr.domain.AnnualLeave;
import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import com.group.hr.exception.AnnualLeaveException;
import com.group.hr.exception.EmployeeException;
import com.group.hr.repository.AnnualLeaveRepository;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static com.group.hr.domain.CacheKey.KEY_EMPLOYEE;
import static com.group.hr.type.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AnnualLeaveService {
    private final AnnualLeaveRepository annualLeaveRepository;
    private final EmployeeRepository employeeRepository;

    public void applyForLeave(Long employeeId, LocalDate leaveDate, int requestedLeaves) {
        Employee employee = findEmployeeById(employeeId);

        LocalDate currentDate = LocalDate.now();

        Team team = getTeamOfEmployee(employee);
        
        AnnualLeave annualLeave = getAnnualLeave(employee);

        // 연차 신청의 유효성을 검사
        validateLeaveApplication(employee, team, leaveDate, currentDate, requestedLeaves, annualLeave);

        // 연차 사용
        annualLeave.useLeave(leaveDate, requestedLeaves);

        // 연차 정보를 저장 또는 업데이트
        annualLeaveRepository.save(annualLeave);
    }

    @Cacheable(key = "#employeeId", value = KEY_EMPLOYEE)
    @Transactional(readOnly = true)
    public int getTotalLeaves(Long employeeId) {
        Employee employee = findEmployeeById(employeeId);

        AnnualLeave annualLeave = getAnnualLeave(employee);

        return annualLeave.getTotalLeaves();
    }

    private AnnualLeave getAnnualLeave(Employee employee) {
        AnnualLeave annualLeave = annualLeaveRepository.findByEmployee(employee)
                .orElseThrow(() -> new AnnualLeaveException(NO_ANNUAL_LEAVE_RECORD));
        return annualLeave;
    }


    private Employee findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeException(EMPLOYEE_NOT_FOUND));
    }

    private Team getTeamOfEmployee(Employee employee) {
        return Optional.ofNullable(employee.getTeam()).orElse(null);
    }

    private void validateLeaveApplication(Employee employee, Team team, LocalDate leaveDate, LocalDate currentDate, int requestedLeaves, AnnualLeave annualLeave) {
        // 사용 가능한 연차 수를 확인
        checkAvailableLeaves(employee, leaveDate, requestedLeaves, annualLeave);

        // 팀의 연차 정책 확인 (팀이 있을 경우에만)
        if (team != null) {
            checkTeamLeavePolicy(team, leaveDate, currentDate);
        }
    }

    public void checkTeamLeavePolicy(Team team, LocalDate leaveDate, LocalDate currentDate) {
        // 팀별 연차 신청 가능 기간을 가져옴
        int daysBeforeLeave = team.getLeaveNoticeDays();

        // 연차를 사용하려는 날짜에서 필요한 만큼의 일 수를 뺀 날짜를 계산
        LocalDate earliestApplicationDate = leaveDate.minusDays(daysBeforeLeave);

        // 현재 날짜가 연차 신청 가능 날짜 이전인지 확인
        if (currentDate.isBefore(earliestApplicationDate)) {
            throw new AnnualLeaveException(ANNUAL_LEAVE_APPLICATION_ADVANCE, daysBeforeLeave);
        }
    }

    public void checkAvailableLeaves(Employee employee, LocalDate leaveDate, int requestedLeaves, AnnualLeave annualLeave) {
        // 특정 직원의 특정 날짜에 대한 연차 기록을 조회
        boolean exist = annualLeaveRepository.existsEmployeeAndDate(employee, leaveDate);
        if (exist) {
            throw new AnnualLeaveException(ANNUAL_LEAVE_ALREADY_USED);
        }

        // 연차 기록이 존재하는 경우, 사용 가능한 연차 수를 확인
        int remainingLeaves = annualLeave.getRemainingLeaves();
        if (requestedLeaves > remainingLeaves) {
            throw new AnnualLeaveException(EXCEEDED_ANNUAL_LEAVES);
        }
    }
}
