package com.arno.grow.user.web.model;

import com.arno.grow.user.web.constant.ErrorCode;

import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午4:42
 * @version:
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 81819567273231409L;

    private int code;
    private String message;

    public BaseResult() {
        this.code = ErrorCode.SUCCESS.getCode();
        this.message = ErrorCode.SUCCESS.getMessage();
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static BaseResult createFail(ErrorCode errorCode) {
        return new BaseResult(errorCode.getCode(), errorCode.getMessage());
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
}
