package com.arno.learn.grow.tiny.configuration.converter;

import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/16 下午10:23
 * @version:
 */
public class StringToLongConverter extends AbstractConverter<Long> {
    private static final long serialVersionUID = 934896040632559021L;

    @Override
    protected Long doConvert(String source) {
        return isHexNumber(source) ? Long.decode(source) : Long.valueOf(source);
    }
}
