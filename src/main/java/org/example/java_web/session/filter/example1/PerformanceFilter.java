package org.example.java_web.session.filter.example1;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 实现与设置过滤器
 *
 * @author lifei
 */
@WebFilter(filterName = "PerformanceFilter", urlPatterns = {"/*"},
        dispatcherTypes = {
                DispatcherType.REQUEST,
                DispatcherType.FORWARD,
                DispatcherType.INCLUDE,
                DispatcherType.ERROR,
                DispatcherType.ASYNC
        })
public class PerformanceFilter extends HttpFilter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("PerformanceFilter init");
    }

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain
    ) throws ServletException, IOException {
        long begin = System.currentTimeMillis();
        chain.doFilter(request, response);
        long end = System.currentTimeMillis();
        System.out.printf("Request %s process in %d milliseconds%n",
                request.getServletPath(), end - begin);
    }

    @Override
    public void destroy() {
        System.out.println("PerformanceFilter destroy");
    }
}
