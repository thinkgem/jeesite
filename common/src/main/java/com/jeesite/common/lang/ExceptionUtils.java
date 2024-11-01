/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.lang;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类.
 * @author calvin
 * @version 2014-8-19
 */
public class ExceptionUtils {

	/**
	 * 在request中获取异常类
	 * @param request
	 * @return 
	 */
	public static Throwable getThrowable(HttpServletRequest request){
		Throwable ex = null;
		if (request.getAttribute("exception") != null) {
			ex = (Throwable) request.getAttribute("exception");
		} else if (request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) != null) {
			ex = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		} else if (request.getAttribute("org.springframework.web.servlet.DispatcherServlet.EXCEPTION") != null) {
			ex = (Throwable) request.getAttribute("org.springframework.web.servlet.DispatcherServlet.EXCEPTION");
		}
		return ex;
	}
	
	public static String getExceptionMessage(Throwable ex){
		String message = null;
		Throwable e = ex;
		while (true){
			if (e == null){
				break;
			}
			if (StringUtils.startsWith(e.getMessage(), "msg:")){
				message = StringUtils.replace(e.getMessage(), "msg:", "");
				break;
			}else if ("com.jeesite.common.service.ServiceException"
					.equals(e.getClass().getName())){
				message = e.getMessage();
				break;
			}
			e = e.getCause();
		}
		return message;
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Throwable e) {
		if (e == null){
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
	
	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}
	
}
