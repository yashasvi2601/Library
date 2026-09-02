package com.MiniProject.Library_Management.service;

import com.MiniProject.Library_Management.dto.DashboardStatsDto;
import com.MiniProject.Library_Management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final FineRepository fineRepository;

    public DashboardStatsDto getDashboardStats() {
        return DashboardStatsDto.builder()
                .totalBooks(bookRepository.count())
                .totalMembers(memberRepository.count())
                .issuedBooks(transactionRepository.countByReturnedAtIsNull())
                .totalFines(fineRepository.count())
                .build();
    }
}