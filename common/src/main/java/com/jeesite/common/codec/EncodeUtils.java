/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.ServletUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 封装各种格式的编码解码工具类.
 * 1. Commons-Codec 的 hex/base64 编码
 * 2. 自制的 base62 编码
 * 3. Commons-Lang 的 xml/html escape
 * 4. JDK 提供的 URLEncoder
 * 5. XSS、SQL、orderBy 过滤器
 * @author calvin、ThinkGem
 * @version 2022-2-17
 */
public class EncodeUtils {

	public static final String UTF_8 = "UTF-8";

	private static final Logger logger = LoggerFactory.getLogger(EncodeUtils.class);
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return new String(Hex.encodeHex(input));
	}

	/**
	 * Hex解码.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(String input) {
		if (StringUtils.isBlank(input)){
			return StringUtils.EMPTY;
		}
		return new String(Base64.encodeBase64(input.getBytes(StandardCharsets.UTF_8)));
	}

//	/**
//	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
//	 */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafe(input);
//	}

	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Base64解码.
	 */
	public static String decodeBase64String(String input) {
		if (StringUtils.isBlank(input)){
			return StringUtils.EMPTY;
		}
		return new String(Base64.decodeBase64(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
	}

	/**
	 * Base62编码。
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * Html 转码.
	 */
	public static String encodeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String decodeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String encodeXml(String xml) {
		return StringEscapeUtils.escapeXml10(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String decodeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String encodeUrl(String part) {
		return encodeUrl(part, EncodeUtils.UTF_8);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String encodeUrl(String part, String encoding) {
		if (part == null){
			return null;
		}
		try {
			return URLEncoder.encode(part, encoding);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String decodeUrl(String part) {
		return decodeUrl(part, EncodeUtils.UTF_8);
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String decodeUrl(String part, String encoding) {
		if (part == null){
			return null;
		}
		try {
			return URLDecoder.decode(part, encoding);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * URL 解码（两次）, Encode默认为UTF-8.
	 */
	public static String decodeUrl2(String part) {
		return decodeUrl(decodeUrl(part));
	}

	// 预编译XSS过滤正则表达式
	private static final List<Pattern> xssPatterns = ListUtils.newArrayList(
			Pattern.compile("(<\\s*(script|link|style|iframe)([\\s\\S]*?)(>|<\\/\\s*\\1\\s*>))|(</\\s*(script|link|style|iframe)\\s*>)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("\\s*(href|src)\\s*=\\s*(\"\\s*(javascript|vbscript):[^\"]+\"|'\\s*(javascript|vbscript):[^']+'|(javascript|vbscript):[^\\s]+)\\s*(?=>)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("\\s*on[a-z]+\\s*=\\s*(\"[^\"]+\"|'[^']+'|[^\\s]+)\\s*(?=>)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("(eval\\((.*?)\\)|expression\\((.*?)\\))", Pattern.CASE_INSENSITIVE),
			Pattern.compile("^(javascript:|vbscript:)", Pattern.CASE_INSENSITIVE)
	);

	/**
	 * XSS 非法字符过滤，内容以<!--HTML-->开头的用以下规则（保留标签）
	 * @author ThinkGem
	 */
	public static String xssFilter(String text) {
		return xssFilter(text, null);
	}

	/**
	 * XSS 非法字符过滤，内容以<!--HTML-->开头的用以下规则（保留标签）
	 * @author ThinkGem
	 */
	public static String xssFilter(String text, HttpServletRequest request) {
		request = (request != null ? request : ServletUtils.getRequest());
		if (request != null && StringUtils.containsAny(request.getRequestURI(), ServletUtils.XSS_FILE_EXCLUDE_URI)) {
			return text;
		}
		String oriValue = StringUtils.trim(text);
		if (text != null){
			String value = oriValue;
			for (Pattern pattern : xssPatterns) {
				Matcher matcher = pattern.matcher(value);
				if (matcher.find()) {
					value = matcher.replaceAll(StringUtils.EMPTY);
				}
			}
			// 如果开始不是HTML，XML，JOSN格式，则再进行HTML的 "、<、> 转码。
			if (!StringUtils.startsWithIgnoreCase(value, "<!--HTML-->")    // HTML
					&& !StringUtils.startsWithIgnoreCase(value, "<?xml ")  // XML
					&& !(StringUtils.startsWith(value, "{") && StringUtils.endsWith(value, "}")) // JSON Object
					&& !(StringUtils.startsWith(value, "[") && StringUtils.endsWith(value, "]")) // JSON Array
			){
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < value.length(); i++) {
					char c = value.charAt(i);
					switch (c) {
						case '>':
							sb.append("＞");
							break;
						case '<':
							sb.append("＜");
							break;
						case '\'':
							sb.append("＇");
							break;
						case '\"':
							sb.append("＂");
							break;
						default:
							sb.append(c);
							break;
					}
				}
				value = sb.toString();
			}
			if (logger.isInfoEnabled() && !value.equals(oriValue)){
				logger.info("xssFilter: {}   <=<=<=   {}   source: {}", value, text,
						request != null ? request.getRequestURL() : "common");
			}
			return value;
		}
		return null;
	}

	// 预编译SQL过滤正则表达式
	private static final Pattern sqlPattern = Pattern.compile(
			"(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|((extractvalue|updatexml|if|mid|database|rand|user)([\\s]*?)\\()"
			+ "|(\\b(select|update|and|or|delete|insert|trancate|substr|ascii|declare|exec|count|master|into"
			+ "|drop|execute|case when|sleep|union|load_file)\\b)", Pattern.CASE_INSENSITIVE);
	private static final Pattern simplePattern = Pattern.compile("[a-z0-9_\\.\\, ]*", Pattern.CASE_INSENSITIVE);

	/**
	 * SQL过滤，防止注入，传入参数输入有select相关代码，替换空。
	 * @author ThinkGem
	 */
	public static String sqlFilter(String text){
		return sqlFilter(text, "common");
	}

	/**
	 * SQL过滤，防止注入，传入参数输入有select相关代码，替换空。
	 * @author ThinkGem
	 */
	public static String sqlFilter(String text, String source){
		if (text != null){
			String value = text;
			if ("simple".equals(source) || "orderBy".equals(source)) {
				Matcher matcher = simplePattern.matcher(value);
				if (!matcher.matches()) {
					value = StringUtils.EMPTY;
				}
			} else {
				Matcher matcher = sqlPattern.matcher(value);
				if (matcher.find()) {
					value = matcher.replaceAll(StringUtils.EMPTY);
				}
			}
			if (logger.isWarnEnabled() && !value.equals(text)){
				logger.info("sqlFilter: {}   <=<=<=   {}   source: {}", value, text, source);
				return StringUtils.EMPTY;
			}
			return value;
		}
		return null;
	}

}
