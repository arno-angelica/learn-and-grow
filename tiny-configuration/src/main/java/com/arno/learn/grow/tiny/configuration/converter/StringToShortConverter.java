package com.arno.learn.grow.tiny.configuration.converter;

import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:20
 * @version:
 */
public class StringToShortConverter implements Converter<Short> {
    private static final long serialVersionUID = 5547486508481285095L;

    @Override
    public Short convert(String source) throws IllegalArgumentException, NullPointerException {
        return isHexNumber(source) ? Short.decode(source) : Short.valueOf(source);
    }

    @Override
    public String covertType() {
        return Short.class.getSimpleName();
    }
}
