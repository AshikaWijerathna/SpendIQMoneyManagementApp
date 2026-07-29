package com.example.moneymanager.repository;

import com.example.moneymanager.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

    List<BudgetEntity> findByProfileIdAndMonthAndYear(Long profileId, Integer month, Integer year);

    @Query("SELECT b FROM BudgetEntity b JOIN FETCH b.category WHERE b.profile.id = :profileId AND b.month = :month AND b.year = :year")List<BudgetEntity>findBudgetsWithCategory(@Param("profileId") Long profileId,@Param("month") Integer month,@Param("year")Integer year);

    Optional<BudgetEntity> findByIdAndProfileId(Long id, Long profileId);

    boolean existsByProfileIdAndCategoryIdAndMonthAndYear(
            Long profileId,
            Long categoryId,
            Integer month,
            Integer year
    );

}
