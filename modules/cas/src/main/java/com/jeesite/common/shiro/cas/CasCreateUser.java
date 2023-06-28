package com.jeesite.common.shiro.cas;

import com.jeesite.modules.sys.entity.User;

import java.util.Map;

/**
 * Cas登录时，本项目没有账号时调用方法
 * @author ThinkGem
 */
public interface CasCreateUser {

	/**
	 * Cas登录时，本项目没有账号时调用方法
	 */
	void createUser(User user, Map<String, Object> attributes);

}
