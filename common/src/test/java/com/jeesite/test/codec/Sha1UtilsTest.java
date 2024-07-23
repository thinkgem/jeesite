/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.Sha1Utils;

/**
 * SHA-1 加密工具类，散列加密，不可逆加密
 * @author ThinkGem
 * @version 2024-07-22
 */
public class Sha1UtilsTest {

	public static void main(String[] args) {

		String s = "Hello word! 你好，中文！";
		System.out.println(s);

		String salt = Sha1Utils.genSaltString(8);
		System.out.println(salt);
		String data = Sha1Utils.sha1(s, salt);
		System.out.println(data);

	}
	
}