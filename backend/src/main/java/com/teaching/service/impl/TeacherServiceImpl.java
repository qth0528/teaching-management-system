package com.teaching.service.impl;

import com.teaching.common.Result;
import com.teaching.entity.Teacher;
import com.teaching.mapper.TeacherMapper;
import com.teaching.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Result<List<Teacher>> getAll() {
        List<Teacher> teachers = teacherMapper.selectAll();
        return Result.success(teachers);
    }
}
