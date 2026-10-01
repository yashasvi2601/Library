package com.MiniProject.Library_Management.dto;

import com.MiniProject.Library_Management.model.Role;
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
    private Role role;
}