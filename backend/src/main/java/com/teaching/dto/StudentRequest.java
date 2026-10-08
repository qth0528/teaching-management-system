package com.teaching.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class StudentRequest {

    @NotBlank(message = "请输入学号")
    private String studentNo;

    @NotBlank(message = "请输入姓名")
    private String name;

    private Integer gender;

    @NotNull(message = "请选择班级")
    private Long classId;

    private String phone;

    private String address;

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
