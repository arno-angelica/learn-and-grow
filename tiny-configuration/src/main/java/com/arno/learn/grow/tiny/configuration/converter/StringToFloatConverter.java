package com.arno.learn.grow.tiny.configuration.converter;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/16 下午10:25
 * @version:
 */
public class StringToFloatConverter extends AbstractConverter<Float> {
    private static final long serialVersionUID = -612948486037384107L;

    @Override
    protected Float doConvert(String source) {
        return Float.valueOf(source);
    }

}
