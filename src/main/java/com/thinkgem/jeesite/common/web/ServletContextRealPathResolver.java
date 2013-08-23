package com.thinkgem.jeesite.common.web;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * User: songlai
 * Date: 13-8-22
 * Time: 上午10:17
 */
@Component
public class ServletContextRealPathResolver implements ServletContextAware {
    public String get(String path) {
        return context.getRealPath(path);
    }

    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    public String getContextPath() {
        return context.getContextPath();
    }

    private ServletContext context;
}
