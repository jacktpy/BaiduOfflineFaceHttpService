package com.huang.config;

import com.alibaba.fastjson.JSONException;
import com.huang.bean.WebResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private Environment environment;

    /**
     * 校验异常的全局处理
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object methodArgumentNotValidHandler(MethodArgumentNotValidException exception,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        //按需重新封装需要返回的错误信息
        //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        Map<String, String> errorData = new HashMap<>(exception.getBindingResult().getErrorCount());
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            if ("POST".equals(request.getMethod()) && error.getField().startsWith("data.")) {
                errorData.put(error.getField().replace("data.", ""), error.getDefaultMessage());
            } else {
                errorData.put(error.getField(), error.getDefaultMessage());
            }
        }
        WebResult rs = WebResult.fail(400, "校验失败" + errorData.values());
        return rs;
    }


    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Object defaultErrorHandler(HttpServletRequest request, NoHandlerFoundException e) {
        LOGGER.error("ExceptionReport|{}|~|{}|{}", request.getRequestURL(), e.getMessage(), getStackTraceString(e));
        WebResult result = WebResult.fail(400, "请求的接口地址不存在");
        return result;
    }


    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Object illegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        LOGGER.error("处理请求【" + request.getRequestURL() + "】出现参数异常", e);
        WebResult result = WebResult.fail(500, "参数不符合要求");
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalStateException.class)
    public Object illegalStateException(HttpServletRequest request, IllegalStateException e) {
        LOGGER.error("处理请求【" + request.getRequestURL() + "】出现语句异常", e);
        WebResult result = WebResult.fail(500, "业务处理发生异常");
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        LOGGER.error("ExceptionReport|{}|~|{}|{}", request.getRequestURL(), e.getMessage(), getStackTraceString(e));
        String msg;
        Throwable cause = e.getCause();
        if (cause != null && cause instanceof JSONException) {
            JSONException jsonException = (JSONException) cause;
            msg = jsonException.getMessage();
        } else {
            msg = e.getMessage();
        }

        WebResult result = WebResult.fail(400, msg);
        return result;
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error("ExceptionReport|{}|~|{}|{}", request.getRequestURL(), e.getMessage(), getStackTraceString(e));
        LOGGER.error("处理请求【" + request.getRequestURL() + "】出现未知异常", e);
        WebResult result = WebResult.fail(500, "服务端发生异常");
        return result;
    }

    private String getStackTraceString(Exception e) {
        StackTraceElement[] elements = e.getStackTrace();
        StringBuffer buffer = new StringBuffer();
        buffer.append(e.getMessage()).append("\\n");
        for (StackTraceElement element : elements) {
            buffer.append(element).append("\\n");
        }
        return buffer.toString();
    }
}