package com.example.moneymanager.controller;

import com.example.moneymanager.dto.BudgetDTO;
import com.example.moneymanager.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<?> createBudget(@RequestBody BudgetDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(dto));
    }
    @GetMapping
    public ResponseEntity<?> getBudgets(@RequestParam(required = false) Integer month, @RequestParam(required = false) Integer year){
        return ResponseEntity.ok(budgetService.getBudgets(month,year));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBudget(@PathVariable Long id, @RequestBody BudgetDTO dto){
        return ResponseEntity.ok(budgetService.updateBudget(id,dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteBudget(@PathVariable Long id){
        budgetService.deleteBudget(id);
        return ResponseEntity.ok("Budget deleted successfully");
    }
}
