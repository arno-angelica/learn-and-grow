package com.arno.learn.grow.tiny.rest.clinet;

import com.arno.learn.grow.tiny.rest.core.DefaultResponse;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static com.arno.learn.grow.tiny.rest.util.URLUtils.DEFAULT_ENCODING;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/28 下午6:38
 * @version:
 */
public abstract class AbstractInvocation implements Invocation {

    private URL url;

    private MultivaluedMap<String, Object> headers;

    private String httpMethod;

    protected String encoding = DEFAULT_ENCODING;

    public AbstractInvocation(URI uri, MultivaluedMap<String, Object> headers, String httpMethod) {
        this.headers = headers;
        this.httpMethod = httpMethod;
        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    public AbstractInvocation(URI uri, MultivaluedMap<String, Object> headers, String httpMethod, String encoding) {
        this.headers = headers;
        this.httpMethod = httpMethod;
        this.encoding = encoding;
        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Invocation property(String name, Object value) {
        return null;
    }

    @Override
    public Response invoke() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            setRequestHeaders(connection);
            // 调用子类
            prepareConnection(connection);

            // TODO Set the cookies
            int statusCode = connection.getResponseCode();
            DefaultResponse response = new DefaultResponse();
            response.setStatus(statusCode);
            Response.Status status = Response.Status.fromStatusCode(statusCode);
            switch (status) {
                case OK:
                    response.setInputStreamReader(connection.getInputStream());
                    break;
                case NOT_FOUND:
                    throw new RuntimeException("Not found the url " + url);
                default:
                    throw new RuntimeException("invoke error " + statusCode);
            }
            return response;

        } catch (Throwable e) {
            throw new RuntimeException("request error. cause " + e, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    protected abstract void prepareConnection(HttpURLConnection connection) throws Throwable;

    private void setRequestHeaders(HttpURLConnection connection) {
        for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (Object headerValue : entry.getValue()) {
                connection.setRequestProperty(headerName, headerValue.toString());
            }
        }
    }

    @Override
    public <T> T invoke(Class<T> responseType) {
        Response response = invoke();
        return response.readEntity(responseType);
    }

    @Override
    public <T> T invoke(GenericType<T> responseType) {
        Response response = invoke();
        return response.readEntity(responseType);
    }

    @Override
    public Future<Response> submit() {
        return null;
    }

    @Override
    public <T> Future<T> submit(Class<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(GenericType<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(InvocationCallback<T> callback) {
        return null;
    }
}
