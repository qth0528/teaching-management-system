package com.teaching.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class HomeworkReviewRequest {

    @NotNull(message = "请输入分数")
    @Min(value = 0, message = "分数范围0~100")
    @Max(value = 100, message = "分数范围0~100")
    private BigDecimal score;

    private String comment;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
