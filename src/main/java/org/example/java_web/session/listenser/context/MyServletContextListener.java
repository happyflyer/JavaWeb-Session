package org.example.java_web.session.listenser.context;

import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ServletContext 事件、监听器
 *
 * @author lifei
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String avatarDir = context.getInitParameter("AVATAR_DIR");
        context.setAttribute("avatarDir", avatarDir);
        System.out.println("Context Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context Destroyed");
    }
}
