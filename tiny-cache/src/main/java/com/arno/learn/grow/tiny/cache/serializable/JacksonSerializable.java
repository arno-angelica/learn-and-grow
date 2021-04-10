package com.arno.learn.grow.tiny.cache.serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Desc:
 * Author: angelica
 * Date: 2021/4/10
 */
public class JacksonSerializable implements SerializableProvider {
    @Override
    public byte[] serialize(Object value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(value).getBytes(Charset.defaultCharset());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T) objectMapper.readValue(new String(bytes, Charset.defaultCharset()), Object.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
