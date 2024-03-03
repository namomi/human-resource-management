package com.group.hr.repository;

import com.group.hr.domain.AnnualLeave;
import com.group.hr.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AnnualLeaveRepository extends JpaRepository<AnnualLeave, Long> {

    @Query("SELECT CASE WHEN COUNT(al) > 0 THEN true ELSE false END FROM ANNUAL_LEAVE al WHERE al.employee = :employee AND al.leaveDate = :leaveDate")
    Boolean existsEmployeeAndDate(@Param("employee") Employee employee, @Param("leaveDate") LocalDate leaveDate);


    Optional<AnnualLeave> findByEmployee(Employee employee);

    @Query("SELECT al FROM ANNUAL_LEAVE al WHERE al.employee = :employee AND al.leaveDate BETWEEN :startDate AND :endDate")
    List<AnnualLeave> findByEmployeeAndPeriod(@Param("employee") Employee employee,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}

