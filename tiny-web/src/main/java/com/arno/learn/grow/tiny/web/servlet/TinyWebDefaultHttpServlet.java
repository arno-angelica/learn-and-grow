package com.arno.learn.grow.tiny.web.servlet;

import com.alibaba.fastjson.JSONObject;
import com.arno.learn.grow.tiny.web.context.TinyWebApplicationContext;
import com.arno.learn.grow.tiny.web.supoort.SupportMethodInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.substringAfter;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/7 下午5:21
 * @version:
 */
public class TinyWebDefaultHttpServlet extends HttpServlet {
    private static final long serialVersionUID = -6027940701124179746L;

    private static final String ENCODE_TYPE = "utf-8";
    private static final String CONTEXT_TYPE = "application/json; charset=utf-8";
    private static final String PATH_END_JSP = ".jsp";
    private static final String PATH_END_HTML = ".html";

    private TinyWebApplicationContext tinyWebApplicationContext;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        tinyWebApplicationContext = (TinyWebApplicationContext) servletConfig.getServletContext().getAttribute(TinyWebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_NAME);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String servletContextPath = request.getContextPath();
        String requestMappingPath = substringAfter(requestURI,
                StringUtils.replace(servletContextPath, "//", "/"));
        SupportMethodInfo supportMethodInfo = tinyWebApplicationContext.getSupportMethod(requestMappingPath);
        if (supportMethodInfo == null) {
            // 404
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        PrintWriter writer = null;
        try {
            if (!supportMethodInfo.getSupportMethods().contains(request.getMethod())) {
                // HTTP 方法不支持
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            Object respBody;
            Method method = supportMethodInfo.getMethod();
            Parameter[] parameters = method.getParameters();
            if (parameters != null && parameters.length > 0) {
                List<Object> params = new LinkedList<>();
                for (Parameter param : parameters) {
                    Object paramObj = parseRequestParam(request, param);
                    params.add(paramObj);
                }
                // 反射调用
                respBody = method.invoke(supportMethodInfo.getInstance(), params.toArray());
            } else {
                respBody = method.invoke(supportMethodInfo.getInstance());
            }
            if (respBody != null
                    && (respBody.toString().endsWith(PATH_END_HTML)
                    || respBody.toString().endsWith(PATH_END_JSP))) {

                String page = !respBody.toString().startsWith(TinyWebApplicationContext.PATH_START) ?
                                TinyWebApplicationContext.PATH_START + respBody.toString()
                                : respBody.toString();
                ServletContext servletContext = request.getServletContext();
                RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(page);
                requestDispatcher.forward(request, response);
                return;
            }
            // 输出响应结果
            String respStr = JSONObject.toJSONString(respBody);
            response.setCharacterEncoding(ENCODE_TYPE);
            response.setContentType(CONTEXT_TYPE);
            writer = response.getWriter();
            writer.append(respStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 处理请求参数
     *
     * @param request
     * @param parameter
     * @return
     * @throws IOException
     */
    private Object parseRequestParam(HttpServletRequest request, Parameter parameter) throws IOException {
        JSONObject json = new JSONObject();
        String contentType = request.getContentType();
        // 如果为 form 表单提交则使用 getParameter()
        if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
            Enumeration<String> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                String parameterName = enumeration.nextElement();
                json.put(parameterName, request.getParameter(parameterName));
            }
        } else if (MediaType.APPLICATION_JSON.equals(contentType)) {
            // json 格式使用 body reader
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                body.append(line);
            }
            json.putAll(JSONObject.parseObject(body.toString()));
        }
        // TODO 其他方式待添加
        return JSONObject.parseObject(json.toJSONString(), parameter.getType());
    }

}
