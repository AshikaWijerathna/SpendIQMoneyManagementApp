package com.example.moneymanager.dto;

import lombok.*;
import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReportDTO {
    private Integer month;
    private Integer year;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal savings;
}
