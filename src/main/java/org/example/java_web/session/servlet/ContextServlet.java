package org.example.java_web.session.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用 ServletContext
 *
 * @author lifei
 */
@WebServlet("/context.do")
public class ContextServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ServletContext context = getServletContext();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>ServletContentDemo</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>context.getRealPath(\"/\"): "
                + context.getRealPath("/") + "</p>");
        out.println("<p>context.getResourcePaths(\"/\"): "
                + context.getResourcePaths("/") + "</p>");
        out.println("<p>context.getResourceAsStream(\"/\")："
                + context.getResourceAsStream("/") + "</p>");
        out.println("<p>new File(\"/filename\").getAbsolutePath(): "
                + new File("/filename").getAbsolutePath() + "</p>");
        out.println("<p>new File(\"filename\").getAbsolutePath(): "
                + new File("filename").getAbsolutePath() + "</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
