package org.example.java_web.session.async;

import java.io.IOException;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 模拟服务器推播
 *
 * @author lifei
 */
@WebServlet(name = "AsyncNumServlet", urlPatterns = {"/asyncNum.do" },
        asyncSupported = true)
public class AsyncNumServlet extends HttpServlet {
    private List<AsyncContext> asyncList;

    @Override
    public void init() {
        asyncList = (List<AsyncContext>) getServletContext().getAttribute("async");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AsyncContext ctx = request.startAsync();
        synchronized (asyncList) {
            asyncList.add(ctx);
        }
    }
}
