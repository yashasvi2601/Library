package com.MiniProject.Library_Management.controller;

import com.MiniProject.Library_Management.dto.DashboardStatsDto;
import com.MiniProject.Library_Management.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard/stats")
    public DashboardStatsDto getStats() {
        return adminService.getDashboardStats();
    }
}