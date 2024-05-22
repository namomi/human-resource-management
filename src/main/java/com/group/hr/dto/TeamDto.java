package com.group.hr.dto;

import org.modelmapper.ModelMapper;

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

	private static ModelMapper modelMapper;

	public static TeamDto of(Team team) {
		return modelMapper.map(team, TeamDto.class);
	}
}
