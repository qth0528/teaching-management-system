package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.dto.ClassRequest;

import java.util.Map;

public interface ClassService {

    Result<Map<String, Object>> list(String className, int page, int pageSize);

    Result<Map<String, Object>> all();

    Result<Map<String, Object>> getById(Long id);

    Result<Map<String, Object>> create(ClassRequest request);

    Result<Map<String, Object>> update(Long id, ClassRequest request);

    Result<Map<String, Object>> delete(Long id);

    Result<Map<String, Object>> getStudents(Long id);
}
