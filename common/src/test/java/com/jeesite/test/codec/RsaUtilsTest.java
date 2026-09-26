/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.RsaUtils;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA 加密解密工具类，非对称加密
 * @author ThinkGem
 * @version 2026-09-23
 */
public class RsaUtilsTest {

	private static final String S = "Hello word! 你好，中文！";

	public static void main(String[] args) {

		String s = S;
		System.out.println("原文：" + s);

		String[] keys = RsaUtils.genKeys();
		System.out.println("公钥：" + keys[0]);
		PublicKey publicKey = RsaUtils.toPublicKey(keys[0]);
		System.out.println("私钥：" + keys[1]);
		PrivateKey privateKey = RsaUtils.toPrivateKey(keys[1]);

		// 一、新版 OAEP 填充方式加密解密
		byte[] data = RsaUtils.encode(s.getBytes(StandardCharsets.UTF_8), publicKey);
		System.out.println("新版(OAEP)加密数据：" + EncodeUtils.encodeBase64(data));
		byte[] data2 = RsaUtils.decode(data, privateKey);
		String dataString2 = new String(data2, StandardCharsets.UTF_8);
		System.out.println("新版(OAEP)解密数据：" + dataString2);
		equals(s, dataString2, "新版 OAEP 字节数组解密");
		equals(s, RsaUtils.decode(RsaUtils.encode(s, publicKey), privateKey), "新版 OAEP 字符串解密");

		// 二、兼容旧版解密：PKCS#1 v1.5 填充方式加密的历史数据
		byte[] legacyData = RsaUtils.encodeLegacy(s.getBytes(StandardCharsets.UTF_8), publicKey);
		System.out.println("旧版(PKCS#1)加密数据：" + EncodeUtils.encodeBase64(legacyData));
		equals(s, new String(RsaUtils.decode(legacyData, privateKey), StandardCharsets.UTF_8),
				"旧版 PKCS#1 字节数组解密");
		String legacyDataString = RsaUtils.encodeLegacy(s, publicKey);
		System.out.println("旧版(PKCS#1)字符串加密数据：" + legacyDataString);
		equals(s, RsaUtils.decode(legacyDataString, privateKey), "旧版 PKCS#1 字符串解密");

		// 三、数据签名与验签
		byte[] sign = RsaUtils.sign(s.getBytes(StandardCharsets.UTF_8), privateKey);
		System.out.println("数据签名：" + EncodeUtils.encodeBase64(sign));
		check(RsaUtils.verify(s.getBytes(StandardCharsets.UTF_8), publicKey, sign), "数据验签通过");
		check(!RsaUtils.verify((s + "1").getBytes(StandardCharsets.UTF_8), publicKey, sign), "篡改数据验签失败");

		// 四、非法密文（长度与密钥模长不符）直接报错，不再尝试旧版填充方式
		checkFail(() -> RsaUtils.decode(new byte[32], privateKey), "非法长度密文解密失败");

		System.out.println("全部验证通过 ^_^");
	}

	private static void equals(String expected, String actual, String name) {
		check(expected.equals(actual), name + "：" + actual);
	}

	private static void checkFail(Runnable runnable, String name) {
		try {
			runnable.run();
		} catch (Exception e) {
			System.out.println("[OK] " + name + "：" + e.getClass().getSimpleName());
			return;
		}
		throw new RuntimeException("验证失败：" + name);
	}

	private static void check(boolean result, String name) {
		if (!result) {
			throw new RuntimeException("验证失败：" + name);
		}
		System.out.println("[OK] " + name);
	}

}
