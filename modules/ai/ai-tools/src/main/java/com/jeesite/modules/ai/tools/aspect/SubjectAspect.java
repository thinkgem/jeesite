/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.aspect;

import com.jeesite.modules.ai.tools.utils.SubjectHolder;
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
public class SubjectAspect {

	/**
	 * 给 Tool 传递当前用户信息
	 */
	@Around("@annotation(org.springframework.ai.tool.annotation.Tool)")
	public Object handleThreadContext(ProceedingJoinPoint joinPoint) throws Throwable {
//		Object[] args = joinPoint.getArgs();
//		for (Object arg : args) {
//			if (arg instanceof ToolContext toolContext) {
//				Map<String, Object> context = toolContext.getContext();
//				Subject subject = (Subject) context.get("subject");
//				if (subject != null) {
//					ThreadContext.bind(subject);
//					return joinPoint.proceed();
//				}
//			} else if (arg instanceof McpMeta mcpMeta) { }
//		}
		Subject subject = SubjectHolder.getSubject();
		if (subject != null) {
			ThreadContext.bind(subject);
		}
		return joinPoint.proceed();
	}
}