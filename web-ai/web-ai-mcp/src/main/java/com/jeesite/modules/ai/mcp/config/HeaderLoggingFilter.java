/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.mcp.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Header 日志过滤器
 * @author ThinkGem
 */
@Component
public class HeaderLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(HeaderLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            logger.info("=== 过滤器捕获请求: {} {} ===", httpRequest.getMethod(), httpRequest.getRequestURI());
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String values = String.join(", ", Collections.list(httpRequest.getHeaders(headerName)));
                logger.info("Header [{}]: {}", headerName, values);
            }
            logger.info("================================");
        }
        chain.doFilter(request, response);
    }
}