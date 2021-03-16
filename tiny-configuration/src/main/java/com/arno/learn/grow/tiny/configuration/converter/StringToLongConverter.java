package com.arno.learn.grow.tiny.configuration.converter;

import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:23
 * @version:
 */
public class StringToLongConverter implements Converter<Long> {
    private static final long serialVersionUID = 934896040632559021L;

    @Override
    public Long convert(String source) throws IllegalArgumentException, NullPointerException {
        return isHexNumber(source) ? Long.decode(source) : Long.valueOf(source);
    }

    @Override
    public String covertType() {
        return Long.class.getSimpleName();
    }
}
