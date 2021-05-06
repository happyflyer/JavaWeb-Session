package org.example.java_web.session.filter.example1;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现与设置过滤器
 *
 * @author lifei
 */
@WebFilter(filterName = "ConfigFilter", urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "param1", value = "value1"),
                @WebInitParam(name = "param2", value = "value2")
        })
public class ConfigFilter extends HttpFilter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        String param1 = config.getInitParameter("param1");
        String param2 = config.getInitParameter("param2");
        System.out.println("ConfigFilter init : param1 : " + param1);
        System.out.println("ConfigFilter init : param2 : " + param2);
    }

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain
    ) throws ServletException, IOException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("ConfigFilter destroy");
    }
}
