package com.group.hr.repository;

import com.group.hr.domain.Employee;
import com.group.hr.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByName(String name);

    Optional<Team> findByName(String name);

    Optional<Team> findByEmployees(Employee employee);
}
