package org.example.java_web.session.listenser.session;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpSession 事件、监听器
 *
 * @author lifei
 */
@WebServlet("/listener/logout.do")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("user");
        String loginView = "login.html";
        response.sendRedirect(loginView);
    }
}
