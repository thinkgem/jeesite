/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.modules.cms.dao.CategoryDao;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.sys.dao.AreaDao;
import com.thinkgem.jeesite.modules.sys.dao.MenuDao;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.security.SystemRealm.Principal;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
@Service
public class UserUtils implements ApplicationContextAware {
	
	private static UserDao userDao;
	private static MenuDao menuDao;
	private static AreaDao areaDao;
	private static OfficeDao officeDao;
	private static CategoryDao categoryDao;
	
	public static User getUser(){
		User user = (User)getCache("user");
		if (user == null){
			Principal principal = (Principal)SecurityUtils.getSubject().getPrincipal();
			if (principal!=null){
				user = userDao.findByLoginName(principal.getLoginName());
				putCache("user", user);
			}
		}
		return user;
	}
	
	public static User getUser(boolean isRefresh){
		if (isRefresh){
			removeCache("user");
		}
		return getUser();
	}

	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache("menuList");
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList();
			}else{
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache("menuList", menuList);
		}
		return menuList;
	}
	
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache("areaList");
		if (areaList == null){
			User user = getUser();
			if (user.isAdmin()){
				areaList = areaDao.findAllList();
			}else{
				areaList = areaDao.findAllChild(user.getArea().getId(), "%,"+user.getArea().getId()+",%");
			}
			putCache("areaList", areaList);
		}
		return areaList;
	}
	
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache("officeList");
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao.findAllList();
			}else{
				officeList = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
			}
			putCache("officeList", officeList);
		}
		return officeList;
	}
	
	public static List<Category> getCategoryList(){
		@SuppressWarnings("unchecked")
		List<Category> categoryList = (List<Category>)getCache("categoryList");
		if (categoryList == null){
			User user = getUser();
			if (user.isAdmin()){
				categoryList = categoryDao.findAllList();
			}else{
				categoryList = categoryDao.findByUserId(user.getId());
			}
			putCache("categoryList", categoryList);
		}
		return categoryList;
	}
	
	public static List<Category> getCategoryListByModule(String module){
		List<Category> list = Lists.newArrayList();
		if (StringUtils.isNotBlank(module)){
			for (Category category : getCategoryList()){
				if (module.equals(category.getModule()) || "".equals(category.getModule())){
					list.add(category);
				}
			}
		}
		return list;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext){
		userDao = (UserDao)applicationContext.getBean("userDao");
		menuDao = (MenuDao)applicationContext.getBean("menuDao");
		areaDao = (AreaDao)applicationContext.getBean("areaDao");
		officeDao = (OfficeDao)applicationContext.getBean("officeDao");
		categoryDao = (CategoryDao)applicationContext.getBean("categoryDao");
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		Object obj = getCacheMap().get(key);
		return obj==null?null:obj;
	}

	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}

	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	
	private static Map<String, Object> getCacheMap(){
		Principal principal = (Principal)SecurityUtils.getSubject().getPrincipal();
		return principal!=null?principal.getCacheMap():new HashMap<String, Object>();
	}
}
