package org.example.java_web.session.filter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lifei
 */
@WebServlet("/filter/form.do")
public class FormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String content = request.getParameter("content");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>FormServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>" + content + " </p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // <a href="http://www.baidu.com">百度一下，你就知道</a>
        String content = request.getParameter("content");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>FormServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>" + content + " </p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
