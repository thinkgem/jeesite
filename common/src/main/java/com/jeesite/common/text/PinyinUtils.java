/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.text;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * 拼音工具类
 * @author ThinkGem
 */
public class PinyinUtils {

	private static final Logger logger = LoggerFactory.getLogger(PinyinUtils.class);

	private static class Static{
		private static final Pattern idPat = Pattern.compile("\\W");
		private static final HanyuPinyinOutputFormat defaultFormat;
		static{
			defaultFormat = new HanyuPinyinOutputFormat();
			defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		}
	}

	/**
	 * 获取汉字串拼音首字母，替换调非法标示符字符，英文字符不变，去除空格
	 * @param chinese 汉字串
	 * @return 汉语拼音首字母
	 */
	public static String getFirstSpell(String chinese) {
		return getFirstSpell(chinese, true);
	}

	/**
	 * 获取汉字串拼音首字母，替换调非法标示符字符，英文字符不变，去除空格
	 * @param chinese 汉字串
	 * @param isId 是否标示符（true：将去掉特殊字符）
	 * @return 汉语拼音首字母
	 */
	public static String getFirstSpell(String chinese, boolean isId) {
		chinese = getDbc(chinese);
		if (chinese == null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		char[] arr = chinese.toCharArray();
		for (char c : arr) {
			if (c > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, Static.defaultFormat);
					if (temp != null && temp.length > 0) {
						sb.append(temp[0].charAt(0));
					} else {
						sb.append(String.valueOf(c));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				sb.append(c);
			}
		}
		if (isId){
			return Static.idPat.matcher(sb.toString()).replaceAll("").trim();
		}
		return sb.toString();
	}

	/**
	 * 获取汉字串全拼，英文字符不变
	 * @param chinese 汉字串
	 * @return 汉语拼音
	 */
	public static String getFullSpell(String chinese) {
		return getFullSpell(chinese, true);
	}

	/**
	 * 获取汉字串全拼，英文字符不变
	 * @param chinese 汉字串
	 * @param isId 是否标示符（true：将去掉特殊字符）
	 * @return 汉语拼音
	 */
	public static String getFullSpell(String chinese, boolean isId) {
		chinese = getDbc(chinese);
		if (chinese == null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		char[] arr = chinese.toCharArray();
		for (char c : arr) {
			if (c > 128) {
				try {
					String[] ss = PinyinHelper.toHanyuPinyinStringArray(c, Static.defaultFormat);
					if (ss != null && ss.length > 0) {
						sb.append(ss[0]);
					} else {
						sb.append(String.valueOf(c));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				sb.append(c);
			}
		}
		if (isId){
			return Static.idPat.matcher(sb.toString()).replaceAll("").trim();
		}
		return sb.toString();
	}
	
	/**
	 * 半角转全角
	 * @param input String.
	 * @return 全角字符串.
	 */
	public static String getSbc(String input) {
		if (input == null){
			return null;
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * @param input String.
	 * @return 半角字符串
	 */
	public static String getDbc(String input) {
		if (input == null){
			return null;
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

}
