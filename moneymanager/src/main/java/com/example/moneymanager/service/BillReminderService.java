package com.example.moneymanager.service;

import com.example.moneymanager.dto.BillReminderDTO;
import com.example.moneymanager.entity.BillReminderEntity;
import com.example.moneymanager.entity.ProfileEntity;
import com.example.moneymanager.repository.BillReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillReminderService {
    private final BillReminderRepository billReminderRepository;
    private final ProfileService profileService;

    public BillReminderDTO createBill(BillReminderDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
               BillReminderEntity bill = BillReminderEntity.builder()
                .billName(dto.getBillName())
                .amount(dto.getAmount())
                .dueDate(dto.getDueDate())
                .paid(false)
                .reminderSent(false)
                .profile(profile)
                .build();

        return toDTO(billReminderRepository.save(bill));
    }
    public List<BillReminderDTO> getBills(){
        ProfileEntity profile = profileService.getCurrentProfile();
        return billReminderRepository
                .findByProfileIdOrderByDueDateAsc(profile.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }
    public BillReminderDTO markAsPaid(Long id){
        ProfileEntity profile = profileService.getCurrentProfile();

        BillReminderEntity bill = billReminderRepository
                .findByIdAndProfileId(id, profile.getId())
                .orElseThrow(()-> new RuntimeException("Bill not found"));
        bill.setPaid(true);
        return toDTO(billReminderRepository.save(bill));
    }
    public void deleteBill(Long id){
        ProfileEntity profile = profileService.getCurrentProfile();

        BillReminderEntity bill = billReminderRepository
                .findByIdAndProfileId(id,profile.getId())
                .orElseThrow(()-> new RuntimeException("Bill not found"));
        billReminderRepository.delete(bill);
    }
    //helper method
    private BillReminderDTO toDTO(BillReminderEntity bill){
        return BillReminderDTO.builder()
                .id(bill.getId())
                .billName(bill.getBillName())
                .amount((bill.getAmount()))
                .dueDate(bill.getDueDate())
                .paid(bill.getPaid())
                .reminderSent(bill.getReminderSent())
                .build();
    }
}
