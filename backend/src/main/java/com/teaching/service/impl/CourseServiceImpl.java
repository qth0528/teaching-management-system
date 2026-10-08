package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.Result;
import com.teaching.dto.CourseRequest;
import com.teaching.entity.Course;
import com.teaching.mapper.CourseMapper;
import com.teaching.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Result<Map<String, Object>> list(String courseName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Course> courses = courseMapper.selectPage(courseName, offset, pageSize);
        int total = courseMapper.countPage(courseName);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("list", courses);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> all() {
        List<Course> courses = courseMapper.selectAll();
        Map<String, Object> data = new HashMap<>();
        data.put("list", courses);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> getById(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("course", course);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> create(CourseRequest request) {
        Course exist = courseMapper.selectByCourseName(request.getCourseName());
        if (exist != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "课程名称已存在");
        }

        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setFullScore(request.getFullScore());
        courseMapper.insert(course);

        Map<String, Object> data = new HashMap<>();
        data.put("course", course);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> update(Long id, CourseRequest request) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }

        Course exist = courseMapper.selectByCourseName(request.getCourseName());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException(ErrorCode.CONFLICT, "课程名称已存在");
        }

        course.setCourseName(request.getCourseName());
        course.setFullScore(request.getFullScore());
        courseMapper.update(course);

        Map<String, Object> data = new HashMap<>();
        data.put("course", course);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> delete(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }
        courseMapper.deleteById(id);
        return Result.success(null);
    }
}
