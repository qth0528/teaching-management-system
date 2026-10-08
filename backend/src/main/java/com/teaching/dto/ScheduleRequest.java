package com.teaching.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ScheduleRequest {

    @NotNull(message = "请选择班级")
    private Long classId;

    @NotNull(message = "请选择课程")
    private Long courseId;

    @NotNull(message = "请选择教师")
    private Long teacherId;

    @NotNull(message = "请选择星期")
    @Min(value = 1, message = "星期范围为1~5")
    @Max(value = 5, message = "星期范围为1~5")
    private Integer dayOfWeek;

    @NotNull(message = "请选择节次")
    @Min(value = 1, message = "节次范围为1~8")
    @Max(value = 8, message = "节次范围为1~8")
    private Integer period;

    private String roomNumber;

    @NotBlank(message = "请输入学期")
    private String semester;

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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
