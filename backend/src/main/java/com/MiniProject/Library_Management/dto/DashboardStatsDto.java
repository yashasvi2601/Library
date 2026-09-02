package com.MiniProject.Library_Management.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {

    private Long totalBooks;
    private Long totalMembers;
    private Long issuedBooks;
    private Long totalFines;
}