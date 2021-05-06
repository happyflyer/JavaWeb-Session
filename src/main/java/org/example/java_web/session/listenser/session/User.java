package org.example.java_web.session.listenser.session;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * HttpSession 事件、监听器
 *
 * @author lifei
 */
public class User implements HttpSessionBindingListener {
    private String username;
    private String password;

    public User(String name) {
        this.username = name;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        System.out.println(this + " 已经绑定");
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        System.out.println(this + " 已经解除");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}

