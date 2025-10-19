/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI MCP 工具调用
 * @author ThinkGem
 */
@Component
public class UserAITools {

	private final Logger logger = LoggerFactory.getLogger(UserAITools.class);

	private final EmpUserService empUserService;

	public UserAITools(EmpUserService empUserService) {
		this.empUserService = empUserService;
	}

	/**
	 * 获取当前会话的用户信息
	 */
	@Tool(name="当前用户信息", description = "无条件获取当前用户信息")
	public String getCurrentUser(ToolContext toolContext) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isBlank(currentUser.getUserCode())) {
			logger.info("当前用户信息 ============== 当前用户未登录。");
			return "当前用户未登录。";
		}
		String result = JsonMapper.toJson(currentUser);
		logger.info("当前用户信息 ============== 查询结果：{}", result);
		return result;
	}


	/**
	 * 查询用户信息
	 */
	@Tool(name="查询用户信息", description = "根据用户名（登录账号）或员工姓名模糊查询用户信息。" +
			"结果以表格形式展示，包含用户名userName、姓名empUser、部门officeName等基本信息。")
	public String findEmpUserInfo(ToolContext toolContext,
		@ToolParam(description = "用户的登录名或员工的真实姓名，支持模糊匹配") String userName
	) {
		EmpUser where = new EmpUser();
		where.sqlMap().getWhere().and(w -> w
				.or("a.user_name", QueryType.LIKE, userName)
				.or("e.emp_name", QueryType.LIKE, userName));
		// 权限控制，只能查询当前用户能查询的用户信息
		logger.info("获取用户信息 ============== 当前用户: {}", where.currentUser().getUserCode());
		empUserService.addDataScopeFilter(where);
		List<EmpUser> list = empUserService.findList(where);
		String result = JsonMapper.toJson(list);
		logger.info("获取用户信息 ============== 查询结果： {}", result);
		if (list.isEmpty()) {
			return "未找到符合条件的用户信息。";
		}
		return result;
	}

}