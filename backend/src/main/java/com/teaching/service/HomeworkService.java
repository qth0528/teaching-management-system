package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.dto.HomeworkRequest;
import com.teaching.dto.HomeworkReviewRequest;
import com.teaching.dto.HomeworkSubmissionRequest;
import com.teaching.dto.HomeworkUpdateRequest;

import java.util.Map;

public interface HomeworkService {

    Result<Map<String, Object>> list(Long classId, Long courseId, int page, int pageSize);

    Result<Map<String, Object>> getById(Long id);

    Result<Map<String, Object>> create(HomeworkRequest request);

    Result<Map<String, Object>> update(Long id, HomeworkUpdateRequest request);

    Result<Map<String, Object>> delete(Long id);

    Result<Map<String, Object>> listSubmissions(Long homeworkId);

    Result<Map<String, Object>> submit(HomeworkSubmissionRequest request);

    Result<Map<String, Object>> review(Long id, HomeworkReviewRequest request);
}
