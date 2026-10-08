package com.teaching.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CourseRequest {

    @NotBlank(message = "请输入课程名称")
    private String courseName;

    @NotNull(message = "请输入满分值")
    @DecimalMin(value = "0.5", message = "满分值不能小于0.5")
    @DecimalMax(value = "999.9", message = "满分值不能超过999.9")
    private BigDecimal fullScore;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public BigDecimal getFullScore() {
        return fullScore;
    }

    public void setFullScore(BigDecimal fullScore) {
        this.fullScore = fullScore;
    }
}
