package com.jeesite.common.text;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 * @author ThinkGem
 */
public class PinyinUtils {
	
//	/**
//	 * 将字符串中的中文转化为拼音，其他字符不变
//	 * @param inputString
//	 * @return
//	 */
//	public static String getPinyin(String inputString) {
//		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		format.setVCharType(HanyuPinyinVCharType.WITH_V);
//
//		char[] input = inputString.trim().toCharArray();
//		String output = "";
//
//		try {
//			for (int i = 0; i < input.length; i++) {
//				if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
//					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
//					output += temp[0];
//				} else {
//					output += java.lang.Character.toString(input[i]);
//				}
//			}
//		} catch (BadHanyuPinyinOutputFormatCombination e) {
//			e.printStackTrace();
//		}
//		return output;
//	}

	/**
	 * 获取汉字串拼音首字母，替换调非法标示符字符，英文字符不变，去除空格
	 * @param chinese 汉字串
	 * @return 汉语拼音首字母
	 */
	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * 获取汉字串全拼，英文字符不变
	 * @param chinese 汉字串
	 * @return 汉语拼音
	 */
	public static String getFullSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] ss = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (ss != null && ss.length > 0){
						pybf.append(ss[0]);
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}
	
//	public static void main(String[] args) {
//		String str = "你好，123，世界abc,~!#$_Sdf";
////		System.out.println(getPinyin(str));
//		System.out.println(getFirstSpell(str));
//		System.out.println(getFullSpell(str));
//	}
}
