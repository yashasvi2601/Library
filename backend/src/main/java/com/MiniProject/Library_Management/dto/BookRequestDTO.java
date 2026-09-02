package com.MiniProject.Library_Management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private Integer totalCopies;
}
