/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.lang;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author ThinkGem
 * @version 2018-1-6
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static final String DOT = ".";
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String TILDE = "~";
	public static final String UNDERLINE = "_";

	/**
	 * 分隔字符串（默认逗号分隔）
	 * @param str
	 * @return
	 */
	public static String[] splitComma(final String str) {
		return split(str, COMMA);
	}

	/**
	 * 连接字符串（默认逗号分隔）
	 * @param iterable
	 * @return
	 */
	public static String joinComma(final Iterable<?> iterable) {
		return join(iterable, COMMA);
	}

    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
    	if (str != null){
			return str.getBytes(StandardCharsets.UTF_8);
		}else{
    		return null;
    	}
    }
    
    /**
     * 转换为字节数组
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes){
		return new String(bytes, StandardCharsets.UTF_8);
	}
    
    /**
     * 是否包含字符串
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs){
    	if (str != null && strs != null){
        	for (String s : strs){
        		if (str.equals(trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
    }
    
    /**
     * 是否包含字符串
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs){
    	if (str != null && strs != null){
        	for (String s : strs){
        		if (str.equalsIgnoreCase(trim(s))){
        			return true;
        		}
        	}
    	}
    	return false;
    }
    
    /**
     * 去除左右空格（包含中文空格）
     * @param str
     */
    public static String trim2(final String str) {
        return str == null ? null : str.replaceAll("^[\\s|　| ]*|[\\s|　| ]*$", "");
    }
	
	/**
	 * 替换掉HTML标签方法
	 */
	public static String stripHtml(String html) {
		if (isBlank(html)){
			return EMPTY;
		}
		//html.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "");
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		return m.replaceAll(EMPTY);
	}
	
	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param html
	 * @return
	 */
	public static String toMobileHtml(String html){
		if (isBlank(html)){
			return EMPTY;
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}
	
	/**
	 * 对txt进行HTML编码，并将\n转换为&gt;br/&lt;、\t转换为&nbsp; &nbsp;
	 * @param txt
	 * @return
	 */
	public static String toHtml(String txt){
		if (isBlank(txt)){
			return EMPTY;
		}
		return replace(replace(EncodeUtils.encodeHtml(trim(txt)), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (isBlank(str)){
			return EMPTY;
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : stripHtml(EncodeUtils.decodeHtml(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return EMPTY;
	}
	
	// 缩略字符串替换Html正则表达式预编译
	private static Pattern p1 = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
	
	/**
	 * 缩略字符串（适应于与HTML标签的）
	 * @param param 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String htmlAbbr(String param, int length) {
		if (isBlank(param)){
			return EMPTY;
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}
			try {
				if (!isCode && !isHTML) {
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String tempResult = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
		// 去掉不需要结素标记的HTML标记
		tempResult = tempResult.replaceAll("</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|"
				+ "HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|"
				+ "basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|"
				+ "option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>", "");
		// 去掉成对的HTML标记
		tempResult = tempResult.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
		// 用正则表达式取出标记
		Matcher m = p1.matcher(tempResult);
		List<String> endHTML = ListUtils.newArrayList();
		while (m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}
	
	/**
	 * 首字母大写
	 */
	public static String cap(String str){
		return capitalize(str);
	}
	
	/**
	 * 首字母小写
	 */
	public static String uncap(String str){
		return uncapitalize(str);
	}
	
	/**
	 * 驼峰命名法工具
	 * @return
	 * 		camelCase("hello_world") == "helloWorld" 
	 * 		capCamelCase("hello_world") == "HelloWorld"
	 * 		uncamelCase("helloWorld") = "hello_world"
	 */
    public static String camelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == UNDERLINE.charAt(0)) {
                upperCase = i != 1; // 不允许第二个字符是大写
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
	 * 驼峰命名法工具
	 * @return
	 * 		camelCase("hello_world") == "helloWorld" 
	 * 		capCamelCase("hello_world") == "HelloWorld"
	 * 		uncamelCase("helloWorld") = "hello_world"
	 */
    public static String capCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = camelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    /**
	 * 驼峰命名法工具
	 * @return
	 * 		camelCase("hello_world") == "helloWorld" 
	 * 		capCamelCase("hello_world") == "HelloWorld"
	 * 		uncamelCase("helloWorld") = "hello_world"
	 */
    public static String uncamelCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(UNDERLINE);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
    
    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     * @param objectString 对象串
     *   例如：row.user.id
     *   返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString){
    	StringBuilder result = new StringBuilder();
    	StringBuilder val = new StringBuilder();
    	String[] vals = split(objectString, ".");
    	for (int i=0; i<vals.length; i++){
    		val.append("." + vals[i]);
    		result.append("!"+(val.substring(1))+"?'':");
    	}
    	result.append(val.substring(1));
    	return result.toString();
    }

	/**
	 * 获取随机字符串
	 * @param count
	 * @return
	 */
	public static String getRandomStr(int count) {
		char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < count; i++) {
			String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
			s.append(r);
		}
		return s.toString();
	}

	/**
	 * 获取随机数字
	 * @param count
	 * @return
	 */
	public static String getRandomNum(int count) {
		char[] codeSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		Random random = new Random();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < count; i++) {
			String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
			s.append(r);
		}
		return s.toString();
	}
	
	/**
	 * 获取树节点名字
	 * @param isShowCode 是否显示编码<br>
	 * 		true or 1：显示在左侧：(code)name<br>
	 * 		2：显示在右侧：name(code)<br>
	 * 		false or null：不显示编码：name
	 * @param code 编码
	 * @param name 名称
	 * @return 
	 */
	public static String getTreeNodeName(String isShowCode, String code, String name) {
		if ("true".equals(isShowCode) || "1".equals(isShowCode)) {
			return "(" + code + ") " + StringUtils.replace(name, SPACE, EMPTY);
		} else if ("2".equals(isShowCode)) {
			return name/*StringUtils.replace(name, " ", "")*/ + " (" + code + ")";
		} else {
			return name/*StringUtils.replace(name, " ", "")*/;
		}
	}
	
}
