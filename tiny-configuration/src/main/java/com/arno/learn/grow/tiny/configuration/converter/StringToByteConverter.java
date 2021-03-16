package com.arno.learn.grow.tiny.configuration.converter;

import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:18
 * @version:
 */
public class StringToByteConverter implements Converter<Byte> {
    private static final long serialVersionUID = -7035561628076431902L;

    @Override
    public Byte convert(String source) throws IllegalArgumentException, NullPointerException {
        return isHexNumber(source) ? Byte.decode(source) : Byte.valueOf(source);
    }

    @Override
    public String covertType() {
        return Byte.class.getSimpleName();
    }
}
