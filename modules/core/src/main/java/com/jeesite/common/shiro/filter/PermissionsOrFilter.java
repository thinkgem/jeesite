/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.shiro.filter;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.shiro.subject.Subject;

import java.io.IOException;

/**
 * 权限字符串过滤器（OR）
 * @author ThinkGem
 * @version 2026-08-24
 */
public class PermissionsOrFilter extends PermissionsFilter {

	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		Subject subject = getSubject(request, response);
		String[] perms = (String[]) mappedValue;
		if (perms == null || perms.length == 0) {
			return true;
		}
		for (String perm : perms) {
			if (subject.isPermitted(perm)) {
				return true;
			}
		}
		return false;
	}
}
