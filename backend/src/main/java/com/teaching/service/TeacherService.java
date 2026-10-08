package com.teaching.service;

import com.teaching.common.Result;
import com.teaching.entity.Teacher;

import java.util.List;

public interface TeacherService {

    Result<List<Teacher>> getAll();
}
