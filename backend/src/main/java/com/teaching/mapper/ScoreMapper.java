package com.teaching.mapper;

import com.teaching.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScoreMapper {

    List<Score> selectPage(@Param("studentName") String studentName,
                           @Param("classId") Long classId,
                           @Param("courseId") Long courseId,
                           @Param("examName") String examName,
                           @Param("offset") int offset,
                           @Param("pageSize") int pageSize);

    int countPage(@Param("studentName") String studentName,
                  @Param("classId") Long classId,
                  @Param("courseId") Long courseId,
                  @Param("examName") String examName);

    Score selectById(@Param("id") Long id);

    int insert(Score score);

    int update(Score score);

    int batchInsert(@Param("list") List<Score> scores);

    int countByStudentExamCourse(@Param("studentId") Long studentId,
                                 @Param("examName") String examName,
                                 @Param("courseId") Long courseId);

    Map<String, Object> selectStats(@Param("examName") String examName,
                                    @Param("classId") Long classId,
                                    @Param("courseId") Long courseId);
}
