package org.example.java_web.session.async;

import java.io.PrintWriter;

import javax.servlet.AsyncContext;

/**
 * 异步处理
 *
 * @author lifei
 */
public class AsyncRequest implements Runnable {
    private final AsyncContext ctx;

    public AsyncRequest(AsyncContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            // 模拟冗长请求
            Thread.sleep(10000);
            PrintWriter out = ctx.getResponse().getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>AsyncServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>久等了 ...</p>");
            out.println("</body>");
            out.println("</html>");
            out.close();
            // 对客户端完成响应
            ctx.complete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
