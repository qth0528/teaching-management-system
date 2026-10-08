package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.dto.BatchScheduleRequest;
import com.teaching.dto.ScheduleRequest;
import com.teaching.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/weekly")
    public Result<Map<String, Object>> weekly(
            @RequestParam Long classId,
            @RequestParam String semester) {
        return scheduleService.weekly(classId, semester);
    }

    @PostMapping
    public Result<Map<String, Object>> create(@Valid @RequestBody ScheduleRequest request) {
        return scheduleService.create(request);
    }

    @PutMapping("/{id}")
    public Result<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request) {
        return scheduleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> delete(@PathVariable Long id) {
        return scheduleService.delete(id);
    }

    @PostMapping("/batch")
    public Result<Map<String, Object>> batch(@Valid @RequestBody BatchScheduleRequest request) {
        return scheduleService.batch(request);
    }
}
