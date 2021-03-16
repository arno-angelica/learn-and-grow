package com.arno.learn.grow.tiny.configuration.converter;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:26
 * @version:
 */
public class StringToDoubleConverter implements Converter<Double> {
    private static final long serialVersionUID = -8959993258184524377L;

    @Override
    public Double convert(String source) throws IllegalArgumentException, NullPointerException {
        return Double.valueOf(source);
    }

    @Override
    public String covertType() {
        return Double.class.getSimpleName();
    }
}
