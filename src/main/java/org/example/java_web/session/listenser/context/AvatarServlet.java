package org.example.java_web.session.listenser.context;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ServletContext 事件、监听器
 *
 * @author lifei
 */
@WebServlet("/avatar.view")
public class AvatarServlet extends HttpServlet {
    private String avatarDir;

    @Override
    public void init() {
        avatarDir = (String) getServletContext().getAttribute("avatarDir");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>AvatarServlet</title>");
        out.println("</head>");
        out.println("<body>");
        for (String avatar : getServletContext().getResourcePaths(avatarDir)) {
            out.print("<img src='" + request.getContextPath() + avatar + "'>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}

