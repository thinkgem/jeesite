/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils;

import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.web.http.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.LocaleContextResolver;

import java.util.Locale;
import java.util.TimeZone;

/**
 * 本地化工具
 * @author ThinkGem
 * @version 2025-05-21
 */
public class LocaleUtils {

	private static final Boolean LANG_ENABLED = PropertiesUtils.getInstance().getPropertyToBoolean("lang.enabled", "false");
	private static final ThreadLocal<TimeZoneAwareLocaleContext> timeZoneAwareLocaleContext = new NamedThreadLocal<>("TimeZoneAwareLocaleContext");
	private static LocaleContextResolver localeResolver;

	/**
	 * 获取当前 Locale 对象，获取顺序：请求 -> 会话 -> Cookie -> lang.defaultLocale
	 */
	public static Locale getLocale() {
		return getTimeZoneAwareLocaleContext().getLocale();
	}

	/**
	 * 获取当前 TimeZone 对象，获取顺序：请求 -> 会话 -> Cookie -> lang.defaultTimeZone
	 */
	public static TimeZone getTimeZone() {
		return getTimeZoneAwareLocaleContext().getTimeZone();
	}

	/**
	 * 获取 TimeZoneAwareLocaleContext
	 */
	public static TimeZoneAwareLocaleContext getTimeZoneAwareLocaleContext() {
		TimeZoneAwareLocaleContext context = timeZoneAwareLocaleContext.get();
		if (context != null){
			return context;
		}
		if (LANG_ENABLED && localeResolver != null){
			HttpServletRequest request = ServletUtils.getRequest();
			if (request != null){
				context = (TimeZoneAwareLocaleContext)localeResolver.resolveLocaleContext(request);
			}
		}
		if (context == null){
			context = new TimeZoneAwareLocaleContext() {
				@Override
				public Locale getLocale() {
					return Locale.getDefault();
				}
				@Override
				public TimeZone getTimeZone() {
					return TimeZone.getDefault();
				}
			};
		}
		setTimeZoneAwareLocaleContext(context);
		return context;
	}

	/**
	 * 设置 TimeZoneAwareLocaleContext
	 */
	public static void setTimeZoneAwareLocaleContext(TimeZoneAwareLocaleContext context) {
		timeZoneAwareLocaleContext.set(context);
	}

	/**
	 * 清理本地线程对象（请求结束时调用）
	 */
	public static void removeTimeZoneAwareLocaleContext() {
		timeZoneAwareLocaleContext.remove();
	}

	/**
	 * 设置 LocaleContextResolver
	 */
	public static void setLocaleResolver(LocaleContextResolver localeResolver) {
		LocaleUtils.localeResolver = localeResolver;
	}

}
