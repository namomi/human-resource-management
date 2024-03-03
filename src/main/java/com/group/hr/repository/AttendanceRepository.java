package com.group.hr.repository;

import com.group.hr.domain.Attendance;
import com.group.hr.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    @Query("SELECT a FROM ATTENDANCE a WHERE a.employee.id = :employeeId " +
            "AND YEAR(a.date) = :year AND MONTH(a.date) = :month")
    List<Attendance> findByEmployeeIdAndMonth(@Param("employeeId") Long employeeId,
                                              @Param("year") int year,
                                              @Param("month") int month);

    @Query("SELECT a FROM ATTENDANCE a WHERE a.employee.id = :employeeId AND a.date BETWEEN :startDate AND :endDate")
    List<Attendance> findByEmployeeIdAndPeriod(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
