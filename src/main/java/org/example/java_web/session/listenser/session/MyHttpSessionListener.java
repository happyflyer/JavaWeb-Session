package org.example.java_web.session.listenser.session;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * HttpSession 事件、监听器
 *
 * @author lifei
 */
@WebListener
public class MyHttpSessionListener implements HttpSessionListener {
    private static int counter = 0;

    public static int getCounter() {
        return counter;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        MyHttpSessionListener.counter++;
        HttpSession session = se.getSession();
        String user = (String) session.getAttribute("user");
        // 修改数据库字段为登录状态，如果已经是登录状态，则不允许登录
        System.out.println("Session Created: " + se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        MyHttpSessionListener.counter--;
        HttpSession session = se.getSession();
        String user = (String) session.getAttribute("user");
        // 修改数据库字段为注销状态
        System.out.println("Session Destroyed: " + se.getSession());
    }
}
