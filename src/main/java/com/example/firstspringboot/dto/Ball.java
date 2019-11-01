package com.example.firstspringboot.dto;

public class Ball {
    private Integer number;//号码
    private Integer addUpTime;//累计出现次数
    private Integer maxContinuityTime;//最大连续出现次数
    private Integer maxOmissionTime;//最大遗漏数
    private Integer averageOmissionTime;//平均遗漏数
    private Double probability;

    public Ball(Integer number, Integer addUpTime, Integer maxContinuityTime, Integer maxOmissionTime, Integer averageOmissionTime) {
        this.number = number;
        this.addUpTime = addUpTime;
        this.maxContinuityTime = maxContinuityTime;
        this.maxOmissionTime = maxOmissionTime;
        this.averageOmissionTime = averageOmissionTime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getAddUpTime() {
        return addUpTime;
    }

    public void setAddUpTime(Integer addUpTime) {
        this.addUpTime = addUpTime;
    }

    public Integer getMaxContinuityTime() {
        return maxContinuityTime;
    }

    public void setMaxContinuityTime(Integer maxContinuityTime) {
        this.maxContinuityTime = maxContinuityTime;
    }

    public Integer getMaxOmissionTime() {
        return maxOmissionTime;
    }

    public void setMaxOmissionTime(Integer maxOmissionTime) {
        this.maxOmissionTime = maxOmissionTime;
    }

    public Integer getAverageOmissionTime() {
        return averageOmissionTime;
    }

    public void setAverageOmissionTime(Integer averageOmissionTime) {
        this.averageOmissionTime = averageOmissionTime;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }
}
