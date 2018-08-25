/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.web.http;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.mapper.XmlMapper;

/**
 * Http与Servlet工具类.
 * @author ThinkGem
 * @version 2014-8-19
 */
public class ServletUtils {

	public static final String DEFAULT_PARAMS_PARAM = "params";			// 登录扩展参数（JSON字符串）优先级高于扩展参数前缀
	public static final String DEFAULT_PARAM_PREFIX_PARAM = "param_";	// 扩展参数前缀
	
	// 定义静态文件后缀；静态文件排除URI地址
	private static String[] staticFiles;
	private static String[] staticFileExcludeUri;
	
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
				response.sendRedirect(request.getContextPath() + url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否是Ajax异步请求
	 * @param request
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		
		String accept = request.getHeader("accept");
		if (accept != null && accept.indexOf("application/json") != -1){
			return true;
		}

		String xRequestedWith = request.getHeader("X-Requested-With");
		if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1){
			return true;
		}
		
		String uri = request.getRequestURI();
		if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml")){
			return true;
		}
		
		String ajax = request.getParameter("__ajax");
		if (StringUtils.inStringIgnoreCase(ajax, "json", "xml")){
			return true;
		}
		
		return false;
	}

	/**
     * 判断访问URI是否是静态文件请求
	 * @throws Exception 
     */
    public static boolean isStaticFile(String uri){
		if (staticFiles == null){
			PropertiesUtils pl = PropertiesUtils.getInstance();
			try{
				staticFiles = StringUtils.split(pl.getProperty("web.staticFile"), ",");
				staticFileExcludeUri = StringUtils.split(pl.getProperty("web.staticFileExcludeUri"), ",");
			}catch(NoSuchElementException nsee){
				; // 什么也不做
			}
			if (staticFiles == null){
				try {
					throw new Exception("检测到“jeesite.yml”中没有配置“web.staticFile”属性。"
							+ "配置示例：\n#静态文件后缀\nweb.staticFile=.css,.js,.png,.jpg,.gif,"
							+ ".jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (staticFileExcludeUri != null){
			for (String s : staticFileExcludeUri){
				if (StringUtils.contains(uri, s)){
					return false;
				}
			}
		}
		if (StringUtils.endsWithAny(uri, staticFiles)){
			return true;
		}
		return false;
    }

	/**
	 * 返回结果JSON字符串（支持JsonP，请求参数加：__callback=回调函数名）
	 * @param result Global.TRUE or Globle.False
	 * @param message 执行消息
	 * @param data 消息数据
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
	@SuppressWarnings("unchecked")
	public static String renderResult(String result, String message, Object data) {
		Map<String, Object> resultMap = MapUtils.newHashMap();
		resultMap.put("result", result);
		resultMap.put("message", message);
		if (data != null){
			if (data instanceof Map){
				resultMap.putAll((Map<String, Object>)data);
			}else{
				resultMap.put("data", data);
			}
		}
		HttpServletRequest request = getRequest();
		String uri = request.getRequestURI();
		if (StringUtils.endsWithIgnoreCase(uri, ".xml") || StringUtils
				.equalsIgnoreCase(request.getParameter("__ajax"), "xml")){
			return XmlMapper.toXml(resultMap);
		}else{
			String functionName = request.getParameter("__callback");
			if (StringUtils.isNotBlank(functionName)){
				return JsonMapper.toJsonp(functionName, resultMap);
			}else{
				return JsonMapper.toJson(resultMap);
			}
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
	 * 将对象转换为JSON、XML、JSONP字符串渲染到客户端（JsonP，请求参数加：__callback=回调函数名）
	 * @param request 请求对象，用来得到输出格式的指令：JSON、XML、JSONP
	 * @param response 渲染对象
	 * @param object 待转换JSON并渲染的对象
	 * @return null
	 */
	public static String renderObject(HttpServletResponse response, Object object) {
		HttpServletRequest request = getRequest();
		String uri = request.getRequestURI();
		if (StringUtils.endsWithIgnoreCase(uri, ".xml") || StringUtils
				.equalsIgnoreCase(request.getParameter("__ajax"), "xml")){
			return renderString(response, XmlMapper.toXml(object));
		}else{
			String functionName = request.getParameter("__callback");
			if (StringUtils.isNotBlank(functionName)){
				return renderString(response, JsonMapper.toJsonp(functionName, object));
			}else{
				return renderString(response, JsonMapper.toJson(object));
			}
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
			if (type == null){
				if ((StringUtils.startsWith(string, "{") && StringUtils.endsWith(string, "}"))
						|| (StringUtils.startsWith(string, "[") && StringUtils.endsWith(string, "]"))){
					type = "application/json";
				}else if (StringUtils.startsWith(string, "<") && StringUtils.endsWith(string, ">")){
					type = "application/xml";
				}else{
					type = "text/html";
				}
			}
			response.setContentType(type);
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
		Map<String, Object> paramMap = null;
		String params =  StringUtils.trim(request.getParameter(DEFAULT_PARAMS_PARAM));
		if (StringUtils.isNotBlank(params) && StringUtils.startsWith(params, "{")) {
			paramMap = JsonMapper.fromJson(params, Map.class);
		} else {
			paramMap = getParametersStartingWith(ServletUtils.getRequest(), DEFAULT_PARAM_PREFIX_PARAM);
		}
		return paramMap;
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
