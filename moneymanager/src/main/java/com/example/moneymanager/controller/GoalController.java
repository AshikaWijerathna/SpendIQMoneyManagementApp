package com.example.moneymanager.controller;

import com.example.moneymanager.dto.GoalDTO;
import com.example.moneymanager.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.createGoal(dto));
    }
    @GetMapping
    public ResponseEntity<?> getGoals(){
        return ResponseEntity.ok(goalService.getGoals());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody GoalDTO dto){
        return ResponseEntity.ok(goalService.updateGoal(id, dto));
    }
    @PatchMapping("/{id}/add-saving")
    public ResponseEntity<?> addSaving(@PathVariable Long id, @RequestBody Map<String, BigDecimal> body){
        return ResponseEntity.ok(goalService.addSaving(id, body.get("amount")));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id){
        goalService.deleteGoal(id);
        return ResponseEntity.ok("Goal deleted successfully");
    }
}
