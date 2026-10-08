package com.teaching.controller;

import com.teaching.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Result<Map<String, Object>> home() {
        Map<String, Object> data = new HashMap<>();
        data.put("app", "教学数字平台管理系统 API");
        data.put("version", "1.0.0");
        data.put("status", "running");
        return Result.success(data);
    }
}
