package org.example.java_web.session.filter.example4;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 响应封装器
 *
 * @author lifei
 */
public class CompressionWrapper extends HttpServletResponseWrapper {
    private GzipServletOutputStream gzServletOutputStream;
    private PrintWriter printwriter;

    public CompressionWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (printwriter != null) {
            throw new IllegalStateException();
        }
        if (gzServletOutputStream == null) {
            gzServletOutputStream = new GzipServletOutputStream(
                    getResponse().getOutputStream()
            );
        }
        return gzServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (gzServletOutputStream != null) {
            throw new IllegalStateException();
        }
        if (printwriter == null) {
            gzServletOutputStream = new GzipServletOutputStream(
                    getResponse().getOutputStream()
            );
            OutputStreamWriter osw = new OutputStreamWriter(
                    gzServletOutputStream,
                    getResponse().getCharacterEncoding()
            );
            printwriter = new PrintWriter(osw);
        }
        return printwriter;
    }

    public GZIPOutputStream getGzipOutputStream() {
        if (this.gzServletOutputStream == null) {
            return null;
        }
        return this.gzServletOutputStream.getGzipOutputStream();
    }
}
