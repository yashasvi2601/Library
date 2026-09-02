package com.MiniProject.Library_Management;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class TestHash {
    public static void main(String[] args) {
        System.out.println(
                new BCryptPasswordEncoder().encode("admin123")
        );
    }
}