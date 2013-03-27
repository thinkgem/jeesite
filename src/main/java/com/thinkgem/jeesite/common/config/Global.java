/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.config;

import com.thinkgem.jeesite.common.utils.PropertiesLoader;

/**
 * 全局配置类
 * @author ThinkGem
 * @version 2013-03-23
 */
public class Global {
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader;
	
	/**
	 * 设置管理端访问路径（ADMIN_PATH或FRONT_PATH可允许一个为空）
	 * 1. 修改本类 ADMIN_PATH 常量
	 * 2. 修改 application.properties 中的 adminPath 属性值
	 * 3. 如果指定了 FRONT_PATH 为主页，需要修改 application.properties 文件中  web.view.index 属性值
	 */
	public static final String ADMIN_PATH = "/a";
	
	/**
	 * 设置网站前端路径（ADMIN_PATH或FRONT_PATH可允许一个为空）
	 * 1. 修改本类 FRONT_PATH 常量
	 * 2. 如果指定了 FRONT_PATH 为主页，需要修改 application.properties 文件中  web.view.index 属性值
	 */
	public static final String FRONT_PATH = "/f";
	
	/**
	 * 设置访问URL后缀
	 */
	public static final String URL_SUFFIX = ".html";
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("application.properties");
		}
		return propertiesLoader.getProperty(key);
	}

	/////////////////////////////////////////////////////////
	
	public static String getAdminPath() {
		return ADMIN_PATH;
	}
	public static String getFrontPath() {
		return FRONT_PATH;
	}
	public static String getUrlSuffix() {
		return URL_SUFFIX;
	}
	
}
