package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.Result;
import com.teaching.dto.AttendanceRequest;
import com.teaching.entity.Attendance;
import com.teaching.mapper.AttendanceMapper;
import com.teaching.mapper.ClassMapper;
import com.teaching.mapper.StudentMapper;
import com.teaching.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result<Map<String, Object>> list(Long classId, LocalDate attendanceDate) {
        if (classMapper.selectById(classId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        List<Attendance> list = attendanceMapper.selectByClassAndDate(classId, attendanceDate);

        Map<String, Object> data = new HashMap<>();
        data.put("classId", classId);
        data.put("attendanceDate", attendanceDate);
        data.put("list", list);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> create(AttendanceRequest request) {
        validateDate(request.getAttendanceDate());

        if (classMapper.selectById(request.getClassId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }
        if (studentMapper.selectById(request.getStudentId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "学生不存在");
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(request.getStudentId());
        attendance.setClassId(request.getClassId());
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());

        attendanceMapper.insertOrUpdate(attendance);

        Map<String, Object> data = new HashMap<>();
        data.put("attendance", attendance);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> batchPresent(Long classId, LocalDate attendanceDate) {
        validateDate(attendanceDate);

        if (classMapper.selectById(classId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        int count = attendanceMapper.batchInsertPresent(classId, attendanceDate);

        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> stats(Long classId, LocalDate attendanceDate) {
        if (classMapper.selectById(classId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        // Total students in class
        int totalStudents = studentMapper.countByClassId(classId);

        Map<String, Object> stats = attendanceMapper.selectStats(classId, attendanceDate);

        Map<String, Object> data = new HashMap<>();
        data.put("classId", classId);
        data.put("attendanceDate", attendanceDate);
        data.put("totalStudents", totalStudents);
        data.put("stats", stats);
        return Result.success(data);
    }

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "不允许补录过去日期的考勤");
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "非工作日不可录入考勤");
        }
    }
}
