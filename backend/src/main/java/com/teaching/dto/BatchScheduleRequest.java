package com.teaching.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class BatchScheduleRequest {

    @NotNull(message = "请选择源班级")
    private Long sourceClassId;

    @NotNull(message = "请选择目标班级")
    private Long targetClassId;

    @NotBlank(message = "请输入学期")
    private String semester;

    private List<Integer> dayOfWeeks;

    public Long getSourceClassId() {
        return sourceClassId;
    }

    public void setSourceClassId(Long sourceClassId) {
        this.sourceClassId = sourceClassId;
    }

    public Long getTargetClassId() {
        return targetClassId;
    }

    public void setTargetClassId(Long targetClassId) {
        this.targetClassId = targetClassId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<Integer> getDayOfWeeks() {
        return dayOfWeeks;
    }

    public void setDayOfWeeks(List<Integer> dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }
}
