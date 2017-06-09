package com.wendu.rongclouddemo.bean;

/**
 * Created by xs on 2017/6/9.
 */

public class Registed {

    /**
     * code : 01
     * message : 用户名可用！
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Registed{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
