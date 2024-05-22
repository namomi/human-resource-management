package com.group.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.hr.dto.TeamDto;
import com.group.hr.service.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @MockBean
    private TeamService teamService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void successCreateTeam() throws Exception {
        //given
        TeamDto teamDto = new TeamDto("3M", "하나", 1);
        doNothing().when(teamService).save(teamDto);

        //when
        mockMvc.perform(post("/team/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"3M\",\"manager\":\"하나\",\"memberCount\":1}"))
            .andExpect(status().isOk());

        //then
        verify(teamService, times(1)).save(any(TeamDto.class));
    }

    @Test
    public void testFindTeam() throws Exception {
        // given
        Pageable pageable =  PageRequest.of(0, 10);
        List<TeamDto> teams = Arrays.asList(new TeamDto("stella","",4),
                new TeamDto("str", "jt", 5),
                new TeamDto("spring", "boot", 10));
        Page<TeamDto> teamPage = new PageImpl<>(teams,  pageable, teams.size());

        given(teamService.findAllTeam(pageable)).willReturn(teamPage);

        // when & then
        mockMvc.perform(get("/team")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(teams.size())));


        verify(teamService).findAllTeam(pageable);
    }
}

