/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.AesUtils;
import com.jeesite.common.codec.EncodeUtils;

import java.nio.charset.StandardCharsets;

/**
 * AES 加密解密工具类
 * @author ThinkGem
 * @version 2024-07-22
 */
public class AesUtilsTest {

	public static void main(String[] args) {

		String s = "Hello word! 你好，中文！";
		System.out.println(s);

		String k = AesUtils.genKeyString();
		System.out.println(k);
		String s1 = AesUtils.encode(s, k);
		System.out.println(s1);
		String s2 = AesUtils.decode(s1, k);
		System.out.println(s2);

		byte[] key = AesUtils.genKey();
		byte[] iv = AesUtils.genIV();
		byte[] data = AesUtils.encode(s.getBytes(StandardCharsets.UTF_8), key, iv);
		System.out.println(EncodeUtils.encodeHex(data));
		byte[] data2 = AesUtils.decode(data, key, iv);
		System.out.println(new String(data2, StandardCharsets.UTF_8));
	}
	
}