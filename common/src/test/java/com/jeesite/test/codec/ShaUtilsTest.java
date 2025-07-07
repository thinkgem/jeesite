/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.ShaUtils;

/**
 * SHA-1 加密工具类，散列加密，不可逆加密
 * @author ThinkGem
 * @version 2024-07-22
 */
public class ShaUtilsTest {

	public static final int HASH_ITERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	public static void main(String[] args) {

		String s = "Hello word! 你好，中文！";
		System.out.println(s);

		String salt = ShaUtils.genSaltString(SALT_SIZE);
		System.out.println(salt);
		String data = ShaUtils.sha1(s, salt, HASH_ITERATIONS);
		System.out.println(data);

		String salt2 = ShaUtils.genSaltString(SALT_SIZE);
		System.out.println(salt2);
		String data2 = ShaUtils.sha256(s, salt2, HASH_ITERATIONS);
		System.out.println(data2);

	}
	
}