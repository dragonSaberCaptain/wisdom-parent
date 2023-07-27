package com.wisdom.core.adapter;

import com.wisdom.base.enums.ConstantEnum;
import com.wisdom.core.tools.request.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RepeatedlyReadRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public RepeatedlyReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        if (ConstantEnum.POST.getCode().equalsIgnoreCase(request.getMethod())) {
            if (RequestUtil.isUploadFile(MediaType.parseMediaType(request.getContentType()))) {
                this.body = "文件上传!".getBytes();
            } else {
                this.body = readBytes(request.getReader());
            }
        }
    }

    public String getBody() {
        return new String(this.body, StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 通过BufferedReader和字符编码集转换成byte数组
     */
    private byte[] readBytes(BufferedReader br) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String body = sb.toString();
        return body.getBytes(StandardCharsets.UTF_8);
    }
}