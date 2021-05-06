package org.example.java_web.session.filter.example2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 实现字符替换过滤器
 *
 * @author lifei
 */
public class EscapeWrapper extends HttpServletRequestWrapper {

    public EscapeWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = getRequest().getParameter(name);
        return StringEscapeUtils.escapeHtml(value);
    }
}
