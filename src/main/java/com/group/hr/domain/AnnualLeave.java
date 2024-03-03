package com.group.hr.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "ANNUAL_LEAVE")
public class AnnualLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate leaveDate;
    private int totalLeaves;
    private int usedLeaves;
    private boolean used;

    public void useLeave(LocalDate leaveDate, int leaves) {
        if (this.usedLeaves + leaves > this.totalLeaves) {
            throw new RuntimeException("사용 가능한 연차를 초과했습니다.");
        }
        this.leaveDate = leaveDate;
        this.usedLeaves += leaves;
        this.totalLeaves -= leaves;
    }

    public int getRemainingLeaves() {
        return this.totalLeaves - this.usedLeaves;
    }
}
