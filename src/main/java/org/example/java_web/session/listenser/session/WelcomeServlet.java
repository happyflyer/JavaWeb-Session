package org.example.java_web.session.listenser.session;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * HttpSession 事件、监听器
 *
 * @author lifei
 */
@WebServlet("/listener/welcome.view")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>WelcomeServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>目前在线人数: " + MyHttpSessionListener.getCounter() + "人 </p>");
        HttpSession session = request.getSession();
        if (session != null) {
            String username = (String) session.getAttribute("username");
            out.println("<p>欢迎, " + username + "</p>");
            out.println("<a href='logout.do'>注销</a>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
