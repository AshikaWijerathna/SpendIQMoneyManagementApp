package com.example.moneymanager.service;

import com.example.moneymanager.dto.CategoryReportDTO;
import com.example.moneymanager.dto.MonthlyReportDTO;
import com.example.moneymanager.dto.ReportSummaryDTO;
import com.example.moneymanager.entity.ProfileEntity;
import com.example.moneymanager.repository.ExpenseRepository;
import com.example.moneymanager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    public ReportSummaryDTO getMonthlySummary(Integer month, Integer year){
        ProfileEntity profile = profileService.getCurrentProfile();

        Integer selectedMonth = month != null ? month : LocalDate.now().getMonthValue();
        Integer selectedYear = year != null ? year :LocalDate.now().getYear();

        BigDecimal income = incomeRepository.getMonthlyIncome(
                profile.getId(),
                selectedMonth,
                selectedYear
        );
        BigDecimal expense = expenseRepository.getMonthlyExpense(
                profile.getId(),
                selectedMonth,
                selectedYear
        );
        return ReportSummaryDTO.builder().totalIncome(income).totalExpense(expense).totalSavings(income.subtract(expense)).build();
    }
    public ReportSummaryDTO getYearlySummary(Integer year){
        ProfileEntity profile = profileService.getCurrentProfile();

        Integer selectedYear = year != null ? year : LocalDate.now().getYear();

        BigDecimal income = incomeRepository.getYearlyIncome(profile.getId(),selectedYear);
        BigDecimal expense = expenseRepository.getYearlyExpense(profile.getId(), selectedYear);

        return ReportSummaryDTO.builder().totalIncome(income).totalExpense(expense).totalSavings(income.subtract(expense)).build();
    }
    public List<MonthlyReportDTO> getYearlyMonthlyReport(Integer year){
        ProfileEntity profile = profileService.getCurrentProfile();

        Integer selectedYear= year != null ? year : LocalDate.now().getYear();

        List<MonthlyReportDTO> reports = new ArrayList<>();
        for(int month = 1; month<=12; month++){
            BigDecimal income = incomeRepository.getMonthlyIncome(
                    profile.getId(),
                    month,
                    selectedYear
            );
            BigDecimal expense = expenseRepository.getMonthlyExpense(
                    profile.getId(),
                    month,
                    selectedYear
            );
            reports.add(
                    MonthlyReportDTO.builder().month(month).year(selectedYear).income(income).expense(expense).savings(income.subtract(expense)).build()
            );
        }
        return reports;
    }
    public ReportSummaryDTO getMonthlySummaryForProfile(ProfileEntity profile, Integer month, Integer year){
        BigDecimal income = incomeRepository.getMonthlyIncome(profile.getId(), month, year);
        BigDecimal expense = expenseRepository.getMonthlyExpense(profile.getId(), month, year);

        return ReportSummaryDTO.builder().totalIncome(income).totalExpense(expense).totalSavings(income.subtract(expense)).build();
    }
    public List<CategoryReportDTO> getCategoryWiseSpending(Integer month, Integer year){
        ProfileEntity profile = profileService.getCurrentProfile();

        Integer selectedMonth = month != null ? month : LocalDate.now().getMonthValue();
        Integer selectedYear = year != null ? year : LocalDate.now().getYear();

        return expenseRepository.getCategoryWiseExpense(
                profile.getId(),
                selectedMonth,
                selectedYear
        );
    }
}
