package com.MiniProject.Library_Management.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MemberResponseDto {
    private Long memberId;
    private String name;
    private String email;
    private Integer booksIssued;
    private Integer maxBooksAllowed;
    private BigDecimal pendingFineAmount;
}