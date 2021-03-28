package com.arno.learn.grow.tiny.configuration.converter;

import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/16 下午10:20
 * @version:
 */
public class StringToShortConverter extends AbstractConverter<Short> {
    private static final long serialVersionUID = 5547486508481285095L;

    @Override
    protected Short doConvert(String source) {
        return isHexNumber(source) ? Short.decode(source) : Short.valueOf(source);
    }
}
