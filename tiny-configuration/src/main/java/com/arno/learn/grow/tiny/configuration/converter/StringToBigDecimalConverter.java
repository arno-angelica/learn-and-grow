package com.arno.learn.grow.tiny.configuration.converter;

import java.math.BigDecimal;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:27
 * @version:
 */
public class StringToBigDecimalConverter extends AbstractConverter<BigDecimal> {
    private static final long serialVersionUID = -5867837033088300951L;

    @Override
    protected BigDecimal doConvert(String value) {
        return new BigDecimal(value);
    }

}
