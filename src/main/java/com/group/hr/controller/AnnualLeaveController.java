package com.group.hr.controller;

import com.group.hr.dto.AnnualLeaveDto;
import com.group.hr.service.AnnualLeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
@Tag(name = "연차 신청 및 조회 API", description = "직원의 연차를 신청 또는 조회를 합니다.")
public class AnnualLeaveController {
    private final AnnualLeaveService annualLeaveService;

    @PostMapping("/annual-leve/{employeeId}")
    @Operation(summary = "apply for leave", description = "연차를 신청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "연차 신청이 실패했습니다."),
    })
    public void applyForLeave(@Positive(message = "직원 id와 연차를 쓸 날짜와 연차 몇일인지를 입력해주세요")
                                           @PathVariable @Schema(description = "직원 ID", example = "1") Long employeeId,
                                           @RequestBody @Schema(description = "연차 쓸 날짜와 몇일간 쓸지",
                                                   example = "leaveDate : 2024-02-27, requestedLeaves : 3")
                                           AnnualLeaveDto annualLeaveDto) {
        annualLeaveService.applyForLeave(employeeId, annualLeaveDto.getLeaveDate(), annualLeaveDto.getRequestedLeaves());
    }

    @GetMapping("/total-leaves/{employeeId}")
    @Operation(summary = "get total leaves", description = " 해당 직원의 남은 연차를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 직원이 없습니다."),
    })
    public int totalLeaves(@Positive(message = "직원 ID를 입력해주세요")
                                @PathVariable
                                @Schema(description = "직원 ID", example = "1") Long employeeId) {
        return annualLeaveService.getTotalLeaves(employeeId);
    }
}
