package com.group.hr.controller;

import com.group.hr.dto.EmployeeDto;
import com.group.hr.service.AttendanceService;
import com.group.hr.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
@Tag(name = "직원 추가 조회 API", description = "직원 추가 및 조회, 직원의 팀을 추가 합니다.")
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping("/add")
    @Operation(summary = "add employee", description = "직원을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "직원 등록이 실패했습니다."),
    })
    public void addEmployee(@RequestBody
                                             @Parameter(description = "직원 이름, 직군, 회사 들오온 일자, 생일 필수입니다.")
                                             @Schema(description = "EmployeeDto", example = "name: 타비," +
                                                     " teamName: stella, " +
                                                     "role : MEMBER, " +
                                                     "birthday : 1998-02-01, " +
                                                     "workStartDate :2024-01-03" )
                                             EmployeeDto request) {
        employeeService.save(request);
    }

    @PutMapping("/{employeeId}/team")
    @Operation(summary = "assign team", description = "해당 직원의 팀을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 팀이 없습니다."),
    })
    public void assignTeam(@PathVariable @Positive(message = "직원 ID와 팀 명을 입력해주세요")
                                            @Schema(description = "직원 ID", example = "1") Long employeeId,
                                        @RequestBody @Schema(description = "팀 이름" , example = "spring") EmployeeDto employeeDto) {
        employeeService.assignEmployeeToTeam(employeeId, employeeDto);
    }

    @GetMapping
    @Operation(summary = "get all employee", description = " 전체 직원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Pageable.class))}),
            @ApiResponse(responseCode = "404", description = "직원 목록이 없습니다."),
    })
    public Page<EmployeeDto> findEmployee(final Pageable pageable) {
        return  employeeService.getAllEmployee(pageable);
    }

}
