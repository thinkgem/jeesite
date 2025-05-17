/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.authc;

import java.io.Serial;
import java.util.Map;

/**
 * LdapToken
 * @author ThinkGem
 * @version 2021-7-6
 */
public class LdapToken extends FormToken {

	@Serial
	private static final long serialVersionUID = 1L;

	public LdapToken() {
		super();
	}
	
	public LdapToken(String username, char[] password, boolean rememberMe, 
			String host, Map<String, Object> params) {
		super(username, password, rememberMe, null, host, params);
	}
	
}
