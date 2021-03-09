package com.arno.grow.user.web.model;

import com.arno.grow.user.web.constant.ErrorCode;

import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午4:42
 * @version:
 */
public class BaseResult<T> implements Serializable {
    private static final long serialVersionUID = 81819567273231409L;

    private int code;
    private String message;
    private T data;

    public BaseResult(T data) {
        this.code = ErrorCode.SUCCESS.getCode();
        this.message = ErrorCode.SUCCESS.getMessage();
        this.data = data;
    }

    public BaseResult() {
        this.code = ErrorCode.SUCCESS.getCode();
        this.message = ErrorCode.SUCCESS.getMessage();
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public static <T> BaseResult<T> createFail(ErrorCode errorCode) {
        return new BaseResult<>(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> BaseResult<T> createFail(ErrorCode errorCode, String message) {
        return new BaseResult<>(errorCode.getCode(), message);
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
