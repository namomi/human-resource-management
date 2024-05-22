package com.group.hr.domain;

import com.group.hr.dto.EmployeeDto;
import com.group.hr.type.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;


    @Column(name = "teamName")
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private LocalDate birthday;

    @NotNull
    private LocalDate workStartDate;


    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnnualLeave> annualLeaves = new ArrayList<>();

    public void setTeam(Team team) {
        this.team = team;
        this.teamName = team.getName();
    }

    public Employee(EmployeeDto employeeDto) {
        this.name = employeeDto.getName();
        this.role = employeeDto.getRole();
        this.birthday = employeeDto.getBirthday();
        this.workStartDate = employeeDto.getWorkStartDate();
    }
    public int setTotalLeaves(int year) {
        return year == Year.now().getValue() ? 11 : 15;
    }
}
