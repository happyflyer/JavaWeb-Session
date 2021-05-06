package org.example.java_web.session.filter.example3;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

/**
 * 实现编码设置过滤器
 *
 * @author lifei
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8")
        })
public class EncodingFilter implements Filter {
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        System.out.println("EncodingFilter init");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String getMethod = "GET";
        if (getMethod.equals(req.getMethod())) {
            req = new EncodingWrapper(req, encoding);
        } else {
            req.setCharacterEncoding(encoding);
        }
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {
        System.out.println("EncodingFilter destroy");
    }
}
