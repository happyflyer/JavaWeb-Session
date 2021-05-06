package org.example.java_web.session.async;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异步处理
 * TODO: 异步处理需要关闭前面的过滤器，因为前面的过滤器没有实现对异步处理的支持
 *
 * @author lifei
 */
@WebServlet(name = "AsyncServlet", urlPatterns = "/async.do", asyncSupported = true)
public class AsyncServlet extends HttpServlet {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        // 开始异步处理，释放请求线程
        AsyncContext ctx = request.startAsync();
        // 创建 AsyncRequest，调度线程
        executorService.submit(new AsyncRequest(ctx));
    }

    @Override
    public void destroy() {
        // 关闭线程池
        executorService.shutdown();
    }
}
