/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.lang;

import com.jeesite.common.lang.StringUtils;

import java.text.ParseException;

/**
 * 字符串工具测试类
 * @author ThinkGem
 * @version 2025-09-18
 */
public class StringUtilsTest {

	public static void main(String[] args) throws ParseException {
		System.out.println(StringUtils.camelCase("id_name") + " = idName");
		System.out.println(StringUtils.camelCase("_id_name") + " = idName");
		System.out.println(StringUtils.camelCase("__id_name") + " = idName");
		System.out.println(StringUtils.camelCase("a_id") + " = aid");
		System.out.println(StringUtils.camelCase("a_b_id") + " = abId");
		System.out.println(StringUtils.camelCase("__a_id") + " = aid");
		System.out.println(StringUtils.camelCase("__a_b_id") + " = abId");
		System.out.println(StringUtils.capCamelCase("id_name") + " = IdName");
		System.out.println(StringUtils.capCamelCase("a_b_id_name") + " = AbIdName");
		System.out.println(StringUtils.uncamelCase("abIdName") + " = ab_id_name");
		System.out.println(StringUtils.uncamelCase("AbIdName") + " = ab_id_name");
	}
	
}
