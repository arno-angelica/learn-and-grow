package com.arno.learn.grow.tiny.configuration.converter;

import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/16 下午10:18
 * @version:
 */
public class StringToByteConverter extends AbstractConverter<Byte> {
    private static final long serialVersionUID = -7035561628076431902L;

    @Override
    protected Byte doConvert(String source) {
        return isHexNumber(source) ? Byte.decode(source) : Byte.valueOf(source);
    }
}
