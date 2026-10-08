package com.teaching.controller;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.JwtUtil;
import com.teaching.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teaching.mapper.TestMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TestMapper testMapper;

    @GetMapping("/success")
    public Result<String> success() {
        return Result.success("test data");
    }

    @GetMapping("/conflict")
    public Result<Void> conflict() {
        throw new BusinessException(ErrorCode.CONFLICT, "班级名称已存在");
    }

    @GetMapping("/not-found")
    public Result<Void> notFound() {
        throw new BusinessException(ErrorCode.NOT_FOUND, "学生不存在");
    }

    @GetMapping("/error")
    public Result<Void> error() {
        throw new RuntimeException("未知异常");
    }

    @GetMapping("/db")
    public Result<Integer> testDb() {
        Integer result = testMapper.selectOne();
        return Result.success(result);
    }

    @GetMapping("/token")
    public Result<Map<String, String>> generateToken(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "1") Long teacherId) {
        String token = jwtUtil.generateToken(userId, teacherId);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", String.valueOf(userId));
        data.put("teacherId", String.valueOf(teacherId));
        return Result.success(data);
    }
}
