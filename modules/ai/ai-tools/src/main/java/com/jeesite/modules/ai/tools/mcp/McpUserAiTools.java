/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.mcp;

import com.jeesite.modules.ai.tools.impl.UserAiTools;
import com.jeesite.modules.sys.service.EmpUserService;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * AI MCP 工具调用
 * @author ThinkGem
 */
@Component
@ConditionalOnProperty(name = "spring.ai.mcp.server.enabled", havingValue = "true", matchIfMissing = false)
public class McpUserAiTools extends UserAiTools {

	public McpUserAiTools(EmpUserService empUserService) {
		super(empUserService);
	}

	/**
	 * 获取当前会话的用户信息
	 */
	@McpTool(name="当前用户信息", description = "无条件获取当前用户信息")
	public String getCurrentUser() {
		return super.getCurrentUser();
	}


	/**
	 * 查询用户信息
	 */
	@McpTool(
			name="查询用户信息",
			description = "根据用户名（登录账号）或员工姓名模糊查询用户信息。结果以表格形式展示，" +
					"包含用户名userName、姓名empUser、部门officeName等基本信息。"
	)
	public String findEmpUserInfo(@McpToolParam(description = "用户的登录名或员工的真实姓名，支持模糊匹配") String userName) {
		return super.findEmpUserInfo(userName);
	}

}