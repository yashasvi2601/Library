package com.MiniProject.Library_Management.controller;

import com.MiniProject.Library_Management.dto.FineTableResponseDto;
import com.MiniProject.Library_Management.dto.PayFineRequestDto;
import com.MiniProject.Library_Management.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping("/pay")
    public FineTableResponseDto payFine(@RequestBody PayFineRequestDto dto, Authentication authentication) {
        return fineService.payFine(dto, authentication);
    }

    @GetMapping("/my")
    public List<FineTableResponseDto> getMyPendingFines(Authentication authentication) {
        return fineService.getMyPendingFines(authentication);
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