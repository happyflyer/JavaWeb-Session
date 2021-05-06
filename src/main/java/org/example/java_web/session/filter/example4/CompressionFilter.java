package org.example.java_web.session.filter.example4;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 响应封装器
 *
 * @author lifei
 */
@WebFilter(filterName = "CompressionFilter", urlPatterns = "/*")
public class CompressionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CompressionFilter init");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String encodings = req.getHeader("accept-encoding");
        if (encodings != null && (encodings.contains("gzip"))) {
            CompressionWrapper responseWrapper = new CompressionWrapper(resp);
            responseWrapper.setHeader("content-encoding", "gzip");
            chain.doFilter(request, responseWrapper);
            GZIPOutputStream gzipOutputStream = responseWrapper.getGzipOutputStream();
            if (gzipOutputStream != null) {
                gzipOutputStream.finish();
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("CompressionFilter destroy");
    }
}
