/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.local;

import com.jeesite.modules.ai.tools.annotation.AiTools;
import com.jeesite.modules.ai.tools.impl.UserAiTools;
import com.jeesite.modules.sys.service.EmpUserService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * AI 本地工具调用（当前用户、用户查询）
 * <p>
 * 本地工具由 ChatClient 的 defaultTools 直接调用，不经过 MCP 服务，
 * 流式对话时执行在异步线程上，当前会话用户由 {@code AiToolSubjectAspect} 执行前注入线程变量，
 * 工具内直接使用 {@code UserUtils.getUser()} 即可获取当前登录用户。
 * @author ThinkGem
 */
@AiTools
@ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "false", matchIfMissing = true)
public class LocalUserAiTools extends UserAiTools {

	public LocalUserAiTools(EmpUserService empUserService) {
		super(empUserService);
	}

	/**
	 * 获取当前会话的用户信息
	 */
	@Tool(name="current-user", description = "无条件获取当前用户信息")
	public String getCurrentUser() {
		return super.getCurrentUser();
	}

	/**
	 * 查询用户信息
	 */
	@Tool(
			name="find-user-info",
			description = "根据用户名（登录账号）或员工姓名模糊查询用户信息。结果以表格形式展示，" +
					"包含用户名userName、姓名empUser、部门officeName等基本信息。"
	)
	public String findEmpUserInfo(@ToolParam(description = "用户的登录名或员工的真实姓名，支持模糊匹配") String userName) {
		return super.findEmpUserInfo(userName);
	}

}
