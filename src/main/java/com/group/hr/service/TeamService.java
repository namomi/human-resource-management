package com.group.hr.service;

import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import com.group.hr.dto.TeamDto;
import com.group.hr.exception.TeamException;
import com.group.hr.repository.EmployeeRepository;
import com.group.hr.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.group.hr.domain.CacheKey.KEY_TEAM;
import static com.group.hr.type.ErrorCode.TEAM_ALREADY_EXIST;
import static com.group.hr.type.ErrorCode.TEAM_NOT_FOUND;

@AllArgsConstructor
@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    public void save(TeamDto teamDto) {
        validation(teamDto);
        Team team = new Team(teamDto);
        existsByManager(teamDto, team);
        teamRepository.save(team);
    }

    @Cacheable(key = "#pageable", value = KEY_TEAM)
    @Transactional(readOnly = true)
    public Page<TeamDto> findAllTeam(Pageable pageable) {
        Page<Team> teamPage = teamRepository.findAll(pageable);
        return teamPage.map(TeamDto::of);
    }

    private void existsByManager(TeamDto teamDto, Team team) {
        boolean exists = employeeRepository.existsByName(teamDto.getManager());
        if(exists){
            Employee manager = employeeRepository.findByName(teamDto.getManager());
            team.getEmployees().add(manager);
        }
    }

    private void validation(TeamDto teamDto) {
        boolean exists = teamRepository.existsByName(teamDto.getName());
        if(exists){
            throw new TeamException(TEAM_ALREADY_EXIST);
        }
        if(teamDto.getName().isEmpty()){
            throw new TeamException(TEAM_NOT_FOUND);
        }
    }

}
