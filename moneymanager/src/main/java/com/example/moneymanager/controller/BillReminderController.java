package com.example.moneymanager.controller;

import com.example.moneymanager.dto.BillReminderDTO;
import com.example.moneymanager.service.BillReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillReminderController {

    private final BillReminderService billReminderService;

    @PostMapping
    public ResponseEntity<?> createBill(@RequestBody BillReminderDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(billReminderService.createBill(dto));
    }
    @GetMapping
    public ResponseEntity<?> getBills(){
        return ResponseEntity.ok(billReminderService.getBills());
    }
    @PutMapping("/{id}/paid")
    public ResponseEntity<?>markAsPaid(@PathVariable Long id){
        return ResponseEntity.ok(billReminderService.markAsPaid(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Long id){
        billReminderService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted successfully");
    }
}
