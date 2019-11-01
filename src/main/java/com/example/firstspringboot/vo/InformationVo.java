package com.example.firstspringboot.vo;

import java.io.Serializable;

public class InformationVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String date;
    private String newsId;
    private String divInfo;

    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String NEWSID = "newsId";
    public static final String DIVINFO = "divInfo";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getDivInfo() {
        return divInfo;
    }

    public void setDivInfo(String divInfo) {
        this.divInfo = divInfo;
    }
}
