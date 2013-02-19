/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.common.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.utils.DateUtils;

/**
 * 控制器支持类
 * @author ThinkGem
 * @version 2013-01-15
 */
public abstract class BaseController {
	
	/**
	 * 设置管理端访问路径（ADMIN_PATH或FRONT_PATH可允许一个为空）
	 * 1. 修改本类 ADMIN_PATH 常量
	 * 2. 修改 applicationContext-shiro.xml 中的 shiroFilter
	 * 3. 修改 decorators.xml 中的 default
	 * 4. 修改 spring-mvc.xml 中的 mvc:view-controller
	 */
	public static final String ADMIN_PATH = "/a";
	
	/**
	 * 设置网站前端路径（ADMIN_PATH或FRONT_PATH可允许一个为空）
	 * 1. 修改本类 FRONT_PATH 常量
	 * 2. 修改 spring-mvc.xml 中的 mvc:view-controller
	 */
	public static final String FRONT_PATH = "/f";

	/**
	 * 设置访问URL后缀
	 */
	public static final String URL_SUFFIX = ".html";
	
	/**
	 * 请求对象
	 */
	protected HttpServletRequest request;
	
	/**
	 * 响应对象
	 */
	protected HttpServletResponse response;
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	/**
	 * 服务端参数有效性验证
	 * @param redirectAttributes 跳转属性对象
	 * @param object 验证的实体对象
	 * @return 验证成功返回true
	 */
	protected boolean beanValidators(RedirectAttributes redirectAttributes, Object object) {
		try{
			BeanValidators.validateWithException(validator, object);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			addFlashMessage(redirectAttributes, list);
			return false;
		}
		return true;
	}
	
	/**
	 * 添加Flash消息
	 * @param redirectAttributes
	 * @param message
	 */
	protected void addFlashMessage(RedirectAttributes redirectAttributes, String message) {
		redirectAttributes.addFlashAttribute("message", message);
	}
	
	/**
	 * 添加Flash消息列表
	 * @param redirectAttributes
	 * @param message
	 */
	protected void addFlashMessage(RedirectAttributes redirectAttributes, List<String> messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message+"<br/>");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder, HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		//将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
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
