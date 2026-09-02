package com.MiniProject.Library_Management.controller;

import com.MiniProject.Library_Management.dto.FineTableResponseDto;
import com.MiniProject.Library_Management.dto.PayFineRequestDto;
import com.MiniProject.Library_Management.model.Fine;
import com.MiniProject.Library_Management.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping("/pay")
    public Fine payFine(@RequestBody PayFineRequestDto dto) {
        return fineService.payFine(dto);
    }
    @GetMapping("/pending")
    public List<FineTableResponseDto> getPendingFines() {
        return fineService.getPendingFines();
    }

    @GetMapping("/history")
    public List<FineTableResponseDto> getFineHistory() {
        return fineService.getFineHistory();
    }

    @DeleteMapping("/{fineId}")
    public String deleteFine(@PathVariable Long fineId) {
        fineService.deleteFine(fineId);
        return "Fine deleted successfully";
    }
}