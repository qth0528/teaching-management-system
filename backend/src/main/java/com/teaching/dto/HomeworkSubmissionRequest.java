package com.teaching.dto;

import javax.validation.constraints.NotNull;

public class HomeworkSubmissionRequest {

    @NotNull(message = "请选择作业")
    private Long homeworkId;

    @NotNull(message = "请选择学生")
    private Long studentId;

    private String content;

    public Long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
