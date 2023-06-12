package com.jeesite.test.test;

import com.jeesite.common.text.PinyinUtils;

/**
 * 拼音工具测试类
 * @author ThinkGem
 */
public class PinyinUtilsTest {

	public static void main(String[] args) {
		String str = "你好，123，世界abc,~!#$_Sdf，女；ｈｅｌｌｏ！-";
		System.out.println(PinyinUtils.getFirstSpell(str));
		System.out.println(PinyinUtils.getFirstSpell(str, false));
		System.out.println(PinyinUtils.getFullSpell(str));
		System.out.println(PinyinUtils.getFullSpell(str, false));
	}

}
