/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.web.http;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.mapper.XmlMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Validate;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Http与Servlet工具类.
 * @author ThinkGem
 * @version 2014-8-19
 */
public class ServletUtils {

	public static final String EXT_PARAMS_PREFIX = "param_";	// 扩展参数前缀
	
	// 定义静态文件后缀；静态文件排除URI地址
	private static final PropertiesUtils PROPS = PropertiesUtils.getInstance();
	private static final String[] STATIC_FILE = StringUtils.splitComma(PROPS.getProperty("web.staticFile"));
	private static final String[] STATIC_FILE_EXCLUDE_URI = StringUtils.splitComma(PROPS.getProperty("web.staticFileExcludeUri"));

	// XSS 过滤器要排除的URI地址
	public static final String[] XSS_FILE_EXCLUDE_URI = StringUtils.splitComma(PROPS.getProperty("web.xssFilterExcludeUri"));

	// AJAX 请求参数和请求头名
	public static final String AJAX_PARAM_NAME = PROPS.getProperty("web.ajaxParamName", "__ajax");
	public static final String AJAX_HEADER_NAME = PROPS.getProperty("web.ajaxHeaderName", "x-ajax");
	
	// MVC 偏好设置，根据后缀、参数、Header 返回特定格式数据
	public static final Boolean FAVOR_PATH_EXTENSION = PROPS.getPropertyToBoolean("web.view.favorPathExtension", "false");
	public static final Boolean FAVOR_PARAMETER = PROPS.getPropertyToBoolean("web.view.favorParameter", "true");
	public static final Boolean FAVOR_HEADER = PROPS.getPropertyToBoolean("web.view.favorHeader", "true");
	
	// JSONP 支持（为兼用旧版保留，建议使用 CORS）
	public static final Boolean JSONP_ENABLED = PROPS.getPropertyToBoolean("web.jsonp.enabled", "false");
	public static final String  JSONP_CALLBACK = PROPS.getProperty("web.jsonp.callback", "__callback");
	
	// 是否打印错误信息参数到视图页面（生产环境关闭）
	private static final Boolean PRINT_ERROR_INFO = PROPS.getPropertyToBoolean("error.page.printErrorInfo", "true");

	/**
	 * 获取当前请求对象
	 * web.xml: <listener><listener-class>
	 * 	org.springframework.web.context.request.RequestContextListener
	 * 	</listener-class></listener>
	 */
	public static HttpServletRequest getRequest(){
		HttpServletRequest request = null;
		try{
			request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			if (request == null){
				return null;
			}
			return request;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 获取当前相应对象
	 * web.xml: <filter><filter-name>requestContextFilter</filter-name><filter-class>
	 * 	org.springframework.web.filter.RequestContextFilter</filter-class></filter><filter-mapping>
	 * 	<filter-name>requestContextFilter</filter-name><url-pattern>/*</url-pattern></filter-mapping>
	 */
	public static HttpServletResponse getResponse(){
		HttpServletResponse response = null;
		try{
			response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
			if (response == null){
				return null;
			}
		}catch(Exception e){
			return null;
		}
		return response;
	}
	
	/**
	 * 支持AJAX的页面跳转
	 */
	public static void redirectUrl(HttpServletRequest request, HttpServletResponse response, String url){
		try {
			if (ServletUtils.isAjaxRequest(request)){
				request.getRequestDispatcher(url).forward(request, response); // AJAX不支持Redirect改用Forward
			}else{
				if (StringUtils.contains(url, "://")){
					response.sendRedirect(url);
				}else{
					String ctxPath = PropertiesUtils.getInstance().getProperty("ctxPath", request.getContextPath());
					response.sendRedirect(ctxPath + url);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断访问URI是否是静态文件请求
	 * @throws Exception 
	 */
	public static boolean isStaticFile(String uri){
		if (STATIC_FILE == null){
			try {
				throw new Exception("检测到“application.yml”中没有配置“web.staticFile”属性。"
						+ "配置示例：\n#静态文件后缀\nweb.staticFile=.css,.js,.png,.jpg,.gif,"
						+ ".jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.containsAny(uri, STATIC_FILE_EXCLUDE_URI)) {
			return false;
		}
		if (StringUtils.endsWithAny(uri, STATIC_FILE)){
			return true;
		}
		return false;
	}

	/**
	 * 是否是Ajax异步请求
	 * @param request
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		
		String accept = request.getHeader("accept");
		if (StringUtils.contains(accept, MediaType.APPLICATION_JSON_VALUE)){
			return true;
		}
		
		String xRequestedWith = request.getHeader("X-Requested-With");
		if (StringUtils.contains(xRequestedWith, "XMLHttpRequest")){
			return true;
		}
		
		if (FAVOR_PATH_EXTENSION) {
			String uri = request.getRequestURI();
			if (StringUtils.endsWithIgnoreCase(uri, ".json")
					|| StringUtils.endsWithIgnoreCase(uri, ".xml")){
				return true;
			}
		}
		
		if (FAVOR_PARAMETER) {
			String ajaxParameter = request.getParameter(AJAX_PARAM_NAME);
			if (StringUtils.inStringIgnoreCase(ajaxParameter, "json", "xml")){
				return true;
			}
		}
		
		if (FAVOR_HEADER) {
			String ajaxHeader = request.getHeader(AJAX_HEADER_NAME);
			if (StringUtils.inStringIgnoreCase(ajaxHeader, "json", "xml")){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 返回结果JSON字符串（支持JsonP，请求参数加：__callback=回调函数名）
	 * @param result Global.TRUE or Globle.False
	 * @param message 执行消息
	 * @return JSON字符串：{result:'true',message:''}
	 */
	public static String renderResult(String result, String message) {
		return renderResult(result, message, null);
	}
	
	/**
	 * 返回结果JSON字符串（支持JsonP，请求参数加：__callback=回调函数名）
	 * @param result Global.TRUE or Globle.False
	 * @param message 执行消息
	 * @param data 消息数据
	 * @return JSON字符串：{result:'true',message:'', if map then key:value,key2:value2... else data:{} }
	 */
	public static String renderResult(String result, String message, Object data) {
		return renderResult(result, message, data, null);
	}
	
	/**
	 * 返回结果JSON字符串（支持JsonP，请求参数加：__callback=回调函数名）
	 * @param result Global.TRUE or Globle.False
	 * @param message 执行消息
	 * @param data 消息数据
	 * @param jsonView 根据 JsonView 过滤
	 * @return JSON字符串：{result:'true',message:'', if map then key:value,key2:value2... else data:{} }
	 */
	@SuppressWarnings("unchecked")
	public static String renderResult(String result, String message, Object data, Class<?> jsonView) {
		Map<String, Object> resultMap = MapUtils.newHashMap();
		resultMap.put("result", result);
		resultMap.put("message", message);
		if (data != null){
			if (data instanceof Throwable){
				Throwable ex = (Throwable)data;
				String exMsg = ExceptionUtils.getExceptionMessage(ex);
				if (StringUtils.isNotBlank(exMsg)){
					resultMap.put("message", message + "，" + exMsg);
				}else if (PRINT_ERROR_INFO){
					resultMap.put("message", message + "，" + ex.getMessage());
				}
			}else if (data instanceof Map){
				resultMap.putAll((Map<String, Object>)data);
			}else{
				resultMap.put("data", data);
			}
		}
		Object object = null;
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
		if (request != null){
			String uri = request.getRequestURI();
			if ((FAVOR_PATH_EXTENSION && StringUtils.endsWithIgnoreCase(uri, ".xml"))
					|| (FAVOR_PARAMETER && StringUtils.equalsIgnoreCase(request.getParameter(AJAX_PARAM_NAME), "xml"))){
				if (response != null){
					response.setContentType(MediaType.APPLICATION_XML_VALUE);
				}
				if (jsonView != null) {
					return XmlMapper.toXml(resultMap, jsonView);
				}else {
					return XmlMapper.toXml(resultMap);
				}
			}
			if (JSONP_ENABLED) {
				String functionName = request.getParameter(JSONP_CALLBACK);
				if (StringUtils.isNotBlank(functionName)){
					object = new JSONPObject(functionName, resultMap);
				}
			}
		}
		if (response != null){
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(EncodeUtils.UTF_8);
		}
		if (object == null) {
			object = resultMap;
		}
		object = ResultUtils.result(object, request, response);
		if (jsonView != null) {
			return JsonMapper.toJson(object, jsonView);
		}else {
			return JsonMapper.toJson(object);
		}
	}
	
	/**
	 * 直接将结果JSON字符串渲染到客户端（支持JsonP，请求参数加：__callback=回调函数名）
	 * @param response 渲染对象：{result:'true',message:'',data:{}}
	 * @param result Global.TRUE or Globle.False
	 * @param message 执行消息
	 * @return null
	 */
	public static String renderResult(HttpServletResponse response, String result, String message) {
		return renderString(response, renderResult(result, message), null);
	}
	
	/**
	 * 直接将结果JSON字符串渲染到客户端（支持JsonP，请求参数加：__callback=回调函数名）
	 * @param response 渲染对象：{result:'true',message:'',data:{}}
	 * @param result 结果标识：Global.TRUE or Globle.False
	 * @param message 执行消息
	 * @param data 消息数据
	 * @return null
	 */
	public static String renderResult(HttpServletResponse response, String result, String message, Object data) {
		return renderString(response, renderResult(result, message, data), null);
	}
	
	/**
	 * 直接将结果JSON字符串渲染到客户端（支持JsonP，请求参数加：__callback=回调函数名）
	 * @param response 渲染对象：{result:'true',message:'',data:{}}
	 * @param result 结果标识：Global.TRUE or Globle.False
	 * @param message 执行消息
	 * @param data 消息数据
	 * @param jsonView 根据 JsonView 过滤
	 * @return null
	 */
	public static String renderResult(HttpServletResponse response, String result, String message, Object data, Class<?> jsonView) {
		return renderString(response, renderResult(result, message, data, jsonView), null);
	}
	
	/**
	 * 将对象转换为JSON、XML、JSONP字符串渲染到客户端（JsonP，请求参数加：__callback=回调函数名）
	 * @param response 渲染对象
	 * @param object 待转换JSON并渲染的对象
	 * @return null
	 */
	public static String renderObject(HttpServletResponse response, Object object) {
		return renderObject(response, object, null);
	}
	
	/**
	 * 将对象转换为JSON、XML、JSONP字符串渲染到客户端（JsonP，请求参数加：__callback=回调函数名）
	 * @param response 渲染对象
	 * @param object 待转换JSON并渲染的对象
	 * @param jsonView 根据 JsonView 过滤
	 * @return null
	 */
	public static String renderObject(HttpServletResponse response, Object object, Class<?> jsonView) {
		HttpServletRequest request = getRequest();
		String uri = request.getRequestURI();
		if ((FAVOR_PATH_EXTENSION && StringUtils.endsWithIgnoreCase(uri, ".xml"))
				|| (FAVOR_PARAMETER && StringUtils.equalsIgnoreCase(request.getParameter(AJAX_PARAM_NAME), "xml"))){
			return renderString(response, XmlMapper.toXml(object));
		}
		if (JSONP_ENABLED) {
			String functionName = request.getParameter(JSONP_CALLBACK);
			if (StringUtils.isNotBlank(functionName)){
				object = new JSONPObject(functionName, object);
			}
		}
		object = ResultUtils.result(object, request, response);
		if (jsonView != null) {
			return renderString(response, JsonMapper.toJson(object, jsonView));
		}else {
			return renderString(response, JsonMapper.toJson(object));
		}
	}
	
	/**
	 * 将字符串渲染到客户端
	 * @param response 渲染对象
	 * @param string 待渲染的字符串
	 * @return null
	 */
	public static String renderString(HttpServletResponse response, String string) {
		return renderString(response, string, null);
	}
	
	/**
	 * 将字符串渲染到客户端
	 * @param response 渲染对象
	 * @param string 待渲染的字符串
	 * @return null
	 */
	public static String renderString(HttpServletResponse response, String string, String type) {
		try {
//			response.reset(); // 注释掉，否则以前设置的Header会被清理掉，如ajax登录设置记住我的Cookie信息
			if (type == null && StringUtils.isBlank(response.getContentType())){
				type = getContentType(string);
			}
			if (type != null) {
				response.setContentType(type);
				response.setCharacterEncoding(EncodeUtils.UTF_8);
			}
			response.getWriter().print(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据内容判断相应类型 MediaType
	 * @return application/json、text/html、application/xml、text/plain
	 */
	public static String getContentType(String string) {
		String type;
		if ((StringUtils.startsWith(string, "{") && StringUtils.endsWith(string, "}"))
				|| (StringUtils.startsWith(string, "[") && StringUtils.endsWith(string, "]"))){
			type = MediaType.APPLICATION_JSON_VALUE;
		}else if (StringUtils.startsWith(string, "<") && StringUtils.endsWith(string, ">")){
			if (StringUtils.startsWith(string, "<!DOCTYPE")){
				type = MediaType.TEXT_HTML_VALUE;
			}else{
				type = MediaType.APPLICATION_XML_VALUE;
			}
		}else{
			type = MediaType.TEXT_PLAIN_VALUE;
		}
		return type;
	}

	/**
	 * 获取请求的域名（含端口）
	 */
	public static String getRequestDomain(String url) {
		String scheme = StringUtils.substringBefore(url, "://");
		String domain = StringUtils.substringAfter(url, "://");
		if (StringUtils.contains(domain, "/")) {
			domain = StringUtils.substringBefore(domain, "/");
		}
		return scheme + "://" + domain;
	}

	/**
	 * 获得请求参数值
	 */
	public static String getParameter(String name) {
		HttpServletRequest request = getRequest();
		if (request == null){
			return null;
		}
		return request.getParameter(name);
	}
	
	/**
	 * 获得请求参数Map
	 */
	public static Map<String, Object> getParameters() {
		return getParameters(getRequest());
	}
	
	/**
	 * 获得请求参数Map
	 */
	public static Map<String, Object> getParameters(ServletRequest request) {
		if (request == null){
			return MapUtils.newHashMap();
		}
		return getParametersStartingWith(request, "");
	}

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 返回的结果的Parameter名已去除前缀.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(pre) || paramName.startsWith(pre)) {
				String unprefixed = paramName.substring(pre.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					values = new String[]{};
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		StringBuilder queryStringBuilder = new StringBuilder();
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append("&");
			}
		}
		return queryStringBuilder.toString();
	}

	/**
	 * 从请求对象中扩展参数数据，格式：JSON 或  param_ 开头的参数
	 * @param request 请求对象
	 * @return 返回Map对象
	 */
	public static Map<String, Object> getExtParams(ServletRequest request) {
//		Map<String, Object> paramMap = null;
//		String params = StringUtils.trim(request.getParameter(DEFAULT_PARAMS_PARAM));
//		if (StringUtils.isNotBlank(params) && StringUtils.startsWith(params, "{")) {
//			paramMap = JsonMapper.fromJson(params, Map.class);
//		} else {
//			paramMap = getParametersStartingWith(request, DEFAULT_PARAM_PREFIX_PARAM);
//		}
		return getParametersStartingWith(request, EXT_PARAMS_PREFIX);
	}
	
	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header, set a time after now.
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader(HttpHeaders.ETAG, etag);
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * @param lastModified 内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified) {
		long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * @param etag 内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader(HttpHeaders.ETAG, etag);
				return false;
			}
		}
		return true;
	}

}
