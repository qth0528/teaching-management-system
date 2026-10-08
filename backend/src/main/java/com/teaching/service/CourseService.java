package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.dto.CourseRequest;

import java.util.Map;

public interface CourseService {

    Result<Map<String, Object>> list(String courseName, int page, int pageSize);

    Result<Map<String, Object>> all();

    Result<Map<String, Object>> getById(Long id);

    Result<Map<String, Object>> create(CourseRequest request);

    Result<Map<String, Object>> update(Long id, CourseRequest request);

    Result<Map<String, Object>> delete(Long id);
}
