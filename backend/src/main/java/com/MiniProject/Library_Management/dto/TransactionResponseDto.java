package com.MiniProject.Library_Management.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDto {
    private Long txnId;
    private Long copyId;
    private Long memberId;
    private LocalDateTime issuedAt;
    private LocalDate dueDate;
    private LocalDateTime returnedAt;
    private BigDecimal fineAmount;
    private String message;
}