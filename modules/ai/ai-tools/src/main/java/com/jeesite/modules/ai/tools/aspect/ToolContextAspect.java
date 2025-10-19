/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.aspect;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Tool 上下文用户信息注入切面
 * @author ThinkGem
 */
@Aspect
@Component
public class ToolContextAspect {
    
    @Around("@annotation(org.springframework.ai.tool.annotation.Tool)")
    public Object handleThreadContext(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        ToolContext toolContext = null;
        for (Object arg : args) {
            if (arg instanceof ToolContext) {
                toolContext = (ToolContext) arg;
                break;
            }
        }
		if (toolContext != null) {
			Map<String, Object> context = toolContext.getContext();
			if (context.containsKey("subject")) {
				ThreadContext.bind((Subject) context.get("subject"));
			}
		}
		return joinPoint.proceed();
    }
}