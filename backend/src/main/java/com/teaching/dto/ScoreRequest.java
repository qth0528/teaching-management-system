package com.teaching.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ScoreRequest {

    @NotNull(message = "请选择学生")
    private Long studentId;

    @NotNull(message = "请选择班级")
    private Long classId;

    @NotNull(message = "请选择课程")
    private Long courseId;

    @NotBlank(message = "请输入考试名称")
    private String examName;

    @NotNull(message = "请选择考试类型")
    @Min(value = 1, message = "考试类型值范围1~5")
    @Max(value = 5, message = "考试类型值范围1~5")
    private Integer examType;

    private BigDecimal score;

    @NotNull(message = "请选择考试日期")
    private LocalDate examDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }
}
