package com.MiniProject.Library_Management.controller;

import com.MiniProject.Library_Management.dto.*;
import com.MiniProject.Library_Management.model.Transaction;
import com.MiniProject.Library_Management.service.CirculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/circulation")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService circulationService;

    @PostMapping("/checkout")
    public TransactionResponseDto checkout(@RequestBody CheckoutRequestDto dto) {
        return circulationService.checkoutBook(dto);
    }

    @PostMapping("/return")
    public TransactionResponseDto returnBook(@RequestBody ReturnRequestDto dto) {
        return circulationService.returnBook(dto);
    }

    @PostMapping("/renew")
    public TransactionResponseDto renew(@RequestBody RenewRequestDto dto) {
        return circulationService.renewBook(dto);
    }
    @GetMapping("/issued")
    public List<IssuedBookResponseDto> getIssuedBooks() {
        return circulationService.getIssuedBooks();
    }
    @GetMapping("/member/{memberId}")
    public List<MemberIssuedBookDto> getMemberBooks(@PathVariable Long memberId) {
        return circulationService.getBooksForMember(memberId);
    }
}