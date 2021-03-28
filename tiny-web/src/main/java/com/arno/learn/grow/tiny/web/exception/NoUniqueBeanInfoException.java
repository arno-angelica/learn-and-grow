package com.arno.learn.grow.tiny.web.exception;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/7 下午3:17
 * @version:
 */
public class NoUniqueBeanInfoException extends RuntimeException {
    private static final long serialVersionUID = 3602315081943754907L;
    public NoUniqueBeanInfoException() {
        super();
    }

    public NoUniqueBeanInfoException(Exception e) {
        super(e);
    }

    public NoUniqueBeanInfoException(String message) {
        super(message);
    }
}
