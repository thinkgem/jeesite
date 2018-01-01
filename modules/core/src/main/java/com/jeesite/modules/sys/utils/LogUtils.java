/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.utils;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.MapperHelper;
import com.jeesite.common.network.IpUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.common.web.http.UserAgentUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.LogService;
import com.jeesite.modules.sys.service.MenuService;
import com.jeesite.modules.sys.utils.UserUtils;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 字典工具类
 * @author ThinkGem
 * @version 2014-11-7
 */
public class LogUtils {
	
	/**
	 * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
	 */
	private static final class Static {
		private static LogService logService = SpringUtils.getBean(LogService.class);
		private static MenuService menuService = SpringUtils.getBean(MenuService.class);
	}
	
	// 参数名获取工具（尝试获取标注为@ModelAttribute注解的方法，第一个参数名一般为主键名）
	private static ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();

	/**
	 * 保存日志
	 */
	public static void saveLog(String title){
		saveLog(null, null, null, title);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title){
		saveLog(request, null, null, title);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title){
		saveLog(null, request, handler, ex, title);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveLog(User user, HttpServletRequest request, Object handler, Exception ex, String title){
		if (user == null){
			user = UserUtils.getUser();
		}
		if (request == null){
			request = ServletUtils.getRequest();
		}
		if (request != null && user != null && StringUtils.isNotBlank(user.getUserCode())){
			Log log = new Log();
			log.setLogTitle(title);
			Throwable throwable = ex != null ? ex : ExceptionUtils.getThrowable(request);
			log.setLogType(throwable == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
			log.setServerAddr(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
			log.setRemoteAddr(IpUtils.getRemoteAddr(request));
			UserAgent userAgent = UserAgentUtils.getUserAgent(request);
			log.setDeviceName(userAgent.getOperatingSystem().getName());
			log.setBrowserName(userAgent.getBrowser().getName());
			log.setUserAgent(request.getHeader("User-Agent"));
			log.setRequestUri(StringUtils.abbr(request.getRequestURI(), 255));
			log.setRequestParams(request.getParameterMap());
			log.setRequestMethod(request.getMethod());
			if (Global.isUseCorpModel()){
				log.setCorpCode(user.getCorpCode());
				log.setCorpName(user.getCorpName());
			}
			log.setCurrentUser(user);
            log.preInsert();
			
			// 异步保存日志
			new SaveLogThread(log, handler, throwable).start();
            
		}
	}
	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		
		private Log log;
		private Object handler;
		private Throwable throwable;
		
		public SaveLogThread(Log log, Object handler, Throwable throwable){
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.handler = handler;
			this.throwable = throwable;
		}
		
		@Override
		public void run() {
			// 获取日志标题
			if (StringUtils.isBlank(log.getLogTitle())){
				String permission = "";
				if (handler instanceof HandlerMethod){
					HandlerMethod hm = ((HandlerMethod)handler);
					Method m = hm.getMethod();
					// 获取权限字符串
					RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
					permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
					
					// 尝试获取BaseEntity的设置的主键值
					for (Class<?> type : m.getParameterTypes()){
						try {
							// 判断是否是BaseEntity的子类
							Class<?> superClass = type.getSuperclass();
							while(superClass != null && superClass != BaseEntity.class){
								superClass = superClass.getSuperclass();
							};
							// 如果是BaseEntity的子类，则获取主键名
							if (superClass != null){
								Table t = type.getAnnotation(Table.class);
								for (Column c : t.columns()){
									if (c.isPK()){
										try {
											String attrName = MapperHelper.getAttrName(c);
											if (attrName != null){
												log.setBizKey(log.getRequestParam(attrName));
											}
										} catch (Exception e) {
											break;
										}
									}
								}
							}
						} catch (Exception e) {
							break;
						}
					}

					// 尝试获取标注为@ModelAttribute注解的方法，第一个参数名一般为主键名
					if (StringUtils.isBlank(log.getBizKey())){
						for (Method me : hm.getBeanType().getMethods()){
							ModelAttribute ma = AnnotationUtils.findAnnotation(me, ModelAttribute.class);
							if(ma != null){
								String[] ps = pnd.getParameterNames(me);
								if(ps != null && ps.length > 0){
									log.setBizKey(StringUtils.abbr(log.getRequestParam(ps[0]), 64));
									break;
								}
							}
						}
					}
					
					// 最后尝试获取参数为id的值
					if (StringUtils.isBlank(log.getBizKey())){
						log.setBizKey(log.getRequestParam("id"));
					}
					
				}
				log.setLogTitle(Static.menuService.getMenuNamePath(log.getRequestUri(), permission));
			}
			if (StringUtils.isBlank(log.getLogTitle())){
				if (StringUtils.contains(log.getRequestParams(), "taskCommandInfo=")){
					log.setLogTitle("我的任务-任务办理");
				}else{
					log.setLogTitle("未知操作");
				}
			}
			// 如果有异常，设置异常信息（将异常对象转换为字符串）
			log.setExceptionInfo(ExceptionUtils.getStackTraceAsString(throwable));
			// 如果无地址并无异常日志，则不保存信息
			if (StringUtils.isBlank(log.getRequestUri()) && StringUtils.isBlank(log.getExceptionInfo())){
				return;
			}
			// 保存日志信息
			log.setIsNewRecord(true);
			Static.logService.insertLog(log);
			
		}
	}

}
