package com.MiniProject.Library_Management.dto;

import com.MiniProject.Library_Management.model.CopyCondition;
import com.MiniProject.Library_Management.model.CopyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCopyResponseDto {
    private Long copyId;
    private Long bookId;
    private CopyStatus status;
    private CopyCondition condition;
}