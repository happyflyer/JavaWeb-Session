package org.example.java_web.session.filter.example2;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * 实现字符替换过滤器
 *
 * @author lifei
 */
@WebFilter(filterName = "EscapeFilter", urlPatterns = "/*")
public class EscapeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("EscapeFilter init");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws ServletException, IOException {
        HttpServletRequest requestWrapper = new EscapeWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {
        System.out.println("EscapeFilter destroy");
    }
}
