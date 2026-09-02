package com.MiniProject.Library_Management.dto;

import lombok.Data;

@Data
public class CheckoutRequestDto {
    private Long memberId;
    private Long bookId;
}