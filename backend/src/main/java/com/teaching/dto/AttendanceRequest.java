package com.teaching.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AttendanceRequest {

    @NotNull(message = "请选择学生")
    private Long studentId;

    @NotNull(message = "请选择班级")
    private Long classId;

    @NotNull(message = "请选择日期")
    private LocalDate attendanceDate;

    @NotNull(message = "请选择考勤状态")
    @Min(value = 1, message = "状态值范围1~4")
    @Max(value = 4, message = "状态值范围1~4")
    private Integer status;

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

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
