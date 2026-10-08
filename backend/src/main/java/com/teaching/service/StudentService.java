package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.dto.StudentRequest;

import java.util.Map;

public interface StudentService {

    Result<Map<String, Object>> list(String name, Long classId, int page, int pageSize);

    Result<Map<String, Object>> getById(Long id);

    Result<Map<String, Object>> create(StudentRequest request);

    Result<Map<String, Object>> update(Long id, StudentRequest request);

    Result<Map<String, Object>> delete(Long id);
}
