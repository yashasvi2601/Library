package com.MiniProject.Library_Management.dto;

import lombok.Data;

@Data
public class MemberRequestDto {
    private String name;
    private String email;
    private String password;
    private Integer maxBooksAllowed;
}