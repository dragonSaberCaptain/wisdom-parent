package com.wisdom.core.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "RepeatedlyReadFilter")
public class RepeatedlyReadFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ServletRequest requestWrapper = null;

        if (request instanceof HttpServletRequest) {
            String servletPath = ((HttpServletRequest) request).getServletPath();
//            if (servletPath.contains("/fileUpload")) {
//                if ("POST".equals(((HttpServletRequest) request).getMethod())) {
//                    chain.doFilter(request, response);
//                    return;
//                }
//            }
            requestWrapper = new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}