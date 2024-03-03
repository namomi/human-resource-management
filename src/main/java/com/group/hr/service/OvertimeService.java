package com.group.hr.service;

import com.group.hr.domain.Attendance;
import com.group.hr.domain.Employee;
import com.group.hr.dto.OvertimeDto;
import com.group.hr.repository.AttendanceRepository;
import com.group.hr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RequiredArgsConstructor
@Service
public class OvertimeService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final HolidayApiService holidayApiService;

    private static final int WORKING_HOURS_DAY = 8;
    private static final String CACHE_NAME = "holidays";

    private final Map<String, Set<LocalDate>> holidayCache = new HashMap<>();

    @Transactional(readOnly = true)
    public List<OvertimeDto> getMonthlyOvertime(int year, int month) throws Exception {
        List<Employee> employees = employeeRepository.findAll();
        List<OvertimeDto> overtimeList = new ArrayList<>();

        Set<LocalDate> holidays = getHolidays(year, month);

        for (Employee employee : employees) {
            int totalOvertimeMinutes = getTotalOvertimeMinutesForEmployee(employee, year, month, holidays);
            overtimeList.add(new OvertimeDto(employee.getId(), employee.getName(), totalOvertimeMinutes));
        }

        return overtimeList;
    }

    @Cacheable(value = CACHE_NAME, key = "#year + ':' + #month")
    public Set<LocalDate> getHolidays(int year, int month) throws Exception {
        // API를 통해 공휴일 정보를 가져옴
        Set<LocalDate> holidays = new HashSet<>();
        JSONArray holidayArray = holidayApiService.getHolidaysResponse(year, month);
        for (int i = 0; i < holidayArray.length(); i++) {
            JSONObject holiday = holidayArray.getJSONObject(i);
            LocalDate date = LocalDate.parse(holiday.getString("locdate"));
            holidays.add(date);
        }
        return holidays;
    }

    private int getTotalOvertimeMinutesForEmployee(Employee employee, int year, int month, Set<LocalDate> holidays) {
        // 연월에 해당하는 출근 기록 조회
        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndMonth(employee.getId(), year, month);

        int totalOvertimeMinutes = 0;
        for (Attendance attendance : attendances) {
            LocalDate date = attendance.getDate();
            if (!isHolidayOrWeekend(date, holidays)) {
                int dailyWorkingMinutes = calculateWorkingMinutes(attendance);
                if (dailyWorkingMinutes > WORKING_HOURS_DAY * 60) {
                    totalOvertimeMinutes += (dailyWorkingMinutes - WORKING_HOURS_DAY * 60);
                }
            }
        }
        return totalOvertimeMinutes;
    }

    private boolean isHolidayOrWeekend(LocalDate date, Set<LocalDate> holidays) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY || holidays.contains(date);
    }

    private int calculateWorkingMinutes(Attendance attendance) {
        // 출근 시간과 퇴근 시간 사이의 차이(분 단위)를 반환
        return (int) Duration.between(attendance.getStartTime(), attendance.getEndTime()).toMinutes();
    }
}
