package com.wendu.rongclouddemo.http;

/**
 * Created by xs on 2017/6/2.
 */

public class Result {
    /**
     * code : 10
     * result : {}
     */

    private String code;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", result=" + result +
                '}';
    }
}
