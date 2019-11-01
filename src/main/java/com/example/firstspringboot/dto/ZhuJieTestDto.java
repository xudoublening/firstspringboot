package com.example.firstspringboot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhuJieTestDto {
    @Null private Integer id;
    @NotNull private String name;
    @DecimalMin("1") private Integer testCount;
    @Valid @NotNull @Size(min = 2)private List<Student> lists;
    @JsonFormat(timezone = "GAT+8",pattern = "yyyy-MM-dd")
    @Past private Date today;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTestCount() {
        return testCount;
    }

    public void setTestCount(Integer testCount) {
        this.testCount = testCount;
    }

    public List<Student> getLists() {
        return lists;
    }

    public void setLists(List<Student> lists) {
        this.lists = lists;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }
}
