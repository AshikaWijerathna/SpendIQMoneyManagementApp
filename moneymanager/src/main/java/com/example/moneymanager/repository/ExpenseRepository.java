package com.example.moneymanager.repository;

import com.example.moneymanager.dto.CategoryReportDTO;
import com.example.moneymanager.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    //select * from tbl_expenses where profile_id = ?1 order by date desc
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    //select * from tbl_expenses where profile_id = ?1 order by date desc limit 5
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    //select * from tbl_expenses where profile_id = ?1 and date between ?2 and ?3 and name like  %?4%
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );
    //select * from tbl_expenses where profile_id = ?1 and date between ?2 and ?3
    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    //select * from tbl_expenses where profile_id = ?1 and date = ?2
    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM ExpenseEntity e " +
            "WHERE e.profile.id = :profileId " +
            "AND e.category.id = :categoryId " +
            "AND MONTH(e.date) = :month " +
            "AND YEAR(e.date) = :year ")
    BigDecimal getTotalExpenseByCategoryAndMonth(
            @Param("profileId") Long profileId,
            @Param("categoryId") Long categoryId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e " +
    "WHERE e.profile.id = :profileId " +
    "AND MONTH(e.date) = :month " + "AND YEAR(e.date) = :year")
    BigDecimal getMonthlyExpense(
            @Param("profileId") Long profileId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e " +
    "WHERE e.profile.id = :profileId " + "AND YEAR(e.date) = :year")
    BigDecimal getYearlyExpense(
            @Param("profileId") Long profileId,
            @Param("year") Integer year
    );
    @Query("SELECT new com.example.moneymanager.dto.CategoryReportDTO(" +
    "c.id, c.name,c.icon, COALESCE(SUM(e.amount),0)) " +
    "FROM ExpenseEntity e JOIN e.category c " + "WHERE e.profile.id = :profileId " + "AND MONTH(e.date) = :month " + "AND YEAR(e.date) = :year " + "GROUP BY c.id, c.name,c.icon")
    List<CategoryReportDTO>getCategoryWiseExpense(
            @Param("profileId") Long profileId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );
}
