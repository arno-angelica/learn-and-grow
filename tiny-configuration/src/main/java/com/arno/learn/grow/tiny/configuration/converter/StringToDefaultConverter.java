package com.arno.learn.grow.tiny.configuration.converter;

/**
 * @Author: angelica
 * @Date: 2021/3/24 12:10
 * @Description:
 */
public class StringToDefaultConverter extends AbstractConverter<String> {
    @Override
    protected String doConvert(String source) {
        return source;
    }
}
