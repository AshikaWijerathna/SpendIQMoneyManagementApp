package com.example.moneymanager.repository;

import com.example.moneymanager.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<GoalEntity, Long>{
    List<GoalEntity> findByProfileIdOrderByCreatedAtDesc(Long profileId);
    Optional<GoalEntity> findByIdAndProfileId(Long id, Long profileId);
}
