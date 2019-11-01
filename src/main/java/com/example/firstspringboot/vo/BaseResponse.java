package com.example.firstspringboot.vo;

import com.example.firstspringboot.commons.Constants;

public class BaseResponse {
    private int code = 200;
    private String message = Constants.Message.SUCCESS;
    private Object data = null;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
