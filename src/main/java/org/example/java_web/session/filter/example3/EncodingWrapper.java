package org.example.java_web.session.filter.example3;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 实现编码设置过滤器
 *
 * @author lifei
 */
public class EncodingWrapper extends HttpServletRequestWrapper {
    private final String encoding;

    public EncodingWrapper(HttpServletRequest request, String encoding) {
        super(request);
        this.encoding = encoding;
    }

    @Override
    public String getParameter(String name) {
        String value = getRequest().getParameter(name);
        if (value != null) {
            try {
                byte[] bytes = value.getBytes(StandardCharsets.ISO_8859_1);
                value = new String(bytes, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }
}
