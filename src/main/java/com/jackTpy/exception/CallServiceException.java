package com.jackTpy.exception;

/**
 * 调用服务异常
 */
public class CallServiceException extends Exception {

    private String code;

    public CallServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CallServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
