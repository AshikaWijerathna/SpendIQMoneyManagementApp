package com.example.moneymanager.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryReportDTO {
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private  BigDecimal totalAmount;
}
