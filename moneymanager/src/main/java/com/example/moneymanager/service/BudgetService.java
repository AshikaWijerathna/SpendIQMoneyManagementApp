package com.example.moneymanager.service;

import com.example.moneymanager.dto.BudgetDTO;
import com.example.moneymanager.entity.BudgetEntity;
import com.example.moneymanager.entity.CategoryEntity;
import com.example.moneymanager.entity.ProfileEntity;
import com.example.moneymanager.repository.BudgetRepository;
import com.example.moneymanager.repository.CategoryRepository;
import com.example.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    public BudgetDTO createBudget(BudgetDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        if(dto.getCategoryId() == null){
            throw new RuntimeException("Category is required");
        }
        if(dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Budget amount must be greater than zero");
        }
        Integer month = dto.getMonth() != null ? dto.getMonth() : LocalDate.now().getMonthValue();
        Integer year = dto.getYear() != null ? dto.getYear() : LocalDate.now().getYear();

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        if(!"expense".equalsIgnoreCase(category.getType())){
            throw new RuntimeException("Budget can be created only for expense categories");
        }
        boolean exists = budgetRepository.existsByProfileIdAndCategoryIdAndMonthAndYear(
                profile.getId(),
                category.getId(),
                month,
                year
        );
        if(exists){
            throw new RuntimeException("Budget already exists for this category and month");
        }
        BudgetEntity budget = BudgetEntity.builder()
                .amount(dto.getAmount())
                .month(month)
                .year(year)
                .profile(profile)
                .category(category)
                .build();
        BudgetEntity savedBudget = budgetRepository.save(budget);
        return toDTO(savedBudget);
    }
    public List<BudgetDTO> getBudgets(Integer month, Integer year){
        ProfileEntity profile = profileService.getCurrentProfile();

        Integer selectedMonth = month != null ? month : LocalDate.now().getMonthValue();
        Integer selectedYear = year != null ? year : LocalDate.now().getYear();

        return budgetRepository.findBudgetsWithCategory(profile.getId(), selectedMonth, selectedYear).stream().map(this::toDTO).toList();
        //return budgetRepository.findByProfileIdAndMonthAndYear(profile.getId(), selectedMonth, selectedYear).stream().map(this::toDTO).toList();
    }
    public BudgetDTO updateBudget(Long id, BudgetDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();

        BudgetEntity budget = budgetRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(()-> new RuntimeException("Budget not found"));

        if(dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO)<= 0){
            throw new RuntimeException("Budget amount must be greater than zero");
        }
        budget.setAmount(dto.getAmount());
        return toDTO(budgetRepository.save(budget));
    }
    public void deleteBudget(Long id){
        ProfileEntity profile = profileService.getCurrentProfile();

        BudgetEntity budget = budgetRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(()-> new RuntimeException("Budget not found"));
        budgetRepository.delete(budget);
    }
    public List<BudgetDTO> getBudgetsForProfile(ProfileEntity profile, Integer month, Integer year){
        return budgetRepository
                .findBudgetsWithCategory(profile.getId(), month, year).stream().map(this::toDTO).toList();
    }
    //helper method
    private BudgetDTO toDTO(BudgetEntity budget){
        BigDecimal spentAmount = expenseRepository.getTotalExpenseByCategoryAndMonth(
                budget.getProfile().getId(),
                budget.getCategory().getId(),
                budget.getMonth(),
                budget.getYear()
        );
        BigDecimal remainingAmount = budget.getAmount().subtract(spentAmount);

        double usedPercentage = 0.0;

        if(budget.getAmount().compareTo(BigDecimal.ZERO) > 0){
            usedPercentage = spentAmount.multiply(BigDecimal.valueOf(100)).divide(budget.getAmount(), 2, RoundingMode.HALF_UP).doubleValue();
        }
        boolean exceeded = spentAmount.compareTo(budget.getAmount()) > 0;
        return BudgetDTO.builder()
                .id(budget.getId())
                .amount(budget.getAmount())
                .month(budget.getMonth())
                .year(budget.getYear())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .categoryIcon(budget.getCategory().getIcon())
                .spentAmount(spentAmount)
                .remainingAmount(remainingAmount)
                .usedPercentage(usedPercentage)
                .exceeded(exceeded)
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .build();
    }
}
