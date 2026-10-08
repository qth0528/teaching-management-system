package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.dto.BatchScheduleRequest;
import com.teaching.dto.ScheduleRequest;

import java.util.Map;

public interface ScheduleService {

    Result<Map<String, Object>> weekly(Long classId, String semester);

    Result<Map<String, Object>> create(ScheduleRequest request);

    Result<Map<String, Object>> update(Long id, ScheduleRequest request);

    Result<Map<String, Object>> delete(Long id);

    Result<Map<String, Object>> batch(BatchScheduleRequest request);
}
