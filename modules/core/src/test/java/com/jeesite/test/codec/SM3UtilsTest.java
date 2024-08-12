/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.SM3Utils;

/**
 * 国密 SM3 加密工具类，散列加密，不可逆加密
 * @author ThinkGem
 * @version 2024-07-22
 */
public class SM3UtilsTest {

	public static void main(String[] args) {

		String s = "Hello word! 你好，中文！";
		System.out.println(s);

		String s1 = SM3Utils.sm3(s);
        System.out.println(s1);

		String key = SM3Utils.genSaltString(8);
        System.out.println(key);

        String s3 = SM3Utils.sm3(s, key);
        System.out.println(s3);

        String s4 = SM3Utils.hmacSm3(s, key);
        System.out.println(s4);
	}

}