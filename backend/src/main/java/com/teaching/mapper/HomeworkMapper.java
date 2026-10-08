package com.teaching.mapper;

import com.teaching.entity.Homework;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomeworkMapper {

    List<Homework> selectPage(@Param("classId") Long classId,
                              @Param("courseId") Long courseId,
                              @Param("offset") int offset,
                              @Param("pageSize") int pageSize);

    int countPage(@Param("classId") Long classId,
                  @Param("courseId") Long courseId);

    Homework selectById(@Param("id") Long id);

    int insert(Homework homework);

    int update(Homework homework);

    int deleteById(@Param("id") Long id);
}
