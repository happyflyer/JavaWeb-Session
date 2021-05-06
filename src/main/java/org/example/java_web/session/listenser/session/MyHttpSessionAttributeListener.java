package org.example.java_web.session.listenser.session;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionAttributeListener;

/**
 * HttpSession 事件、监听器
 *
 * @author lifei
 */
@WebListener
public class MyHttpSessionAttributeListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        System.out.println("Session Attribute Added: "
                + event.getName() + " : " + event.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        System.out.println("Session Attribute Removed: "
                + event.getName() + " : " + event.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        System.out.println("Session Attribute Replaced: "
                + event.getName() + " : " + event.getValue());
    }
}
