package org.example.java_web.session.listenser.context;

import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/**
 * ServletContext 事件、监听器
 *
 * @author lifei
 */
@WebListener
public class MyServletContextAttributeListener
        implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        System.out.println("Context Attribute Added: "
                + event.getName() + " : " + event.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        System.out.println("Context Attribute Removed: "
                + event.getName() + " : " + event.getValue());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        System.out.println("Context Attribute Replaced: "
                + event.getName() + " : " + event.getValue());
    }
}
