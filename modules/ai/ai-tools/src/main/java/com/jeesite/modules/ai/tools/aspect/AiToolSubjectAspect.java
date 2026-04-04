/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.aspect;

import com.jeesite.modules.ai.tools.context.AiToolContextProvider;
import io.modelcontextprotocol.common.McpTransportContext;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 用户信息注入切面
 * @author ThinkGem
 */
@Aspect
@Component
public class AiToolSubjectAspect {

	/**
	 * 给 Tool 传递当前用户信息
	 */
	@Around("@annotation(org.springframework.ai.tool.annotation.Tool)")
	public Object handleThreadContext(ProceedingJoinPoint joinPoint) throws Throwable {
		McpTransportContext context = AiToolContextProvider.INSTANCE.get();
		Subject subject = (Subject) context.get(AiToolContextProvider.SUBJECT_KEY);
		if (subject != null) {
			ThreadContext.bind(subject);
		}
		return joinPoint.proceed();
	}
}