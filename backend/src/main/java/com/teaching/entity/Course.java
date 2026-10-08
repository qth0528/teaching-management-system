package com.teaching.entity;

import com.teaching.common.BaseEntity;

import java.math.BigDecimal;

public class Course extends BaseEntity {

    private String courseName;
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
