/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.SM4Utils;

import java.nio.charset.StandardCharsets;

/**
 * 国密 SM4 加密解密工具类，对称加密
 * @author ThinkGem
 * @version 2024-07-22
 */
public class SM4UtilsTest {

	public static void main(String[] args) {

		String s = "Hello word! 你好，中文！";
		System.out.println(s);

		String k = SM4Utils.genKeyString();
		System.out.println(k);
		String s1 = SM4Utils.encode(s, k);
		System.out.println(s1);
		String s2 = SM4Utils.decode(s1, k);
		System.out.println(s2);

		byte[] key = SM4Utils.genKey();
		byte[] iv = SM4Utils.genIV();
		byte[] data = SM4Utils.encode(s.getBytes(StandardCharsets.UTF_8), key, iv);
		System.out.println(EncodeUtils.encodeBase64(data));
		byte[] data2 = SM4Utils.decode(data, key, iv);
		System.out.println(new String(data2, StandardCharsets.UTF_8));
	}

}