package com.teaching.service.impl;

import com.teaching.common.Result;
import com.teaching.entity.Clazz;
import com.teaching.mapper.AttendanceMapper;
import com.teaching.mapper.ClassMapper;
import com.teaching.mapper.HomeworkSubmissionMapper;
import com.teaching.mapper.StudentMapper;
import com.teaching.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private HomeworkSubmissionMapper homeworkSubmissionMapper;

    @Override
    public Result<Map<String, Object>> stats() {
        int studentCount = studentMapper.countAll();
        int classCount = classMapper.selectAll().size();
        int pendingHomeworkCount = homeworkSubmissionMapper.countPending();

        double todayAttendanceRate = 0.0;
        Map<String, Object> todayStats = attendanceMapper.selectDailyStats(LocalDate.now());
        if (todayStats != null) {
            Object totalObj = todayStats.get("total");
            Object presentObj = todayStats.get("present");
            long total = totalObj instanceof Long ? (Long) totalObj : ((Number) totalObj).longValue();
            long present = presentObj instanceof Long ? (Long) presentObj : ((Number) presentObj).longValue();
            if (total > 0) {
                todayAttendanceRate = new BigDecimal(present)
                        .multiply(new BigDecimal("100"))
                        .divide(new BigDecimal(total), 1, RoundingMode.HALF_UP)
                        .doubleValue();
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("studentCount", studentCount);
        data.put("classCount", classCount);
        data.put("todayAttendanceRate", todayAttendanceRate);
        data.put("pendingHomeworkCount", pendingHomeworkCount);
        return Result.success(data);
    }

    @Override
    public Result<Object> classDistribution() {
        List<Clazz> classes = classMapper.selectAll();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Clazz clazz : classes) {
            int count = studentMapper.countByClassId(clazz.getId());
            Map<String, Object> item = new HashMap<>();
            item.put("className", clazz.getClassName());
            item.put("studentCount", count);
            list.add(item);
        }
        return Result.success(list);
    }

    @Override
    public Result<Object> attendanceTrend() {
        List<Map<String, Object>> list = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Map<String, Object> stats = attendanceMapper.selectDailyStats(date);
            if (stats != null) {
                Object totalObj = stats.get("total");
                Object presentObj = stats.get("present");
                long total = totalObj instanceof Long ? (Long) totalObj : ((Number) totalObj).longValue();
                if (total > 0) {
                    long present = presentObj instanceof Long ? (Long) presentObj : ((Number) presentObj).longValue();
                    double rate = new BigDecimal(present)
                            .multiply(new BigDecimal("100"))
                            .divide(new BigDecimal(total), 1, RoundingMode.HALF_UP)
                            .doubleValue();
                    Map<String, Object> item = new HashMap<>();
                    item.put("date", date.toString());
                    item.put("rate", rate);
                    list.add(item);
                }
            }
        }

        return Result.success(list);
    }
}
