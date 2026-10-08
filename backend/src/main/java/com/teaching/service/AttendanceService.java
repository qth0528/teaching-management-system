package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.dto.AttendanceRequest;

import java.time.LocalDate;
import java.util.Map;

public interface AttendanceService {

    Result<Map<String, Object>> list(Long classId, LocalDate attendanceDate);

    Result<Map<String, Object>> create(AttendanceRequest request);

    Result<Map<String, Object>> batchPresent(Long classId, LocalDate attendanceDate);

    Result<Map<String, Object>> stats(Long classId, LocalDate attendanceDate);
}
