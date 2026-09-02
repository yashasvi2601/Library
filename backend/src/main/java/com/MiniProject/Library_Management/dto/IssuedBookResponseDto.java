package com.MiniProject.Library_Management.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuedBookResponseDto {
    private String memberName;
    private String email;
    private String bookTitle;
    private Long copyId;
    private String status;
}