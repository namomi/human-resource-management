package com.group.hr.dto;

import com.group.hr.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamDto {

    private String name;

    private String manager;

    private Integer memberCount;

    public TeamDto(Team team) {
        this.name = team.getName();
        this.manager = team.getManager();
        this.memberCount = team.getMemberCount();
    }
}
