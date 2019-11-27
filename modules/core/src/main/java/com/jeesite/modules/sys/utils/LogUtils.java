/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.utils;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.MapperHelper;
import com.jeesite.common.network.IpUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.common.web.http.UserAgentUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.LogService;
import com.jeesite.modules.sys.service.MenuService;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 日志工具类
 * @author ThinkGem
 * @version 2017-11-7
 */
public class LogUtils {

	// 采用线程池优化性能
	private static ExecutorService logThreadPool = new ThreadPoolExecutor(5, 20,
			60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
			new DefaultThreadFactory("log-save"));

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
	public static void saveLog(User user, HttpServletRequest request, String logTitle, String logType){
		saveLog(user, request, null, null, logTitle, logType, 0);
	}
	
	/**
	 * 保存日志
	 * @param executeTime 
	 */
	public static void saveLog(User user, HttpServletRequest request, Object handler, Exception ex, String logTitle, String logType, long executeTime){
		if (user == null || StringUtils.isBlank(user.getUserCode()) || request == null){
			return;
		}
		Log log = new Log();
		log.setLogTitle(logTitle);
		log.setLogType(logType);
		if (StringUtils.isBlank(log.getLogType())){
			String sqlCommandTypes = ObjectUtils.toString(request.getAttribute(SqlCommandType.class.getName()));
			if (StringUtils.containsAny(","+sqlCommandTypes+",", ",INSERT,", ",UPDATE,", ",DELETE,")){
				log.setLogType(Log.TYPE_UPDATE);
			}else if (StringUtils.contains(","+sqlCommandTypes+",", ",SELECT,")){
				log.setLogType(Log.TYPE_SELECT);
			}else{
				log.setLogType(Log.TYPE_ACCESS);
			}
		}
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
		log.setExecuteTime(executeTime);
		log.setCurrentUser(user);
        log.preInsert();
        
        // 获取异常对象
        Throwable throwable = ex;
        if (throwable == null){
        	throwable = ExceptionUtils.getThrowable(request);
        }

		// 异步保存日志
		logThreadPool.submit(new SaveLogThread(log, handler, request.getContextPath(), throwable));
	}
	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		
		private Log log;
		private Object handler;
		private String contextPath;
		private Throwable throwable;
		
		public SaveLogThread(Log log, Object handler, String contextPath, Throwable throwable){
			this.log = log;
			this.handler = handler;
			this.contextPath = contextPath;
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
												log.setBizType(type.getSimpleName());
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
									log.setBizType(me.getReturnType().getSimpleName());
									break;
								}
							}
						}
					}
				}
				String href = log.getRequestUri();
				if (StringUtils.startsWith(href, contextPath)){
					href = StringUtils.substringAfter(href, contextPath);
				}
				if (StringUtils.startsWith(href, Global.getAdminPath())){
					href = StringUtils.substringAfter(href, Global.getAdminPath());
				}
				if (StringUtils.startsWith(href, Global.getFrontPath())){
					href = StringUtils.substringAfter(href, Global.getFrontPath());
				}
				log.setLogTitle(Static.menuService.getMenuNamePath(href, permission));
			}
			if (StringUtils.isBlank(log.getLogTitle())){
				log.setLogTitle("未知操作");
			}
			// 如果有异常，设置异常信息（将异常对象转换为字符串）
			log.setIsException(throwable != null ? Global.YES : Global.NO);
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
