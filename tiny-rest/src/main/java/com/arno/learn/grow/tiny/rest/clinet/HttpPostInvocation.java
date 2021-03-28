package com.arno.learn.grow.tiny.rest.clinet;

import org.apache.commons.lang.StringUtils;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/28 下午6:37
 * @version:
 */
public class HttpPostInvocation extends AbstractInvocation {

    private static final String CONTENT_TYPE = "%s/%s; %s";
    private static final String DEFAULT_CONTENT_TYPE = "*/*";

    private final Entity<?> entity;
    private final String contentType;

    public HttpPostInvocation(URI uri, MultivaluedMap<String, Object> headers, Entity<?> entity) {
        super(uri, headers, HttpMethod.POST);
        this.entity = entity;
        MediaType type = entity.getMediaType();
        if (type == null || StringUtils.isBlank(type.getType())) {
            this.contentType = DEFAULT_CONTENT_TYPE + "; " + encoding;
        } else {
            this.contentType = String.format(CONTENT_TYPE, type.getType(), type.getSubtype(), encoding);
        }
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection) throws Throwable {
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Accept", DEFAULT_CONTENT_TYPE);
        connection.setDoOutput(true);
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = entity.getEntity().toString().getBytes(encoding);
            os.write(input, 0, input.length);
        }
    }
}
