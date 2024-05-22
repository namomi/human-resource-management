package com.group.hr.controller;

import com.group.hr.dto.TeamDto;
import com.group.hr.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
@AllArgsConstructor
@Tag(name = "팀 추가 조회 API", description = "팀 추가 및 조회 할 수 있습니다..")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/add")
    @Operation(summary = "add team", description = "팀을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "팀 등록이 실패했습니다.."),
    })
    public void addTeam(@RequestBody
                                         @Parameter(description = "팀이름 필수입니다.")
                                         @Schema(description = "TeamDto", example = "teamName: stella")
                                         TeamDto request) {
        teamService.save(request);
    }

    @GetMapping
    @Operation(summary = "get all team", description = "모든 팀 정보를 조회 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Pageable.class))}),
            @ApiResponse(responseCode = "404", description = "팀 목록이 없습니다."),
    })
    public Page<TeamDto> findTeam(final Pageable pageable) {
        return teamService.findAllTeam(pageable);
    }


}
