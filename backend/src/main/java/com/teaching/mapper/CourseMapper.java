package com.teaching.mapper;

import com.teaching.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {

    List<Course> selectPage(@Param("courseName") String courseName,
                            @Param("offset") int offset,
                            @Param("pageSize") int pageSize);

    int countPage(@Param("courseName") String courseName);

    List<Course> selectAll();

    Course selectById(@Param("id") Long id);

    Course selectByCourseName(@Param("courseName") String courseName);

    int insert(Course course);

    int update(Course course);

    int deleteById(@Param("id") Long id);
}
