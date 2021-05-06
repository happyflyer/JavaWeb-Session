package org.example.java_web.session.filter.example4;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * 响应封装器
 *
 * @author lifei
 */
public class GzipServletOutputStream extends ServletOutputStream {
    private final GZIPOutputStream gzipOutputStream;

    public GzipServletOutputStream(ServletOutputStream servletOutputStream)
            throws IOException {
        this.gzipOutputStream = new GZIPOutputStream(servletOutputStream);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }

    @Override
    public void write(int b) throws IOException {
        gzipOutputStream.write(b);
    }

    public GZIPOutputStream getGzipOutputStream() {
        return gzipOutputStream;
    }
}
