/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.utils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Cache工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
public class CacheUtils implements ApplicationContextAware {

	private static final String SYS_CACHE = "sysCache";
	
	private static CacheManager customEhcacheManager;

	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}
	
	public static Object get(String cacheName, String key) {
		Element element = customEhcacheManager.getCache(cacheName).get(key);
		return element==null?null:element.getObjectValue();
	}

	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		customEhcacheManager.getCache(cacheName).put(element);
	}

	public static void remove(String cacheName, String key) {
		customEhcacheManager.getCache(cacheName).remove(key);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext){
		customEhcacheManager = (CacheManager)applicationContext.getBean("customEhcacheManager");
	}
	
}
