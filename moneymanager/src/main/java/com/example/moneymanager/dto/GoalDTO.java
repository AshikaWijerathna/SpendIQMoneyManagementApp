package com.example.moneymanager.dto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalDTO {

    private Long id;
    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate deadline;

    private Double progressPercentage;
    private BigDecimal remainingAmount;
    private Long daysRemaining;

    private Boolean completed;
    private LocalDate completedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
