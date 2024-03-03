package com.group.hr.repository;

import com.group.hr.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByName(String name);

    Employee findByName(String name);
}
