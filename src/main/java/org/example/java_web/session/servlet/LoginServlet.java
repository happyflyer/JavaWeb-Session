package org.example.java_web.session.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用 ServletConfig
 *
 * @author lifei
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/login.do", initParams = {
        @WebInitParam(name = "success", value = "success.html"),
        @WebInitParam(name = "error", value = "error.html")
})
public class LoginServlet extends HttpServlet {
    private String successView;
    private String errorView;

    @Override
    public void init() {
        successView = getInitParameter("success");
        errorView = getInitParameter("error");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String trueUsername = "zhangsan";
        String truePassword = "123456";
        if (trueUsername.equals(username) && truePassword.equals(password)) {
            request.getRequestDispatcher(successView).forward(request, response);
        } else {
            request.getRequestDispatcher(errorView).forward(request, response);
        }
    }
}
