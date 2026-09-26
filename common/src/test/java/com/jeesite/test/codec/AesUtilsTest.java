/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.AesUtils;
import com.jeesite.common.codec.EncodeUtils;

import javax.crypto.AEADBadTagException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * AES 加密解密工具类
 * @author ThinkGem
 * @version 2026-09-23
 */
public class AesUtilsTest {

	private static final String S = "Hello word! 你好，中文！";
	private static final byte[] DEFAULT_KEY = EncodeUtils.decodeHex("9f58a20946b47e190003ec716c1c457d");

	public static void main(String[] args) throws Exception {

		String s = S;
		System.out.println("原文：" + s);

		String k = AesUtils.genKeyString();
		System.out.println("秘钥：" + k);
		String s1 = AesUtils.encode(s, k);
		System.out.println("加密数据：" + s1);
		String s2 = AesUtils.decode(s1, k);
		System.out.println("解密数据：" + s2);

		byte[] key = AesUtils.genKey();
		byte[] iv = AesUtils.genIV();
		byte[] data = AesUtils.encode(s.getBytes(StandardCharsets.UTF_8), key, iv);
		System.out.println("加密byte数据：" + EncodeUtils.encodeHex(data));
		byte[] data2 = AesUtils.decode(data, key, iv);
		System.out.println("解密byte数据：" + new String(data2, StandardCharsets.UTF_8));

		// 一、新版 GCM 增强加密：随机初始向量、密文防篡改
		String gcm1 = AesUtils.encode(s);
		equals(s, AesUtils.decode(gcm1), "新版 GCM 默认秘钥解密");
		equals(s, AesUtils.decode(AesUtils.encode(s, k), k), "新版 GCM 指定秘钥解密");
		equals(s, new String(AesUtils.decode(AesUtils.encode(
				s.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY), DEFAULT_KEY), StandardCharsets.UTF_8),
				"新版 GCM 字节数组解密");
		check(!gcm1.equals(AesUtils.encode(s)), "新版 GCM 每次加密初始向量随机");
		check(AesUtils.encode(s).length() > AesUtils.encodeLegacy(s).length(), "新版 GCM 密文长度大于旧版 CBC");

		byte[] gcmData = AesUtils.encode(s.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY);
		check(gcmData[0] == 0x01, "新版 GCM 数据首字节为版本号");
		byte[] tampered = gcmData.clone();
		tampered[tampered.length - 1] ^= 0x01;
		checkIsGcm(() -> AesUtils.decode(tampered, DEFAULT_KEY), "新版 GCM 密文被篡改时解密失败");

		// 二、兼容旧版解密：CBC 有初始向量（密文的前 16 字节为初始向量）
		String legacyCbcHex = AesUtils.encodeLegacy(s);
		System.out.println("旧版(CBC 有IV)加密数据：" + legacyCbcHex);
		equals(s, AesUtils.decode(legacyCbcHex), "旧版 CBC 有IV：默认秘钥解密");
		String legacyCbcCustom = AesUtils.encodeLegacy(s, k);
		System.out.println("旧版(CBC 有IV)指定秘钥加密数据：" + legacyCbcCustom);
		equals(s, AesUtils.decode(legacyCbcCustom, k), "旧版 CBC 有IV：指定秘钥解密");

		// 三、兼容旧版解密：CBC 无初始向量（全 0 初始向量、密钥作为初始向量）
		String legacyZeroIv = AesUtils.encodeLegacy(s, new byte[16]);
		System.out.println("旧版(CBC 无IV-全0)加密数据：" + legacyZeroIv);
		equals(s, AesUtils.decode(legacyZeroIv), "旧版 CBC 无IV：零初始向量解密");
		String legacyKeyIv = AesUtils.encodeLegacy(s, Arrays.copyOf(DEFAULT_KEY, 16));
		System.out.println("旧版(CBC 无IV-秘钥)加密数据：" + legacyKeyIv);
		equals(s, AesUtils.decode(legacyKeyIv), "旧版 CBC 无IV：密钥作为初始向量解密");

		// 四、旧版密文不能被误判为新版（GCM）解密
		// 初始向量以版本号 0x01 开头，先按新格式尝试，认证标签校验失败后仍按旧版 CBC 正确解密
		byte[] versionIv = new byte[] {0x01, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		byte[] versionIvData = AesUtils.encodeLegacy(s.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY, versionIv);
		byte[] versionIvLegacy = new byte[versionIv.length + versionIvData.length];
		System.arraycopy(versionIv, 0, versionIvLegacy, 0, versionIv.length);
		System.arraycopy(versionIvData, 0, versionIvLegacy, versionIv.length, versionIvData.length);
		System.out.println("旧版(CBC 有IV-版本号开头)加密数据：" + EncodeUtils.encodeHex(versionIvLegacy));
		equals(s, new String(AesUtils.decode(versionIvLegacy, DEFAULT_KEY), StandardCharsets.UTF_8),
				"旧版 CBC 初始向量以版本号开头解密");
		checkNotGcm(() -> AesUtils.decode("e34574805a857298eeb14b4a2c30fe9d"), "旧版短密文不按 GCM 格式解密");

		// 五、未加密的明文，解密时抛出异常（Global.getPropertyDecodeAndEncode 依赖该异常判断是否已加密）
		checkFail(() -> AesUtils.decode("Abcd1234!@#$"), "未加密的明文解密失败");

		System.out.println("全部验证通过 ^_^");
	}

	private static void equals(String expected, String actual, String name) {
		check(expected.equals(actual), name + "：" + actual);
	}

	private static void check(boolean result, String name) {
		if (!result) {
			throw new RuntimeException("验证失败：" + name);
		}
		System.out.println("[OK] " + name);
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

	/**
	 * 解密失败时，异常必须是 GCM 认证标签校验失败（即确实按新版格式解密，未被降级为旧版格式）
	 */
	private static void checkIsGcm(Runnable runnable, String name) {
		try {
			runnable.run();
		} catch (Exception e) {
			for (Throwable t = e; t != null; t = t.getCause()) {
				if (t instanceof AEADBadTagException) {
					System.out.println("[OK] " + name + "：" + e.getClass().getSimpleName());
					return;
				}
			}
			throw new RuntimeException("验证失败：" + name + "：异常不是 GCM 认证标签校验失败");
		}
		throw new RuntimeException("验证失败：" + name + "：未按 GCM 格式解密");
	}

	/**
	 * 解密失败时，异常必须来自旧版解密，不能是 GCM 认证标签校验失败（即不能被误判为新版格式）
	 */
	private static void checkNotGcm(Runnable runnable, String name) {
		try {
			runnable.run();
		} catch (Exception e) {
			for (Throwable t = e; t != null; t = t.getCause()) {
				if (t instanceof AEADBadTagException) {
					throw new RuntimeException("验证失败：" + name + "：被误判为 GCM 格式");
				}
			}
			System.out.println("[OK] " + name + "：" + e.getClass().getSimpleName());
			return;
		}
		throw new RuntimeException("验证失败：" + name + "：被误判为 GCM 格式");
	}

}
