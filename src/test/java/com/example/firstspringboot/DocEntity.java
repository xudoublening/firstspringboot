package com.example.firstspringboot;

import java.io.Serializable;

/**
 * doc 对象
 */
public class DocEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String textType;
    private String textValue;

    public static final String DOC_HEAD = "head";//头部文本名
    public static final String DOC_TITLE = "title";//标题
    public static final String DOC_TEXT = "text";//文本
    public static final String DOC_WRAP = "wrap";//换行
    public static final String DOC_IMG_SMALL = "image_small";//小图
    public static final String DOC_IMG_MEDIUM = "image_medium";//中图
    public static final String DOC_IMG_BIG = "image_big";//大图
    public static final String DOC_FOOT = "foot";//尾部署名

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
}
