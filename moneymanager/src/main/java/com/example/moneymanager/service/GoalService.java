package com.example.moneymanager.service;

import com.example.moneymanager.dto.GoalDTO;
import com.example.moneymanager.entity.GoalEntity;
import com.example.moneymanager.entity.ProfileEntity;
import com.example.moneymanager.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final ProfileService profileService;

    public GoalDTO createGoal(GoalDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();

        if(dto.getGoalName() == null || dto.getGoalName().trim().isEmpty()){
            throw new RuntimeException("Goal name is required");
        }
        if(dto.getTargetAmount() == null || dto.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("target amount must be greater than zero");
        }
        if(dto.getDeadline() == null){
            throw new RuntimeException("Deadline is required.");
        }
        BigDecimal currentAmount = dto.getCurrentAmount() != null ? dto.getCurrentAmount() : BigDecimal.ZERO;

        GoalEntity goal = GoalEntity.builder()
                .goalName(dto.getGoalName())
                .targetAmount(dto.getTargetAmount())
                .currentAmount(currentAmount)
                .deadline(dto.getDeadline())
                .completed(false)
                .profile(profile)
                .build();
        checkGoalCompletion(goal);
        return toDTO(goalRepository.save(goal));
    }
    public List<GoalDTO> getGoals(){
        ProfileEntity profile = profileService.getCurrentProfile();
        return goalRepository.findByProfileIdOrderByCreatedAtDesc(profile.getId()).stream().map(this::toDTO).toList();
    }
    public GoalDTO updateGoal(Long id, GoalDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();

        GoalEntity goal = goalRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(()-> new RuntimeException("Goal not found"));
        if(dto.getGoalName() != null){
            goal.setGoalName(dto.getGoalName());
        }
        if(dto.getTargetAmount() != null){
            goal.setTargetAmount(dto.getTargetAmount());
        }
        if(dto.getDeadline() != null){
            goal.setDeadline(dto.getDeadline());
        }
        checkGoalCompletion(goal);
        return toDTO(goalRepository.save(goal));
    }
    public GoalDTO addSaving(Long id, BigDecimal amount){
        ProfileEntity profile = profileService.getCurrentProfile();
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Saving amount must be greater than zero");
        }
        GoalEntity goal = goalRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(() -> new RuntimeException("Goal not found"));
        goal.setCurrentAmount(goal.getCurrentAmount().add(amount));
        checkGoalCompletion(goal);
        return toDTO(goalRepository.save(goal));
    }
    public List<GoalDTO> getGoalsForProfile(ProfileEntity profile){
        return goalRepository
                .findByProfileIdOrderByCreatedAtDesc(profile.getId()).stream().map(this::toDTO).toList();
    }
    public void deleteGoal(Long id){
        ProfileEntity profile = profileService.getCurrentProfile();
        GoalEntity goal = goalRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(()-> new RuntimeException("Goal not found"));
        goalRepository.delete(goal);
    }
    //helper method
    private void checkGoalCompletion(GoalEntity goal){
        if(goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0){
            goal.setCompleted(true);
            if(goal.getCompletedAt() == null){
                goal.setCompletedAt(LocalDate.now());
            }
        }else{
            goal.setCompleted(false);
            goal.setCompletedAt(null);
        }
    }
    private GoalDTO toDTO(GoalEntity goal){
        BigDecimal remainingAmount = goal.getTargetAmount().subtract(goal.getCurrentAmount());

        if(remainingAmount.compareTo(BigDecimal.ZERO) < 0){
            remainingAmount = BigDecimal.ZERO;
        }
        double progressPercentage = 0.0;

        if(goal.getTargetAmount().compareTo(BigDecimal.ZERO) > 0){
            progressPercentage = goal.getCurrentAmount().multiply(BigDecimal.valueOf(100)).divide(goal.getTargetAmount(), 2, RoundingMode.HALF_UP).doubleValue();
        }
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), goal.getDeadline());

        return GoalDTO.builder()
                .id(goal.getId())
                .goalName(goal.getGoalName())
                .targetAmount(goal.getTargetAmount())
                .currentAmount(goal.getCurrentAmount())
                .deadline(goal.getDeadline())
                .progressPercentage(progressPercentage)
                .remainingAmount(remainingAmount)
                .daysRemaining(daysRemaining)
                .completed(goal.getCompleted())
                .completedAt(goal.getCompletedAt())
                .createdAt(goal.getCreatedAt())
                .updatedAt(goal.getUpdatedAt())
                .build();
    }
}
