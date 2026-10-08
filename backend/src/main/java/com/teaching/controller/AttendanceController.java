package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.dto.AttendanceRequest;
import com.teaching.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam Long classId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate attendanceDate) {
        return attendanceService.list(classId, attendanceDate);
    }

    @PostMapping
    public Result<Map<String, Object>> create(@Valid @RequestBody AttendanceRequest request) {
        return attendanceService.create(request);
    }

    @PutMapping("/batch")
    public Result<Map<String, Object>> batchPresent(
            @RequestParam Long classId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate attendanceDate) {
        return attendanceService.batchPresent(classId, attendanceDate);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats(
            @RequestParam Long classId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate attendanceDate) {
        return attendanceService.stats(classId, attendanceDate);
    }
}
