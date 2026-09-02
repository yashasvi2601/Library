package com.MiniProject.Library_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberIssuedBookDto {
    private Long txnId;
    private Long bookId;
    private String title;
    private LocalDateTime issuedAt;
    private LocalDate dueDate;
    private long daysLeft;
    private BigDecimal currentFine;
}
