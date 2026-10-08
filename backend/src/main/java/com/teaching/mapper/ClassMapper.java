package com.teaching.mapper;

import com.teaching.entity.Clazz;
import com.teaching.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassMapper {

    List<Clazz> selectPage(@Param("className") String className,
                           @Param("offset") int offset,
                           @Param("pageSize") int pageSize);

    int countPage(@Param("className") String className);

    List<Clazz> selectAll();

    Clazz selectById(@Param("id") Long id);

    Clazz selectByClassName(@Param("className") String className);

    int insert(Clazz clazz);

    int update(Clazz clazz);

    int deleteById(@Param("id") Long id);

    int countStudentsByClassId(@Param("classId") Long classId);

    List<Student> selectStudentsByClassId(@Param("classId") Long classId);
}
