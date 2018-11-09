/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.lang;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author ThinkGem
 * @version 2018-1-6
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    
    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
    	if (str != null){
    		try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    	}else{
    		return null;
    	}
    }
    
    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static String toString(byte[] bytes){
    	try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
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
			return "";
		}
		//html.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "");
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}
	
	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param html
	 * @return
	 */
	public static String toMobileHtml(String html){
		if (html == null){
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}
	
	/**
	 * 对txt进行HTML编码，并将\n转换为&gt;br/&lt;、\t转换为&nbsp; &nbsp;
	 * @param txt
	 * @return
	 */
	public static String toHtml(String txt){
		if (txt == null){
			return "";
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
		if (str == null) {
			return "";
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
		return "";
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
		if (param == null) {
			return "";
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

            if (c == SEPARATOR) {
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
                    sb.append(SEPARATOR);
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
		char[] codeSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
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
			return "(" + code + ") " + StringUtils.replace(name, " ", "");
		} else if ("2".equals(isShowCode)) {
			return StringUtils.replace(name, " ", "") + " (" + code + ")";
		} else {
			return StringUtils.replace(name, " ", "");
		}
	}
	
//	/**
//	 * 测试代码
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		String html = "<p style=\"text-align: left; padding-bottom: 0px; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/25px 宋体, tahoma, arial, sans-serif; white-space: normal; color: rgb(0,0,0); word-spacing: 0px; padding-top: 0px; -webkit-text-stroke-width: 0px\">__eol__	科技日报讯&nbsp;（通讯员辛志向&nbsp;记者王建高）日前，青岛高新区国家级生态工业示范园区通过了由国家环保部及科技部、商务部三部门联合组织的国家正式验收。</p>__eol__<p style=\"text-align: left; padding-bottom: 0px; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/25px 宋体, tahoma, arial, sans-serif; white-space: normal; color: rgb(0,0,0); word-spacing: 0px; padding-top: 0px; -webkit-text-stroke-width: 0px\">__eol__	&nbsp;&nbsp;&nbsp;&nbsp;青岛市委常委、红岛经济区工委书记、管委主任陈飞介绍，建立国家生态工业示范园区是青岛高新区深化和拓展生态创建成果，加快结构调整，推进转型发展，进而辐射带动红岛经济区生态建设的重要抓手。</p>__eol__<p style=\"text-align: left; padding-bottom: 0px; text-transform: none; background-color: rgb(255,255,255); text-indent: 0px; margin: 0px; padding-left: 0px; letter-spacing: normal; padding-right: 0px; font: 14px/25px 宋体, tahoma, arial, sans-serif; white-space: normal; color: rgb(0,0,0); word-spacing: 0px; padding-top: 0px; -webkit-text-stroke-width: 0px\">__eol__	&nbsp;&nbsp;&nbsp;&nbsp;自2007年经国家三部委批准创建以来，青岛高新区高度重视，强化措施，始终按照创建工作要求，坚持生态、生活、生产&ldquo;三生共融&rdquo;理念，以生态环境的优化带动经济的发展。创建过程中，坚持&ldquo;科学性规划，高水平建设，生态化发展&rdquo;的原则，实施&ldquo;国际化、高端化、低碳化&rdquo;三大战略，逐步建立起适应循环经济发展的产业体系和政策体系，形成了完善的生态工业园建设框架和循环经济发展机制，实现了经济高速发展与环境持续改善的良性循环。随着发展环境和核心竞争力的不断提升，通过了ISO14001环境管理体系认证，获得了中国最具投资潜力经济园区、国家科技兴贸创新基地、山东省海外高层次人才创新创业基地等称号，在国家级高新区综合评价中连续多年跻身前列，已经成为山东半岛乃至环渤海区域快速成长、极具创新活力和竞争力的蓝色经济特色高科技产业园区。</p>";
//		html += "<div style=\"text-indent: 32pt\">__eol__	<span style=\"line-height: 20px; font-size: 20px\"><span style=\"font-family: 宋体\">我市积极支持和引导企业、高校院所&ldquo;走出去&rdquo;开展国际科技合作，在国外集聚和利用各种创新资源，增强我市企业和科研院所竞争实力。</span></span></div>__eol__<div align=\"left\" style=\"text-align: left; text-indent: 32pt; text-autospace: \">__eol__	<span style=\"line-height: 20px; font-size: 20px\"><span style=\"font-family: 宋体\">一是在国外设立研发中心。通过合资、并购、控股等方式，在国外设立研发中心和产业园，传播中国文化，提升青岛声誉。如，软控在斯洛伐克设立的研发中心成为我国与中东欧国家合作的成功样板。南车青岛四方积极筹建与泰国等高铁联合研发中心，将成为我国高端成套装备出口的重要研发基地。<span style=\"color: black\">2013</span><span style=\"color: black\">年，我市企业与大学科研机构与国外共建7家联合研发中心。 </span></span></span></div>__eol__<div style=\"text-indent: 32pt\">__eol__	<span style=\"line-height: 20px; font-size: 20px\"><span style=\"font-family: 宋体\"><span style=\"color: black\">二是利用科技资源和渠道助推中小科技企业&ldquo;走出去&rdquo;开发新兴市场。</span><span style=\"color: black\">利用尼日利亚总统访华契机，积极向尼方政府代表团推介我市现代农业技术和先进制造业企业。引导我市科技型企业输出技术，拓展西非共同体市场，推动企业转型升级，拓宽&ldquo;走出去&rdquo;渠道</span><span style=\"color: black\">。</span></span></span></div>";
//		html += "<p style='TEXT-ALIGN: center' class=\"sdfasf\"><strong>蓝海技术加以</strong></p><p style='TEXT-ALIGN: center'><em>蓝海技术加以</em></p><p style='TEXT-ALIGN: center'><span style='TEXT-DECORATION: underline'>蓝海技术加以</span></p><p style='TEXT-ALIGN: center'>蓝海技术加以</p><p></p>";
//		html = html.replaceAll("__eol__", "");
//		System.out.println(replaceMobileHtml(html));
//	}
	
}
