package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.dto.HomeworkRequest;
import com.teaching.dto.HomeworkReviewRequest;
import com.teaching.dto.HomeworkSubmissionRequest;
import com.teaching.dto.HomeworkUpdateRequest;
import com.teaching.service.HomeworkService;
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
@RequestMapping("/api/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return homeworkService.list(classId, courseId, page, pageSize);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        return homeworkService.getById(id);
    }

    @PostMapping
    public Result<Map<String, Object>> create(@Valid @RequestBody HomeworkRequest request) {
        return homeworkService.create(request);
    }

    @PutMapping("/{id}")
    public Result<Map<String, Object>> update(@PathVariable Long id,
                                               @Valid @RequestBody HomeworkUpdateRequest request) {
        return homeworkService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Result<Map<String, Object>> delete(@PathVariable Long id) {
        return homeworkService.delete(id);
    }

    @GetMapping("/{id}/submissions")
    public Result<Map<String, Object>> listSubmissions(@PathVariable Long id) {
        return homeworkService.listSubmissions(id);
    }

    @PostMapping("/submission")
    public Result<Map<String, Object>> submit(@Valid @RequestBody HomeworkSubmissionRequest request) {
        return homeworkService.submit(request);
    }

    @PutMapping("/submission/{id}")
    public Result<Map<String, Object>> review(@PathVariable Long id,
                                               @Valid @RequestBody HomeworkReviewRequest request) {
        return homeworkService.review(id, request);
    }
}
