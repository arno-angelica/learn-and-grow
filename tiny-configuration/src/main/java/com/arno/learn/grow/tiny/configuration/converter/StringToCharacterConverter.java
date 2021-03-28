package com.arno.learn.grow.tiny.configuration.converter;

import com.arno.learn.grow.tiny.core.util.StringUtils;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/16 下午9:39
 * @version:
 */
public class StringToCharacterConverter extends AbstractConverter<Character> {
    private static final long serialVersionUID = 8937748933085742097L;

    @Override
    protected Character doConvert(String source) {
        if (!StringUtils.hasText(source) || !StringUtils.hasText(source.trim())) {
            return null;
        }
        return source.charAt(0);
    }
}
