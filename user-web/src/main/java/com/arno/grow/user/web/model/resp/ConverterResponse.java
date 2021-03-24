package com.arno.grow.user.web.model.resp;

import java.io.Serializable;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/3/24 18:17
 * @Description:
 */
public class ConverterResponse implements Serializable {
    private static final long serialVersionUID = 3630940297850706579L;
    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
