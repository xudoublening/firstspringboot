package com.example.firstspringboot.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Xnn on 2019/11/5 17:09
 */
public class UserJoinAnnal implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer userId;
    private String appId;
    private String markId;
    private String loginIp;
    private Integer cityCode;
    private Timestamp annalTime;
    private String annalMonth;
    private String annalYear;
    
    public Integer getUserId() {
        
        return userId;
    }
    
    public void setUserId(Integer userId) {
        
        this.userId = userId;
    }
    
    public String getAppId() {
        
        return appId;
    }
    
    public void setAppId(String appId) {
        
        this.appId = appId;
    }
    
    public String getMarkId() {
        
        return markId;
    }
    
    public void setMarkId(String markId) {
        
        this.markId = markId;
    }
    
    public String getLoginIp() {
        
        return loginIp;
    }
    
    public void setLoginIp(String loginIp) {
        
        this.loginIp = loginIp;
    }
    
    public Integer getCityCode() {
        
        return cityCode;
    }
    
    public void setCityCode(Integer cityCode) {
        
        this.cityCode = cityCode;
    }
    
    public Timestamp getAnnalTime() {
        
        return annalTime;
    }
    
    public void setAnnalTime(Timestamp annalTime) {
        
        this.annalTime = annalTime;
    }
    
    public String getAnnalMonth() {
        
        return annalMonth;
    }
    
    public void setAnnalMonth(String annalMonth) {
        
        this.annalMonth = annalMonth;
    }
    
    public String getAnnalYear() {
        
        return annalYear;
    }
    
    public void setAnnalYear(String annalYear) {
        
        this.annalYear = annalYear;
    }
}
