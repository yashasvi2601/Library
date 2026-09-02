package com.MiniProject.Library_Management.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}