package com.arno.learn.grow.tiny.configuration.converter;

import com.arno.learn.grow.tiny.core.util.StringUtils;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午9:38
 * @version:
 */
public class StringToBooleanConverter implements Converter<Boolean> {
    private static final long serialVersionUID = 5848628749461325759L;

    @Override
    public Boolean convert(String source) throws IllegalArgumentException, NullPointerException {
        if (!StringUtils.hasText(source) || !StringUtils.hasText(source.trim())) {
            return Boolean.FALSE;
        }
        return Boolean.parseBoolean(source);
    }

    @Override
    public String covertType() {
        return Boolean.class.getSimpleName();
    }
}
