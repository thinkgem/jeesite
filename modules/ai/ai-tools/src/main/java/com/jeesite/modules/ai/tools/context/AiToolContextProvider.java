/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.context;

import io.modelcontextprotocol.common.McpTransportContext;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.model.tool.internal.ToolCallReactiveContextHolder;
import org.springframework.util.Assert;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Mcp 和 Tool 上下文提供者
 * @author ThinkGem
 */
public class AiToolContextProvider implements Supplier<McpTransportContext> {

	public static final String SUBJECT_KEY = Subject.class.getName();
	public static final String SESSION_ID_KEY = Session.class.getName() + "Id";
	public static AiToolContextProvider INSTANCE = new AiToolContextProvider();

	protected static final Logger logger = LoggerFactory.getLogger(AiToolContextProvider.class);
	protected final boolean reactiveContextHolderAvailable;

	public AiToolContextProvider() {
		boolean reactiveContextHolderAvailable = false;
		try {
			Class.forName("org.springframework.ai.model.tool.internal.ToolCallReactiveContextHolder");
			reactiveContextHolderAvailable = true;
		}
		catch (ClassNotFoundException ignored) { }
		this.reactiveContextHolderAvailable = reactiveContextHolderAvailable;
	}

	public static ContextView contextWrite() {
		return Context.empty().put(McpTransportContext.KEY, fromThreadLocals());
	}

	@Override
	public McpTransportContext get() {
		var transportContext = fromThreadLocals();
		if (this.reactiveContextHolderAvailable && transportContext == McpTransportContext.EMPTY) {
			transportContext = fromToolCallReactiveContextHolder();
		}
		return transportContext;
	}

	private static McpTransportContext fromThreadLocals() {
		var data = new HashMap<String, Object>();
		Subject subject = ThreadContext.getSubject();
		if (subject != null) {
			data.put(SUBJECT_KEY, subject);
			try {
				String sessionId = subject.getSession().getId().toString();
				data.put(SESSION_ID_KEY, sessionId);
				logger.debug("Adding session id to McpTransportContext: {}", sessionId);
			} catch (Exception e) {
				logger.debug("Failed to get session id from Subject", e);
			}
		}
		if (data.isEmpty()) {
			logger.debug("No thread locals available, creating empty McpTransportContext");
			return McpTransportContext.EMPTY;
		}
		//logger.debug("Creating McpTransportContext from thread locals");
		return McpTransportContext.create(data);
	}

	private static McpTransportContext fromToolCallReactiveContextHolder() {
		var reactorContext = ToolCallReactiveContextHolder.getContext();
		if (reactorContext == Context.empty()) {
			logger.debug("No context in ToolCallReactiveContextHolder, creating empty McpTransportContext");
			return McpTransportContext.EMPTY;
		}
		//logger.debug("Creating McpTransportContext from ToolCallReactiveContextHolder");
		var transportContext = reactorContext.getOrDefault(McpTransportContext.KEY, McpTransportContext.EMPTY);
		Assert.notNull(transportContext, "transportContext cannot be null");
		return transportContext;
	}

}
