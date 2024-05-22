package com.group.hr.controller;

import com.group.hr.dto.AttendanceDto;
import com.group.hr.dto.AttendanceSummaryDto;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
@Tag(name = "출 퇴근 API", description = "직원의 출퇴근 및 근무시간을 조회합니다.")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/attendance/{employeeId}")
    @Operation(summary = "record attendance", description = "해당 직원을 출근을 기록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "출근 등록이 실패했습니다."),
    })
    public void attendance(@PathVariable
                                        @Positive(message = "직원 id는 필수입니다.")
                                        @Schema(description = "employeeId", example = "2")
                                        Long employeeId) {
        attendanceService.attendance(employeeId);
    }

    @PostMapping("/gotOffWork/{employeeId}")
    @Operation(summary = "got off work", description = "해당 직원을 퇴근을 기록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "퇴근 등록이 실패했습니다."),
    })
    public void gotOffWork(@PathVariable
                                        @Positive(message = "직원 id는 필수입니다.")
                                        @Schema(description = "employeeId", example = "2")
                                        Long employeeId) {
        attendanceService.gotOffWork(employeeId);
    }

    @GetMapping("/attendance/{employeeId}/{date}")
    @Operation(summary = "get monthly attendance", description = "특정 직원의 날짜별 근무시간을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = AttendanceSummaryDto.class))}),
            @ApiResponse(responseCode = "404", description = "근무시간 조회를 실패했습니다.."),
    })
    public AttendanceSummaryDto getMonthlyAttendance(
            @Positive(message = "직원 id와 조회 하고 싶은 날짜를 입력해주세요")
            @PathVariable @Schema(description = "employeeId", example = "2") Long employeeId,
            @PathVariable @Schema(description = "date", example = "2024-02-01") String date) {
        LocalDate localDate = LocalDate.parse(date);
        YearMonth yearMonth = YearMonth.of(localDate.getYear(), localDate.getMonth());
        return attendanceService.getMonthlyAttendance(employeeId, yearMonth);
    }
}
