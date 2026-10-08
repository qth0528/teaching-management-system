package com.teaching.service;

import com.teaching.common.Result;

import java.util.Map;

public interface DashboardService {

    Result<Map<String, Object>> stats();

    Result<Object> classDistribution();

    Result<Object> attendanceTrend();
}
