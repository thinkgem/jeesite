/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.utils;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.BaseEntity;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.MapperHelper;
import com.jeesite.common.network.IpUtils;
import com.jeesite.common.utils.DiffDataUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Log;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.LogService;
import com.jeesite.modules.sys.service.MenuService;
import eu.bitwalker.useragentutils.UserAgent;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志工具类
 * @author ThinkGem
 * @version 2017-11-7
 */
public class LogUtils {

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
		if (user == null || StringUtils.isBlank(user.getUserCode()) || request == null
				|| !Global.getPropertyToBoolean("web.interceptor.log.enabled", "true")){
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
		log.setUserAgent(EncodeUtils.xssFilter(request.getHeader("User-Agent"), request));
		UserAgent userAgent = UserAgent.parseUserAgentString(log.getUserAgent());
		log.setDeviceName(userAgent.getOperatingSystem().getName());
		log.setBrowserName(userAgent.getBrowser().getName());
		log.setRequestUri(StringUtils.abbr(request.getRequestURI(), 255));
		log.setRequestParams(request.getParameterMap());
		log.setRequestMethod(request.getMethod());
		if (Global.isUseCorpModel()){
			log.setCorpCode(user.getCorpCode());
			log.setCorpName(user.getCorpName());
		}
		log.setExecuteTime(executeTime);
		log.currentUser(user);
        log.preInsert();
        
        // 获取异常对象
        Throwable throwable = ex;
        if (throwable == null){
        	throwable = ExceptionUtils.getThrowable(request);
        }
        
        // 获取原数据和修改后的目标数据对象
        Object sourceData = request.getAttribute(WebDataBinder.class.getName()+".SOURCE");
        Object targetData = request.getAttribute(WebDataBinder.class.getName()+".TARGET");

		// 异步保存日志
		logThreadPool.submit(new SaveLogThread(log, handler, request.getContextPath(),
				throwable, sourceData, targetData));
	}
	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread implements Runnable{
		
		private Log log;
		private Object handler;
		private String contextPath;
		private Throwable throwable;
		private Object sourceData;
		private Object targetData;
		
		public SaveLogThread(Log log, Object handler, String contextPath,
				Throwable throwable, Object sourceData, Object targetData){
			this.log = log;
			this.handler = handler;
			this.contextPath = contextPath;
			this.throwable = throwable;
			this.sourceData = sourceData;
			this.targetData = targetData;
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
								Table t = MapperHelper.getTableCache(type);
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
			// 如果有异常，并且不是登录登出的日志，则设置异常信息（将异常对象转换为字符串）
			log.setIsException(throwable != null && !Log.TYPE_LOGIN_LOGOUT.equals(log.getLogType()) ? Global.YES : Global.NO);
			String message = ExceptionUtils.getExceptionMessage(throwable);
			if (message != null) {
				log.setExceptionInfo(message);
			} else {
				log.setExceptionInfo(ExceptionUtils.getStackTraceAsString(throwable));
			}
			// 如果无地址并无异常日志，则不保存信息
			if (StringUtils.isBlank(log.getRequestUri()) && StringUtils.isBlank(log.getExceptionInfo())){
				return;
			}
			// 如果是修改类型的日志，则获取修改前后的差异数据
			if (Log.TYPE_UPDATE.equals(log.getLogType()) && sourceData != null && targetData != null) {
				log.setDiffModifyData(DiffDataUtils.compareToString(sourceData, targetData));
			}
			// 保存日志信息
			log.setIsNewRecord(true);
			Static.logService.insertLog(log);
			
		}
	}
	
}
