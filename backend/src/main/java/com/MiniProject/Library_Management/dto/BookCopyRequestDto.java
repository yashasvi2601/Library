package com.MiniProject.Library_Management.dto;

import com.MiniProject.Library_Management.model.CopyCondition;
import com.MiniProject.Library_Management.model.CopyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCopyRequestDto {
    private Long bookId;
    private CopyStatus status;
    private CopyCondition condition;
}