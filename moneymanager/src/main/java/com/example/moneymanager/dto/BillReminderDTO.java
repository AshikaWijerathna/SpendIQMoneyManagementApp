package com.example.moneymanager.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillReminderDTO {
    private Long id;
    private String billName;
    private BigDecimal amount;
    private LocalDate dueDate;
    private Boolean paid;
    private Boolean reminderSent;

}
