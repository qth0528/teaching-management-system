package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.Result;
import com.teaching.dto.StudentRequest;
import com.teaching.entity.Student;
import com.teaching.mapper.StudentMapper;
import com.teaching.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result<Map<String, Object>> list(String name, Long classId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Student> students = studentMapper.selectPage(name, classId, offset, pageSize);
        int total = studentMapper.countPage(name, classId);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("list", students);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> getById(Long id) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "学生不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("student", student);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> create(StudentRequest request) {
        // 手机号格式校验
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            if (!request.getPhone().matches("^1[3-9]\\d{9}$")) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "手机号格式错误");
            }
        }

        // 学号唯一性校验
        Student exist = studentMapper.selectByStudentNo(request.getStudentNo());
        if (exist != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "学号已存在");
        }

        Student student = new Student();
        student.setStudentNo(request.getStudentNo());
        student.setName(request.getName());
        student.setGender(request.getGender());
        student.setClassId(request.getClassId());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());

        studentMapper.insert(student);

        Map<String, Object> data = new HashMap<>();
        data.put("student", student);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> update(Long id, StudentRequest request) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "学生不存在");
        }

        // 手机号格式校验
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            if (!request.getPhone().matches("^1[3-9]\\d{9}$")) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "手机号格式错误");
            }
        }

        // 学号唯一性校验（排除自身）
        Student exist = studentMapper.selectByStudentNo(request.getStudentNo());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException(ErrorCode.CONFLICT, "学号已存在");
        }

        student.setStudentNo(request.getStudentNo());
        student.setName(request.getName());
        student.setGender(request.getGender());
        student.setClassId(request.getClassId());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());

        studentMapper.update(student);

        Map<String, Object> data = new HashMap<>();
        data.put("student", student);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> delete(Long id) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "学生不存在");
        }

        int attendanceCount = studentMapper.countAttendanceByStudentId(id);
        int homeworkCount = studentMapper.countHomeworkSubmissionByStudentId(id);
        int scoreCount = studentMapper.countScoreByStudentId(id);
        int totalAssociations = attendanceCount + homeworkCount + scoreCount;

        studentMapper.deleteById(id);

        Map<String, Object> data = new HashMap<>();
        data.put("attendanceCount", attendanceCount);
        data.put("homeworkCount", homeworkCount);
        data.put("scoreCount", scoreCount);
        data.put("totalAssociations", totalAssociations);
        return Result.success(data);
    }
}
