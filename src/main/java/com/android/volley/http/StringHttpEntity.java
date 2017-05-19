package com.android.volley.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StringHttpEntity extends HttpEntity {

    public final static String PLAIN_TEXT_TYPE = "text/plain";
    public final static String CHARSET_PARAM = "; charset=";

    protected final byte[] content;

    public StringHttpEntity(final String s, String charset)
            throws UnsupportedEncodingException {
        super();
        if (s == null) {
            throw new IllegalArgumentException("Source string may not be null");
        }
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        this.content = s.getBytes(charset);
        setContentType(PLAIN_TEXT_TYPE + CHARSET_PARAM + charset);
    }

    public InputStream getContent() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void writeTo(final ByteArrayOutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        outstream.write(this.content);
        outstream.flush();
    }
}
