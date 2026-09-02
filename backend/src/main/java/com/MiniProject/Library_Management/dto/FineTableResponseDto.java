package com.MiniProject.Library_Management.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FineTableResponseDto {
    private Long fineId;
    private String memberName;
    private String email;
    private BigDecimal amount;
    private LocalDateTime paidAt;
}