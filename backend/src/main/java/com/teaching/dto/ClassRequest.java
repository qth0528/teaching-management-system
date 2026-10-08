package com.teaching.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ClassRequest {

    @NotBlank(message = "请输入班级名称")
    private String className;

    @NotNull(message = "请选择负责教师")
    private Long teacherId;

    private String roomNumber;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
