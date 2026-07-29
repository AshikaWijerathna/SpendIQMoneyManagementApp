package com.example.moneymanager.controller;

import com.example.moneymanager.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/monthly-summary")
    public ResponseEntity<?> getMonthlySummary(@RequestParam(required = false) Integer month, @RequestParam(required = false) Integer year){
        return ResponseEntity.ok(reportService.getMonthlySummary(month,year));
    }
    @GetMapping("/yearly-summary")
    public ResponseEntity<?> getYearlySummary(@RequestParam(required = false) Integer year){
        return ResponseEntity.ok(reportService.getYearlySummary(year));
    }
    @GetMapping("/yearly-monthly")
    public ResponseEntity<?> getYearlyMonthlyReport(@RequestParam(required = false) Integer year){
        return ResponseEntity.ok(reportService.getYearlyMonthlyReport(year));
    }
    @GetMapping("/category-wise")
    public ResponseEntity<?> getCategoryWiseSpending(@RequestParam(required = false) Integer month,@RequestParam(required = false) Integer year){
        return ResponseEntity.ok(reportService.getCategoryWiseSpending(month,year));
    }
}
