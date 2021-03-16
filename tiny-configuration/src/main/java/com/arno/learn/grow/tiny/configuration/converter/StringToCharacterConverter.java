package com.arno.learn.grow.tiny.configuration.converter;

import com.arno.learn.grow.tiny.core.util.StringUtils;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午9:39
 * @version:
 */
public class StringToCharacterConverter implements Converter<Character> {
    private static final long serialVersionUID = 8937748933085742097L;

    @Override
    public Character convert(String s) throws IllegalArgumentException, NullPointerException {
        if (!StringUtils.hasText(s) || !StringUtils.hasText(s.trim())) {
            return null;
        }
        return s.charAt(0);
    }

    @Override
    public String covertType() {
        return Character.class.getSimpleName();
    }
}
