package com.arno.learn.grow.tiny.configuration.converter;

import org.eclipse.microprofile.config.spi.Converter;

public abstract class AbstractConverter<T> implements Converter<T> {

    private static final long serialVersionUID = -1190682523016214784L;

    @Override
    public T convert(String source) {
        if (source == null) {
            throw new NullPointerException("The value must not be null!");
        }
        return doConvert(source);
    }

    protected abstract T doConvert(String source);
}
