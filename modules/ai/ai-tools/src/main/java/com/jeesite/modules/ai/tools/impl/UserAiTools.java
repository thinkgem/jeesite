/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.tools.impl;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * AI MCP 工具调用
 * @author ThinkGem
 */
public class UserAiTools {

	private final Logger logger = LoggerFactory.getLogger(UserAiTools.class);

	private final EmpUserService empUserService;

	public UserAiTools(EmpUserService empUserService) {
		this.empUserService = empUserService;
	}

	/**
	 * 获取当前会话的用户信息
	 */
	public String getCurrentUser() {
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
	public String findEmpUserInfo(String userName) {
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