/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.lang;

import com.jeesite.common.lang.ObjectUtils;

import java.text.ParseException;

/**
 * 对象操作工具测试类
 * @author ThinkGem
 * @version 2025-02-05
 */
public class ObjectUtilsTest {

	public static void main(String[] args) throws ParseException {
		String str = "1738746499603094500";
		System.out.println(ObjectUtils.toDouble(str));
		System.out.println(ObjectUtils.toFloat(str));
		System.out.println(ObjectUtils.toLong(str));
		System.out.println(ObjectUtils.toInteger(str));
	}
	
}
