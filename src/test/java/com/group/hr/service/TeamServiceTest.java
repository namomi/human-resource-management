package com.group.hr.service;

import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import com.group.hr.dto.EmployeeDto;
import com.group.hr.dto.TeamDto;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.group.hr.type.Role.MEMBER;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void successCreateTeam() throws Exception{
        EmployeeDto employee = new EmployeeDto("cosmo",
                "개발팀",
                MEMBER,
                LocalDate.parse("1990-01-01"),
                LocalDate.parse("2022-01-01"));

        TeamDto teamDto = new TeamDto("stella", "str", 5);

        when(teamRepository.existsByName("stella")).thenReturn(false);
        when(employeeRepository.existsByName("str")).thenReturn(true);
        when(employeeRepository.findByName("str")).thenReturn(new Employee(employee));
        when(teamRepository.save(any(Team.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        TeamDto savedTeamDto = teamService.save(teamDto);

        // then
        verify(teamRepository).save(any(Team.class));
        assertThat(savedTeamDto.getName()).isEqualTo("stella");
        assertThat(savedTeamDto.getManager()).isEqualTo("str");
    }

    @Test
    public void successFindTeam() throws Exception{
        Pageable pageable = PageRequest.of(0, 10);
        TeamDto team1 = new TeamDto("stella", "str", 5);
        TeamDto team2 = new TeamDto("spring", "boot", 10);
        TeamDto team3 = new TeamDto("apple", "mac", 7);
        List<Team> teams = Arrays.asList(new Team(team1), new Team(team2), new Team(team3));
        Page<Team> teamPage = new PageImpl<>(teams, pageable, teams.size());

        when(teamRepository.findAll(pageable)).thenReturn(teamPage);

        // when
        Page<TeamDto> result = teamService.findAllTeam(pageable);
        List<TeamDto> content = result.getContent();

        //then
        assertThat(content.size()).isEqualTo(teams.size());
        assertThat(content.get(0).getName()).isEqualTo("stella");
        assertThat(content.get(1).getManager()).isEqualTo("boot");
    }
}
