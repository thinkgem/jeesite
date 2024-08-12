/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.SM2Utils;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 国密 SM2 加密解密工具类，非对称加密
 * @author ThinkGem
 * @version 2024-07-22
 */
public class SM2UtilsTest {

	public static void main(String[] args) {

		String s = "Hello word! 你好，中文！";
		System.out.println(s);

		String[] keys = SM2Utils.genKeys();
		System.out.println("公钥：" + keys[0]);
		PublicKey publicKey = SM2Utils.toPublicKey(keys[0]);
		System.out.println("私钥：" + keys[1]);
		PrivateKey privateKey = SM2Utils.toPrivateKey(keys[1]);

		byte[] data = SM2Utils.encode(s.getBytes(), publicKey);
		String dataString = EncodeUtils.encodeBase64(data);
		System.out.println("加密数据：" + dataString);

		byte[] data2 = SM2Utils.decode(data, privateKey);
		String dataString2 = new String(data2, StandardCharsets.UTF_8);
		System.out.println("解密数据：" + dataString2);

		byte[] sign = SM2Utils.sign(s.getBytes(), privateKey);
		System.out.println("数据签名：" + EncodeUtils.encodeBase64(sign));

		boolean b = SM2Utils.verify(s.getBytes(), publicKey, sign);
		System.out.println("数据验签：" + b);
	}

}