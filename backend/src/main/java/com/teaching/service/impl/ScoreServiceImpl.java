package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.Result;
import com.teaching.dto.BatchScoreRequest;
import com.teaching.dto.ScoreRequest;
import com.teaching.entity.Course;
import com.teaching.entity.Score;
import com.teaching.mapper.ClassMapper;
import com.teaching.mapper.CourseMapper;
import com.teaching.mapper.ScoreMapper;
import com.teaching.mapper.StudentMapper;
import com.teaching.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public Result<Map<String, Object>> list(String studentName, Long classId, Long courseId,
                                            String examName, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Score> scores = scoreMapper.selectPage(studentName, classId, courseId, examName, offset, pageSize);
        int total = scoreMapper.countPage(studentName, classId, courseId, examName);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("list", scores);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> getById(Long id) {
        Score score = scoreMapper.selectById(id);
        if (score == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "成绩记录不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("score", score);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> create(ScoreRequest request) {
        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }

        if (classMapper.selectById(request.getClassId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        if (studentMapper.selectById(request.getStudentId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "学生不存在");
        }

        validateScore(request.getScore(), course);

        // 唯一性校验
        int existCount = scoreMapper.countByStudentExamCourse(
                request.getStudentId(), request.getExamName(), request.getCourseId());
        if (existCount > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "该学生在此次考试中该科目已有成绩记录");
        }

        Score score = buildScore(request);
        scoreMapper.insert(score);

        Map<String, Object> data = new HashMap<>();
        data.put("score", score);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> update(Long id, ScoreRequest request) {
        Score exist = scoreMapper.selectById(id);
        if (exist == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "成绩记录不存在");
        }

        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }

        validateScore(request.getScore(), course);

        exist.setScore(request.getScore());
        scoreMapper.update(exist);

        Map<String, Object> data = new HashMap<>();
        data.put("score", exist);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> batchCreate(BatchScoreRequest request) {
        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }

        if (classMapper.selectById(request.getClassId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        List<Score> scoreList = new ArrayList<>();
        for (BatchScoreRequest.StudentScoreItem item : request.getScores()) {
            if (studentMapper.selectById(item.getStudentId()) == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "学生ID " + item.getStudentId() + " 不存在");
            }

            validateScore(item.getScore(), course);

            // 检查该学生在此次考试此科目是否已有记录
            int existCount = scoreMapper.countByStudentExamCourse(
                    item.getStudentId(), request.getExamName(), request.getCourseId());
            if (existCount > 0) {
                throw new BusinessException(ErrorCode.CONFLICT,
                        "学生ID " + item.getStudentId() + " 在此次考试中该科目已有成绩记录");
            }

            Score score = new Score();
            score.setStudentId(item.getStudentId());
            score.setClassId(request.getClassId());
            score.setCourseId(request.getCourseId());
            score.setExamName(request.getExamName());
            score.setExamType(request.getExamType());
            score.setScore(item.getScore());
            score.setExamDate(request.getExamDate());
            scoreList.add(score);
        }

        if (!scoreList.isEmpty()) {
            scoreMapper.batchInsert(scoreList);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("count", scoreList.size());
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> stats(String examName, Long classId, Long courseId) {
        if (classMapper.selectById(classId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }

        if (courseMapper.selectById(courseId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }

        Course course = courseMapper.selectById(courseId);
        Map<String, Object> statsMap = scoreMapper.selectStats(examName, classId, courseId);

        if (statsMap == null) {
            statsMap = new HashMap<>();
        }

        int totalStudents = studentMapper.countByClassId(classId);

        Map<String, Object> data = new HashMap<>();
        data.put("examName", examName);
        data.put("classId", classId);
        data.put("courseId", courseId);
        data.put("fullScore", course.getFullScore());
        data.put("totalStudents", totalStudents);
        data.put("stats", statsMap);
        return Result.success(data);
    }

    private void validateScore(BigDecimal score, Course course) {
        if (score != null) {
            if (score.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "分数不能为负数");
            }
            if (score.compareTo(course.getFullScore()) > 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST,
                        "分数不能超过课程满分 " + course.getFullScore().stripTrailingZeros().toPlainString());
            }
            // 最多1位小数
            if (score.scale() > 1) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, "分数最多保留1位小数");
            }
        }
    }

    private Score buildScore(ScoreRequest request) {
        Score score = new Score();
        score.setStudentId(request.getStudentId());
        score.setClassId(request.getClassId());
        score.setCourseId(request.getCourseId());
        score.setExamName(request.getExamName());
        score.setExamType(request.getExamType());
        score.setScore(request.getScore());
        score.setExamDate(request.getExamDate());
        return score;
    }
}
