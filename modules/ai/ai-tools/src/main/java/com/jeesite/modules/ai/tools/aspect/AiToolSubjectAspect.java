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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.stereotype.Component;

/**
 * 用户信息注入切面
 * <p>
 * 工具方法（本地 Bean 的 {@code @Tool} 方法、MCP 工具方法）可能执行在非请求线程上
 * （流式输出的 Reactor 线程、异步任务线程等），Shiro 的 Subject 不会自动绑定到这些线程，
 * 导致工具内 {@code UserUtils.getUser()} 取不到当前登录用户（提示未登录）。
 * <p>
 * 这里从工具上下文取回当前会话的 Subject，绑定到执行线程，执行结束后还原线程原有状态，
 * 避免线程池复用（Reactor、异步任务线程均为池化线程）造成上一个会话的用户被带到下一个会话。
 * @author ThinkGem
 */
@Aspect
@Component
public class AiToolSubjectAspect {

	private static final Logger logger = LoggerFactory.getLogger(AiToolSubjectAspect.class);

	/**
	 * 给 Tool 传递当前用户信息
	 */
	@Around("@annotation(org.springframework.ai.tool.annotation.Tool)")
	public Object handleThreadContext(ProceedingJoinPoint joinPoint) throws Throwable {
		Subject subject = getToolContextSubject(joinPoint);
		if (subject == null) {
			subject = getTransportContextSubject();
		}
		if (subject == null) {
			logger.debug("工具方法执行时未获取到当前会话用户信息：{}", joinPoint.getSignature().toShortString());
			return joinPoint.proceed();
		}
		// 记录线程原有的 Subject，执行结束后还原，避免线程复用导致会话串号
		Subject previous = ThreadContext.getSubject();
		ThreadContext.bind(subject);
		try {
			return joinPoint.proceed();
		} finally {
			if (previous == null) {
				ThreadContext.unbindSubject();
			} else {
				ThreadContext.bind(previous);
			}
		}
	}

	/**
	 * 从工具方法入参中获取当前用户信息
	 * <p>
	 * 工具方法可声明 {@link ToolContext} 参数（Spring AI 不将其暴露给模型），
	 * 由调用方通过 {@code ChatClient.toolContext(...)} 传入，优先级最高。
	 * @param joinPoint 切点
	 */
	protected Subject getToolContextSubject(ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if (args != null) {
			for (Object arg : args) {
				if (arg instanceof ToolContext toolContext) {
					if (toolContext.getContext().get(AiToolContextProvider.SUBJECT_KEY) instanceof Subject) {
						return (Subject) toolContext.getContext().get(AiToolContextProvider.SUBJECT_KEY);
					}
				}
			}
		}
		return null;
	}

	/**
	 * 从工具上下文（线程变量、Reactor 上下文）中获取当前用户信息
	 */
	protected Subject getTransportContextSubject() {
		McpTransportContext context = AiToolContextProvider.INSTANCE.get();
		if (context != null && context.get(AiToolContextProvider.SUBJECT_KEY) instanceof Subject) {
			return (Subject) context.get(AiToolContextProvider.SUBJECT_KEY);
		}
		return null;
	}

}
