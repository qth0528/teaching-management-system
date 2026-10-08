package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.dto.BatchScoreRequest;
import com.teaching.dto.ScoreRequest;
import com.teaching.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "") String studentName,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "") String examName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return scoreService.list(studentName, classId, courseId, examName, page, pageSize);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return scoreService.getById(id);
    }

    @PostMapping
    public Result<Map<String, Object>> create(@Valid @RequestBody ScoreRequest request) {
        return scoreService.create(request);
    }

    @PutMapping("/{id}")
    public Result<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody ScoreRequest request) {
        return scoreService.update(id, request);
    }

    @PostMapping("/batch")
    public Result<Map<String, Object>> batchCreate(@Valid @RequestBody BatchScoreRequest request) {
        return scoreService.batchCreate(request);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats(
            @RequestParam String examName,
            @RequestParam Long classId,
            @RequestParam Long courseId) {
        return scoreService.stats(examName, classId, courseId);
    }
}
