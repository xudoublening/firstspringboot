package com.example.firstspringboot.dto;

public class GnSysConfig {

    private String cfgType;
    private String cfgValue;
    private String cfgKey;
    private String remark;
    private String state;

    public String getCfgType() {
        return cfgType;
    }

    public void setCfgType(String cfgType) {
        this.cfgType = cfgType;
    }

    public String getCfgValue() {
        return cfgValue;
    }

    public void setCfgValue(String cfgValue) {
        this.cfgValue = cfgValue;
    }

    public String getCfgKey() {
        return cfgKey;
    }

    public void setCfgKey(String cfgKey) {
        this.cfgKey = cfgKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
