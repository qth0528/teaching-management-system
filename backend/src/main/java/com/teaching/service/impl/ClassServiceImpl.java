package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.Result;
import com.teaching.dto.ClassRequest;
import com.teaching.entity.Clazz;
import com.teaching.entity.Student;
import com.teaching.mapper.ClassMapper;
import com.teaching.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    public Result<Map<String, Object>> list(String className, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Clazz> classes = classMapper.selectPage(className, offset, pageSize);
        int total = classMapper.countPage(className);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("list", classes);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> all() {
        List<Clazz> classes = classMapper.selectAll();
        return Result.success(new HashMap<String, Object>() {{
            put("list", classes);
        }});
    }

    @Override
    public Result<Map<String, Object>> getById(Long id) {
        Clazz clazz = classMapper.selectById(id);
        if (clazz == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("class", clazz);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> create(ClassRequest request) {
        Clazz exist = classMapper.selectByClassName(request.getClassName());
        if (exist != null) {
            throw new BusinessException(ErrorCode.CONFLICT, "班级名称已存在");
        }

        Clazz clazz = new Clazz();
        clazz.setClassName(request.getClassName());
        clazz.setTeacherId(request.getTeacherId());
        clazz.setRoomNumber(request.getRoomNumber());

        classMapper.insert(clazz);

        Map<String, Object> data = new HashMap<>();
        data.put("class", clazz);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> update(Long id, ClassRequest request) {
        Clazz clazz = classMapper.selectById(id);
        if (clazz == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        Clazz exist = classMapper.selectByClassName(request.getClassName());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException(ErrorCode.CONFLICT, "班级名称已存在");
        }

        clazz.setClassName(request.getClassName());
        clazz.setTeacherId(request.getTeacherId());
        clazz.setRoomNumber(request.getRoomNumber());

        classMapper.update(clazz);

        Map<String, Object> data = new HashMap<>();
        data.put("class", clazz);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> delete(Long id) {
        Clazz clazz = classMapper.selectById(id);
        if (clazz == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        int studentCount = classMapper.countStudentsByClassId(id);
        if (studentCount > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "该班级下还有 " + studentCount + " 名学生，请先将学生移出班级后再删除");
        }

        classMapper.deleteById(id);

        return Result.success(null);
    }

    @Override
    public Result<Map<String, Object>> getStudents(Long id) {
        Clazz clazz = classMapper.selectById(id);
        if (clazz == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        List<Student> students = classMapper.selectStudentsByClassId(id);

        Map<String, Object> data = new HashMap<>();
        data.put("classId", id);
        data.put("className", clazz.getClassName());
        data.put("list", students);
        return Result.success(data);
    }
}
