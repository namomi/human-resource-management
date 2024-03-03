package com.group.hr.controller;

import com.group.hr.dto.OvertimeDto;
import com.group.hr.service.OvertimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
@Tag(name = "초과 근무 계산  API", description = "모든 직원의 초과 근무(주말, 공휴일 제외)를 했는지 계산합니다.")
public class OverTimeController {
    private final OvertimeService overtimeService;

    @GetMapping("/overtime/{year}/{month}")
    @Operation(summary = "calculate overtime", description = "모든 직원의 초과근무를 계산합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = OvertimeDto.class))}),
            @ApiResponse(responseCode = "404", description = "근무 기록이 없습니다."),
    })
    public ResponseEntity<List<OvertimeDto>> getMonthlyOvertime(@PathVariable
                                                                @Positive(message = "년도와 월을 입력해주세요")
                                                                @Schema(description = "년도", example = "2024") int year,
                                                                @PathVariable
                                                                @Schema(description = "월", example = "03") int month)
            throws Exception {
        return ResponseEntity.ok(overtimeService.getMonthlyOvertime(year, month));
    }
}
