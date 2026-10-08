package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.ErrorCode;
import com.teaching.common.Result;
import com.teaching.dto.HomeworkRequest;
import com.teaching.dto.HomeworkReviewRequest;
import com.teaching.dto.HomeworkSubmissionRequest;
import com.teaching.dto.HomeworkUpdateRequest;
import com.teaching.entity.Homework;
import com.teaching.entity.HomeworkSubmission;
import com.teaching.entity.Student;
import com.teaching.mapper.ClassMapper;
import com.teaching.mapper.CourseMapper;
import com.teaching.mapper.HomeworkMapper;
import com.teaching.mapper.HomeworkSubmissionMapper;
import com.teaching.mapper.StudentMapper;
import com.teaching.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkSubmissionMapper submissionMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result<Map<String, Object>> list(Long classId, Long courseId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Homework> list = homeworkMapper.selectPage(classId, courseId, offset, pageSize);
        int total = homeworkMapper.countPage(classId, courseId);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("list", list);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> getById(Long id) {
        Homework homework = homeworkMapper.selectById(id);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "作业不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("homework", homework);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> create(HomeworkRequest request) {
        if (classMapper.selectById(request.getClassId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "班级不存在");
        }
        if (courseMapper.selectById(request.getCourseId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "课程不存在");
        }

        if (!request.getDeadline().isAfter(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "截止时间必须晚于发布时间");
        }

        Homework homework = new Homework();
        homework.setTitle(request.getTitle());
        homework.setDescription(request.getDescription());
        homework.setClassId(request.getClassId());
        homework.setCourseId(request.getCourseId());
        homework.setTeacherId(request.getTeacherId());
        homework.setDeadline(request.getDeadline());
        homeworkMapper.insert(homework);

        List<Student> students = studentMapper.selectByClassId(request.getClassId());
        if (!students.isEmpty()) {
            List<HomeworkSubmission> submissions = new ArrayList<>();
            for (Student student : students) {
                HomeworkSubmission submission = new HomeworkSubmission();
                submission.setHomeworkId(homework.getId());
                submission.setStudentId(student.getId());
                submission.setStatus(1);
                submission.setIsLate(0);
                submissions.add(submission);
            }
            submissionMapper.batchInsert(submissions);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("id", homework.getId());
        data.put("submissionCount", students.size());
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> update(Long id, HomeworkUpdateRequest request) {
        Homework homework = homeworkMapper.selectById(id);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "作业不存在");
        }

        if (!request.getDeadline().isAfter(homework.getPublishTime())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "截止时间必须晚于发布时间");
        }

        homework.setTitle(request.getTitle());
        homework.setDescription(request.getDescription());
        homework.setDeadline(request.getDeadline());
        homeworkMapper.update(homework);

        return Result.success(null);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> delete(Long id) {
        Homework homework = homeworkMapper.selectById(id);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "作业不存在");
        }

        int submissionCount = submissionMapper.countByHomeworkId(id);
        submissionMapper.deleteByHomeworkId(id);
        homeworkMapper.deleteById(id);

        Map<String, Object> data = new HashMap<>();
        data.put("submissionCount", submissionCount);
        return Result.success(data);
    }

    @Override
    public Result<Map<String, Object>> listSubmissions(Long homeworkId) {
        Homework homework = homeworkMapper.selectById(homeworkId);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "作业不存在");
        }

        List<HomeworkSubmission> list = submissionMapper.selectByHomeworkId(homeworkId);

        int submittedCount = 0;
        int gradedCount = 0;
        for (HomeworkSubmission s : list) {
            if (s.getStatus() != null && s.getStatus() >= 2) {
                submittedCount++;
            }
            if (s.getStatus() != null && s.getStatus() == 3) {
                gradedCount++;
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("homeworkId", homeworkId);
        data.put("title", homework.getTitle());
        data.put("totalCount", list.size());
        data.put("submittedCount", submittedCount);
        data.put("pendingCount", list.size() - submittedCount);
        data.put("gradedCount", gradedCount);
        data.put("list", list);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> submit(HomeworkSubmissionRequest request) {
        Homework homework = homeworkMapper.selectById(request.getHomeworkId());
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "作业不存在");
        }

        if (studentMapper.selectById(request.getStudentId()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "学生不存在");
        }

        HomeworkSubmission target = submissionMapper.selectByHomeworkAndStudent(
                request.getHomeworkId(), request.getStudentId());
        if (target == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "未找到该学生的提交记录");
        }

        if (target.getStatus() == 3) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该作业已批改，无法重复提交");
        }

        LocalDateTime now = LocalDateTime.now();
        target.setContent(request.getContent());
        target.setSubmitTime(now);
        target.setStatus(2);
        target.setIsLate(now.isAfter(homework.getDeadline()) ? 1 : 0);
        submissionMapper.update(target);

        Map<String, Object> data = new HashMap<>();
        data.put("id", target.getId());
        data.put("isLate", target.getIsLate() == 1);
        return Result.success(data);
    }

    @Override
    @Transactional
    public Result<Map<String, Object>> review(Long id, HomeworkReviewRequest request) {
        HomeworkSubmission submission = submissionMapper.selectById(id);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "提交记录不存在");
        }

        if (submission.getStatus() != 2) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只能批改已提交待批改的作业");
        }

        BigDecimal score = request.getScore();
        if (score.scale() > 1) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "分数最多保留1位小数");
        }

        submission.setScore(score);
        submission.setComment(request.getComment());
        submission.setStatus(3);
        submissionMapper.update(submission);

        return Result.success(null);
    }
}
