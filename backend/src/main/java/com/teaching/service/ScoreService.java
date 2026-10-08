package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.dto.BatchScoreRequest;
import com.teaching.dto.ScoreRequest;

import java.util.Map;

public interface ScoreService {

    Result<Map<String, Object>> list(String studentName, Long classId, Long courseId,
                                      String examName, int page, int pageSize);

    Result<Map<String, Object>> getById(Long id);

    Result<Map<String, Object>> create(ScoreRequest request);

    Result<Map<String, Object>> update(Long id, ScoreRequest request);

    Result<Map<String, Object>> batchCreate(BatchScoreRequest request);

    Result<Map<String, Object>> stats(String examName, Long classId, Long courseId);
}
