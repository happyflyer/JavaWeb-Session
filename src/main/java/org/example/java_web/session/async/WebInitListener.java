package org.example.java_web.session.async;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 模拟服务器推播
 *
 * @author lifei
 */
@WebListener
public class WebInitListener implements ServletContextListener {
    /**
     * 所有异步请求的 AsyncContext 将存储在这个 List
     */
    private List<AsyncContext> async = new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("async", async);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // 模拟不定时产生随机数字
                        Thread.sleep((int) (Math.random() * 3000));
                        double num = Math.random() * 10;
                        synchronized (async) {
                            // 逐一完成异步请求
                            for (AsyncContext ctx : async) {
                                ctx.getResponse().getWriter().println(num);
                                ctx.complete();
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        System.out.println("Async Context Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Async Context Destroyed");
    }
}
