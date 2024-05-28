/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.web;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.StringUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
public class CookieUtils {

	/**
	 * 设置 Cookie（生存时间为30天）
	 * @param name 名称
	 * @param value 值
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, 60*60*24*30);
	}
	
	/**
	 * 设置 Cookie
	 * @param name 名称
	 * @param value 值
	 * @param path 路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path) {
		setCookie(response, name, value, path, 60*60*24*30);
	}
	
	/**
	 * 设置 Cookie
	 * @param name 名称
	 * @param value 值
	 * @param maxAge 生存时间（单位秒）
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		setCookie(response, name, value, "/", maxAge);
	}
	
	/**
	 * 设置 Cookie
	 * @param name 名称
	 * @param value 值
	 * @param path 路径
	 * @param maxAge 生存时间（单位秒）
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
		if (StringUtils.isNotBlank(name)){
			name = EncodeUtils.encodeUrl(name);
			value = EncodeUtils.encodeUrl(value);
			Cookie cookie = new Cookie(name, value);
			cookie.setPath(path);
			cookie.setMaxAge(maxAge);
			PropertiesUtils props = PropertiesUtils.getInstance();
			cookie.setSecure(props.getPropertyToBoolean("session.sessionIdCookieSecure", "false"));
			cookie.setHttpOnly(props.getPropertyToBoolean("session.sessionIdCookieHttpOnly", "true"));
			response.addCookie(cookie);
		}
	}
	
	/**
	 * 获得指定Cookie的值
	 * @param name 名称
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		return getCookie(request, null, name, false);
	}
	
	/**
	 * 获得指定Cookie的值，并删除。
	 * @param name 名称
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		return getCookie(request, response, name, false);
	}

	/**
	 * 获得指定Cookie的值
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param name 名字
	 * @param isRemove 是否移除
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove) {
		String ctxPath = PropertiesUtils.getInstance().getProperty("ctxPath", request != null ? request.getContextPath() : StringUtils.EMPTY);
		return getCookie(request, response, name, ctxPath, isRemove);
	}

	/**
	 * 获得指定Cookie的值
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param name 名字
	 * @param isRemove 是否移除
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, String path, boolean isRemove) {
		String value = null;
		if (StringUtils.isNotBlank(name)){
			name = EncodeUtils.encodeUrl(name);
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(name)) {
						value = EncodeUtils.decodeUrl(cookie.getValue());
						value = EncodeUtils.xssFilter(value, request);
						if (isRemove && response != null) {
							cookie.setPath(path);
							cookie.setMaxAge(0);
							response.addCookie(cookie);
						}
					}
				}
			}
		}
		return value;
	}
}
