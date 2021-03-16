package com.arno.learn.grow.tiny.configuration.converter;

import com.arno.learn.grow.tiny.core.util.StringUtils;

import java.nio.charset.Charset;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午9:40
 * @version:
 */
public class StringToCharsetConverter implements Converter<Charset> {
    private static final long serialVersionUID = 4758129761524747799L;

    @Override
    public Charset convert(String s) throws IllegalArgumentException, NullPointerException {
        if (!StringUtils.hasText(s) || !StringUtils.hasText(s.trim())) {
            return null;
        }
        return Charset.forName(s);
    }

    @Override
    public String covertType() {
        return Charset.class.getSimpleName();
    }
}
