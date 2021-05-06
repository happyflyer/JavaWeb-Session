package org.example.java_web.session.listenser.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpSession 事件、监听器
 *
 * @author lifei
 */
@WebServlet("/listener/login.do")
public class LoginServlet extends HttpServlet {
    private final Map<String, String> users;

    public LoginServlet() {
        users = new HashMap<>();
        users.put("zhangsan", "123456");
        users.put("lisi", "000000");
        users.put("wangwu", "888888");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (users.containsKey(username) && users.get(username).equals(password)) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("user", new User(username));
            String welcomeView = "/listener/welcome.view";
            getServletContext().getRequestDispatcher(welcomeView).forward(request, response);
        } else {
            String loginView = "login.html";
            response.sendRedirect(loginView);
        }
    }
}
