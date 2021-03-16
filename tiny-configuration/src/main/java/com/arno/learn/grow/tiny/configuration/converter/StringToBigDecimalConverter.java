package com.arno.learn.grow.tiny.configuration.converter;

import java.math.BigDecimal;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:27
 * @version:
 */
public class StringToBigDecimalConverter implements Converter<BigDecimal> {
    private static final long serialVersionUID = -5867837033088300951L;

    @Override
    public BigDecimal convert(String source) throws IllegalArgumentException, NullPointerException {
        return new BigDecimal(source);
    }

    @Override
    public String covertType() {
        return BigDecimal.class.getSimpleName();
    }
}
