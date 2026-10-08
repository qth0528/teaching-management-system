package com.teaching.mapper;

import com.teaching.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<Student> selectPage(@Param("name") String name,
                             @Param("classId") Long classId,
                             @Param("offset") int offset,
                             @Param("pageSize") int pageSize);

    int countPage(@Param("name") String name,
                  @Param("classId") Long classId);

    Student selectById(@Param("id") Long id);

    Student selectByStudentNo(@Param("studentNo") String studentNo);

    int insert(Student student);

    int update(Student student);

    int deleteById(@Param("id") Long id);

    int countByClassId(@Param("classId") Long classId);

    int countAttendanceByStudentId(@Param("studentId") Long studentId);

    int countHomeworkSubmissionByStudentId(@Param("studentId") Long studentId);

    int countScoreByStudentId(@Param("studentId") Long studentId);

    List<Student> selectByClassId(@Param("classId") Long classId);

    int countAll();
}
