package com.arno.learn.grow.tiny.configuration.converter;

import com.arno.learn.grow.tiny.core.util.StringUtils;

import java.nio.charset.Charset;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/16 下午9:40
 * @version:
 */
public class StringToCharsetConverter extends AbstractConverter<Charset> {
    private static final long serialVersionUID = 4758129761524747799L;

    @Override
    protected Charset doConvert(String source) {
        if (!StringUtils.hasText(source) || !StringUtils.hasText(source.trim())) {
            return null;
        }
        return Charset.forName(source);
    }
}
