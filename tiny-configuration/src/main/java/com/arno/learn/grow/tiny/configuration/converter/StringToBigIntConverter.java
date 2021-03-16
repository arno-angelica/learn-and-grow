package com.arno.learn.grow.tiny.configuration.converter;

import java.math.BigInteger;

import static com.arno.learn.grow.tiny.core.util.StringUtils.decodeBigInteger;
import static com.arno.learn.grow.tiny.core.util.StringUtils.isHexNumber;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:23
 * @version:
 */
public class StringToBigIntConverter implements Converter<BigInteger> {
    private static final long serialVersionUID = -8664665392720305521L;

    @Override
    public BigInteger convert(String source) throws IllegalArgumentException, NullPointerException {
        return isHexNumber(source) ? decodeBigInteger(source) : new BigInteger(source);
    }

    @Override
    public String covertType() {
        return BigInteger.class.getSimpleName();
    }
}
