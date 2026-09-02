package com.MiniProject.Library_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long bookId;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private Integer totalCopies;
    private Integer availableCopies;
}
