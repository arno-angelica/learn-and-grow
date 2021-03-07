package com.arno.learn.grow.tiny.web.exception;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/7 下午3:03
 * @version:
 */
public class ServiceConfigException extends RuntimeException {
    private static final long serialVersionUID = -8262726374123142322L;

    public ServiceConfigException() {
        super();
    }

    public ServiceConfigException(Exception e) {
        super(e);
    }

    public ServiceConfigException(String message) {
        super(message);
    }
}
