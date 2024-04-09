package com.jackTpy.bean;

import java.io.Serializable;

/**
 * WebApi接口对象.
 */
public class WebResult implements Serializable {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 结果编码
     */
    private Integer code;
    /**
     * 结果描述
     */
    private String message;
    /**
     * 业务数据
     */
    private Object data;

    public WebResult() {
    }

    public static WebResult ok() {
        return ok(null);
    }

    public static WebResult ok(Object data) {
        WebResult result = new WebResult();
        result.setSuccess(true);
        result.setCode(0);
        result.setData(data);
        return result;
    }

    public static WebResult fail(Integer code, String message) {
        WebResult result = new WebResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
