package com.teaching.mapper;

import com.teaching.entity.HomeworkSubmission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomeworkSubmissionMapper {

    List<HomeworkSubmission> selectByHomeworkId(@Param("homeworkId") Long homeworkId);

    int insert(HomeworkSubmission submission);

    int batchInsert(@Param("list") List<HomeworkSubmission> submissions);

    int update(HomeworkSubmission submission);

    int deleteByHomeworkId(@Param("homeworkId") Long homeworkId);

    int countByHomeworkId(@Param("homeworkId") Long homeworkId);

    HomeworkSubmission selectById(@Param("id") Long id);

    HomeworkSubmission selectByHomeworkAndStudent(@Param("homeworkId") Long homeworkId,
                                                  @Param("studentId") Long studentId);

    int countPending();
}
