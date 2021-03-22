package com.arno.learn.grow.tiny.configuration.converter;

import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:21
 * @version:
 */
public class StringToIntegerConverter extends AbstractConverter<Integer> {
    private static final long serialVersionUID = -2291709268948519532L;

    @Override
    protected Integer doConvert(String source) {
        return isHexNumber(source) ? Integer.decode(source) : Integer.valueOf(source);
    }
}
