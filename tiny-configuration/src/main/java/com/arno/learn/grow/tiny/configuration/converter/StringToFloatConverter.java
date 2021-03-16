package com.arno.learn.grow.tiny.configuration.converter;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/16 下午10:25
 * @version:
 */
public class StringToFloatConverter implements Converter<Float> {
    private static final long serialVersionUID = -612948486037384107L;

    @Override
    public Float convert(String source) throws IllegalArgumentException, NullPointerException {
        return Float.valueOf(source);
    }

    @Override
    public String covertType() {
        return Float.class.getSimpleName();
    }
}
