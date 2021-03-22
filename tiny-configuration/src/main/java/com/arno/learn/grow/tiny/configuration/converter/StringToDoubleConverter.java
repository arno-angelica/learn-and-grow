package com.arno.learn.grow.tiny.configuration.converter;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:26
 * @version:
 */
public class StringToDoubleConverter extends AbstractConverter<Double> {
    private static final long serialVersionUID = -8959993258184524377L;

    @Override
    protected Double doConvert(String source) {
        return Double.valueOf(source);
    }
}
