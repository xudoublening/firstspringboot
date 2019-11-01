package com.example.firstspringboot.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Student {
    private Integer number;
    @NotNull private String name;
    private Integer age;
    private String sxe;
    private Date admissionTime;

    public Student() {
    }

    public Student(Integer number, String name, Integer age, String sxe, Date admissionTime) {
        this.number = number;
        this.name = name;
        this.age = age;
        this.sxe = sxe;
        this.admissionTime = admissionTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSxe() {
        return sxe;
    }

    public void setSxe(String sxe) {
        this.sxe = sxe;
    }

    public Date getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(Date admissionTime) {
        this.admissionTime = admissionTime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Student{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sxe='" + sxe + '\'' +
                ", admissionTime=" + admissionTime +
                '}';
    }
}
