package org.example.java_web.session.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
@WebServlet(name = "ConfigServlet", urlPatterns = "/config.do", initParams = {
        @WebInitParam(name = "param3", value = "value3"),
        @WebInitParam(name = "param4", value = "value4")
})
public class ConfigServlet extends HttpServlet {
    private String param1;
    private String param2;
    private String param3;
    private String param4;

    @Override
    public void init() throws ServletException {
        param1 = getInitParameter("param1");
        param2 = getInitParameter("param2");
        param3 = getInitParameter("param3");
        param4 = getInitParameter("param4");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>ServletConfigDemo</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>param1: " + param1 + " </p>");
        out.println("<p>param2: " + param2 + " </p>");
        out.println("<p>param3: " + param3 + " </p>");
        out.println("<p>param4: " + param4 + " </p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
