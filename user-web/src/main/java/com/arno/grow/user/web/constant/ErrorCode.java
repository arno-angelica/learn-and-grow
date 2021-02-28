package com.arno.grow.user.web.constant;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午4:43
 * @version:
 */
public enum ErrorCode {
    SUCCESS(10000, "success");

    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
