package com.arno.learn.grow.tiny.rest.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/28 下午7:34
 * @version:
 */
public class StreamUtils {


    public static ByteArrayOutputStream copyInputStream(InputStream input) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byteArrayOutputStream.flush();
        return byteArrayOutputStream;
    }
}
