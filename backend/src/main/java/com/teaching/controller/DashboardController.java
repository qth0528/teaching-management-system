package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        return dashboardService.stats();
    }

    @GetMapping("/class-distribution")
    public Result<Object> classDistribution() {
        return dashboardService.classDistribution();
    }

    @GetMapping("/attendance-trend")
    public Result<Object> attendanceTrend() {
        return dashboardService.attendanceTrend();
    }
}
