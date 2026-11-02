/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @version 2025-7-9
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
			return URLEncoder.encode(part, StringUtils.isNotBlank(encoding) ? encoding : EncodeUtils.UTF_8);
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
			return URLDecoder.decode(part, StringUtils.isNotBlank(encoding) ? encoding : EncodeUtils.UTF_8);
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
	
	/**
	 * URL 解码（针对多次编码的，可设置最大解密次数）, Encode默认为UTF-8.
	 */
	public static String decodeUrlEach(String part, int maxDecodeIterations) {
		String current = part;
		String previous;
		for (int i = 0; i < maxDecodeIterations; i++) {
			previous = current;
			current = EncodeUtils.decodeUrl(previous);
			if (StringUtils.equals(current, previous)) {
				break;
			}
		}
		return current;
	}

	// 危险标签：脚本、外链、内嵌对象，以及 SVG 动画标签（可通过 attributeName、values、from、to
	// 动态写入 href 或事件属性，如 <svg><animate attributeName=href values=javascript:alert(1)></animate></svg>）
	private static final String XSS_DANGEROUS_TAGS = "script|link|style|iframe|embed|object"
			+ "|animatetransform|animatemotion|animatecolor|animate|set";

	// 预编译XSS过滤正则表达式
	private static final List<Pattern> xssPatterns = ListUtils.newArrayList(
			Pattern.compile("(<\\s*(" + XSS_DANGEROUS_TAGS + ")([\\s\\S]*?)(>|<\\/\\s*\\1\\s*>))|(</\\s*(" + XSS_DANGEROUS_TAGS + ")\\s*>)", Pattern.CASE_INSENSITIVE),
			// 危险协议属性，无引号的值不依赖 > 结尾，防止 <a href=javascript:... 这类无标签结束符的载荷绕过；
			// data: 不在此处一刀切，只按具体类型（text/html、image/svg 等）判断，见 isDangerousUrl，
			// 避免把 data:image/png 等正常内联资源误删
			Pattern.compile("\\s*(href|src|data)\\s*=\\s*(\"\\s*(javascript|vbscript):[^\"]*\"|'\\s*(javascript|vbscript):[^']*'|(javascript|vbscript):[^\\s>]*(?=\\s|>|$))", Pattern.CASE_INSENSITIVE),
			// 事件属性，末尾无 > 时（浏览器会自动闭合标签）同样需要匹配，防止 <img onerror=alert(1) 这类载荷绕过；
			// 前导断言避免把 content=、data-onclick= 等普通属性误判为事件属性
			Pattern.compile("(?<![\\w-])\\s*/?\\s*on[a-zA-Z]+\\s*=\\s*(['\"]?)(.*?)\\1(?=\\s|>|/>|$)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("(eval\\((.*?)\\)|expression\\((.*?)\\))", Pattern.CASE_INSENSITIVE),
			Pattern.compile("^(javascript:|vbscript:)", Pattern.CASE_INSENSITIVE)
	);

	// 用于识别可能携带协议的 URL 属性，例如 href="jav&#x61;script:alert(1)"、style="url(javascript:...)"
	private static final Pattern xssUrlAttrPattern = Pattern.compile(
			"\\s*(href|src|action|formaction|xlink:href|srcset|poster|background|style|content)"
					+ "\\s*=\\s*(\"([^\"]*)\"|'([^']*)'|([^\\s>]+))",
			Pattern.CASE_INSENSITIVE);

	// HTML5 新增的命名字符实体（HTML4 不包含），浏览器在属性值中同样会解码，如 href=javascript&colon;alert(1)
	private static final Pattern xssHtmlEntityPattern = Pattern.compile(
			"&(colon|tab|newline|sol|bsol|num|period|percnt|lpar|rpar|quest|commat|nbsp);",
			Pattern.CASE_INSENSITIVE);

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
		if (StringUtils.isBlank(text)){
			return text;
		}
		String oriValue = StringUtils.trim(text);
		String value = oriValue;
		// 多次过滤，避免嵌套标签移除后重新拼接出危险标签（如 <scr<script>ipt>）
		for (int i = 0; i < 3; i++) {
			String before = value;
			for (Pattern pattern : xssPatterns) {
				Matcher matcher = pattern.matcher(value);
				if (matcher.find()) {
					value = matcher.replaceAll(StringUtils.EMPTY);
				}
			}
			// 过滤 HTML 实体编码的危险协议属性
			value = xssUrlAttrFilter(value);
			if (value.equals(before)) {
				break;
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

	/**
	 * 过滤 URL 属性中带 HTML 实体编码、空白干扰或内嵌在 style/url() 中的危险协议，
	 * 例如 href="jav&#x61;script:alert(1)"、href=java\t script:alert(1)，
	 * 浏览器会还原为 javascript 协议后执行。
	 * @author ThinkGem
	 */
	private static String xssUrlAttrFilter(String value) {
		Matcher matcher = xssUrlAttrPattern.matcher(value);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String url = matcher.group(3) != null ? matcher.group(3)
					: matcher.group(4) != null ? matcher.group(4) : matcher.group(5);
			if (isDangerousUrl(url)) {
				matcher.appendReplacement(sb, StringUtils.EMPTY);
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 属性值中是否包含危险协议（先去掉 HTML 实体与空白，避免 jav&#x61;script:、java\t script: 等变形）
	 * @param value 属性值
	 */
	private static boolean isDangerousUrl(String value) {
		// 绝大多数正常属性值不含这些关键字，一次查找即可短路
		if (!StringUtils.containsAnyIgnoreCase(value, "&", "script", "data", "vbscript")) {
			return false;
		}
		String decoded = StringUtils.deleteWhitespace(decodeHtmlEntity(value)).toLowerCase();
		if (StringUtils.contains(decoded, "javascript:") || StringUtils.contains(decoded, "vbscript:")) {
			return true;
		}
		// data: 只在可执行类型时视为危险，data:image/png、data:application/pdf 等正常内联资源保留
		return StringUtils.contains(decoded, "data:text/html")
				|| StringUtils.contains(decoded, "data:image/svg")
				|| StringUtils.contains(decoded, "data:application/xhtml")
				|| StringUtils.contains(decoded, "data:text/javascript")
				|| StringUtils.contains(decoded, "data:application/javascript");
	}

	/**
	 * 解码 URL 属性中的字符实体，除 HTML4 实体外，同时解码 HTML5 新增的命名字符实体，
	 * 例如 href="javascript&colon;alert(1)"，避免浏览器解码实体后还原出危险协议。
	 * @author ThinkGem
	 */
	private static String decodeHtmlEntity(String value) {
		String decoded = decodeHtml(value);
		if (!StringUtils.contains(decoded, "&")) {
			return decoded;
		}
		Matcher matcher = xssHtmlEntityPattern.matcher(decoded);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String replacement;
			switch (matcher.group(1).toLowerCase()) {
				case "colon": replacement = ":"; break;
				case "sol": replacement = "/"; break;
				case "bsol": replacement = "\\"; break;
				case "num": replacement = "#"; break;
				case "period": replacement = "."; break;
				case "percnt": replacement = "%"; break;
				case "lpar": replacement = "("; break;
				case "rpar": replacement = ")"; break;
				case "quest": replacement = "?"; break;
				case "commat": replacement = "@"; break;
				case "tab": replacement = "\t"; break;
				case "newline": replacement = "\n"; break;
				default: replacement = " "; break;
			}
			matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	// 预编译SQL过滤正则表达式
	private static final Pattern sqlPattern = Pattern.compile(
			"(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|((extractvalue|updatexml|if|mid|database|rand|user)([\\s]*?)\\()"
			+ "|(\\b(select|update|and|or|delete|insert|trancate|substr|ascii|declare|exec|count|master|into"
			+ "|drop|execute|case when|sleep|union|load_file)\\b)", Pattern.CASE_INSENSITIVE);
	private static final Pattern simplePattern = Pattern.compile("[a-z0-9_\\.\\, ]*", Pattern.CASE_INSENSITIVE);
	private static final Pattern columnNamePattern = Pattern.compile("[a-z0-9_\\.`\"\\[\\]]*", Pattern.CASE_INSENSITIVE);

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
		if (StringUtils.isBlank(text)){
			return text;
		}
		String value = text;
		// 如果仅为 AND 或 OR 则不认为是注入
		if (StringUtils.equalsAnyIgnoreCase(StringUtils.trim(value), "AND", "OR")) {
			return value;
		}
		// 简单数据 或 排序 类型的正则表达式验证
		else if (StringUtils.inString(source, "simple", "orderBy")) {
			Matcher matcher = simplePattern.matcher(value);
			if (!matcher.matches()) {
				value = StringUtils.EMPTY;
			}
		}
		// 数据表字段列 类型的正则表达式验证
		else if (StringUtils.inString(source, "columnName")) {
			Matcher matcher = columnNamePattern.matcher(value);
			if (!matcher.matches()) {
				value = StringUtils.EMPTY;
			}
		}
		// 其它 类型的正则表达式验证
		else {
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

    /**
	 * 定义 JDBC 连接字符串参数正则列表
	 */
	private static final List<Pattern> jdbcUrlPattern = ListUtils.newArrayList(
			// 1. MySQL / MariaDB
			Pattern.compile("\\b(allowLoadLocalInfile\\s*=\\s*true|autoDeserialize\\s*=\\s*true|queryInterceptors\\s*=|statementInterceptors\\s*=|allowUrlInLocalInfile\\s*=)", Pattern.CASE_INSENSITIVE),
			// 2. PostgreSQL / Redshift
			Pattern.compile("\\b(socketFactory\\s*=|socketFactoryArg\\s*=|sslfactory\\s*=|sslfactoryarg\\s*=|authenticationPluginClassName\\s*=)", Pattern.CASE_INSENSITIVE),
			// 3. SQL Server
			Pattern.compile("\\b(integratedSecurity\\s*=|keyPath\\s*=)", Pattern.CASE_INSENSITIVE),
			// 4. SQLite
			Pattern.compile("\\b(enable_load_extension\\s*=)", Pattern.CASE_INSENSITIVE),
			// 5. Oracle (新增)
			Pattern.compile("\\b(tns_admin\\s*=|wallet_location\\s*=|oracle\\.net\\.encryption_client\\s*=)", Pattern.CASE_INSENSITIVE),
			// 6. 通用验证规则
			Pattern.compile("(;\\s*--|EXEC\\s*\\(|CALL\\s+EXEC)", Pattern.CASE_INSENSITIVE)
	);
	
	/**
	 * 校验 JDBC URL 是否安全
	 */
	public static void validJdbcUrl(String jdbcUrl) {
		if (StringUtils.isBlank(jdbcUrl)){
			return;
		}
		String decodedUrl = decodeUrlEach(jdbcUrl, 50);
		if (StringUtils.startsWithIgnoreCase(decodedUrl,"jdbc:h2:")) {
			decodedUrl = decodedUrl.replaceAll("/\\*.*?\\*/", "");
			decodedUrl = decodedUrl.replaceAll("\\\\([A-Za-z])", "$1");
			if (StringUtils.containsIgnoreCase(decodedUrl, "INIT")) {
				throw new IllegalArgumentException("msg: H2 数据库禁止使用 INIT 参数。");
			}
		}
		for (Pattern pattern : jdbcUrlPattern) {
			if (pattern.matcher(decodedUrl).find()) {
				throw new IllegalArgumentException("msg: 不安全的 JDBC 连接参数，请检查输入。");
			}
		}
	}

	// 对邮箱和手机号进行安全处理
	private static final Pattern emailPattern = Pattern.compile("([\\w\\W]?)([\\w\\W]+)([\\w\\W])(@[\\w\\W]+)");
	private static final Pattern mobilePattern = Pattern.compile("(\\d{3})(\\d+)(\\d{3})");

	/**
	 * 手机号码进行掩码处理
	 */
	public static String mobileMask(String mobile){
		if (StringUtils.isBlank(mobile)){
			return mobile;
		}
		return mobilePattern.matcher(mobile).replaceAll("$1****$3");
	}

	/**
	 * 对电子邮箱进行掩码处理
	 */
	public static String emailMask(String email){
		if (StringUtils.isBlank(email)){
			return email;
		}
		return emailPattern.matcher(email).replaceAll("$1****$3$4");
	}
}
