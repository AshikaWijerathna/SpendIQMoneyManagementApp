package com.example.moneymanager.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDTO {
    private Long id;
    private BigDecimal amount;
    private Integer month;
    private Integer year;

    private Long categoryId;
    private String categoryName;
    private String categoryIcon;

    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Double usedPercentage;
    private Boolean exceeded;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
