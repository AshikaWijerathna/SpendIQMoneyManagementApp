package com.example.moneymanager.repository;

import com.example.moneymanager.entity.BillReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillReminderRepository extends JpaRepository<BillReminderEntity,Long> {
    List<BillReminderEntity> findByProfileIdOrderByDueDateAsc(Long profileId);
    Optional<BillReminderEntity> findByIdAndProfileId(Long id, Long profileId);
  //  List<BillReminderEntity> findByPaidFalseAndReminderSentFalseAndDueDateLessThanEqual(LocalDate date);
    @Query("SELECT b FROM BillReminderEntity b JOIN FETCH b.profile WHERE b.paid = false AND b.reminderSent = false AND b.dueDate <= :date")
    List<BillReminderEntity>findDueBillsWithProfile(@Param("date") LocalDate date);

}
