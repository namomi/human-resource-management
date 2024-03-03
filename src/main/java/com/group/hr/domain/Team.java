package com.group.hr.domain;

import com.group.hr.dto.TeamDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "TEAM")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private String manager;
    private int leaveNoticeDays;


    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    public Team(TeamDto teamDto) {
        this.name = teamDto.getName();
        this.manager = teamDto.getManager();
    }

    public void addEmployee(Employee employee) {
        if (employee != null) {
            if (employees == null) {
                employees = new ArrayList<>();
            }
            employees.add(employee);
            employee.setTeam(this);
        }
    }

    public int getMemberCount() {
        return employees.size();
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
