/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.ExceptionUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/**
 * AES 加密解密工具类
 * @author ThinkGem
 */
public class AesUtils {
	
	private static final String AES = "AES";
	private static final String AES_CBC = "AES/CBC/PKCS5Padding";
	private static final int DEFAULT_KEY_SIZE = 128; 			// 生成AES密钥, 默认长度为128位(16字节).
	private static final int DEFAULT_IV_SIZE = 16; 				// 生成随机向量, 默认大小为cipher.getBlockSize(), 16字节
	private static final SecureRandom RANDOM = new SecureRandom();	// 用于 生成 generateIV随机数对象

	private static final byte[] DEFAULT_KEY = EncodeUtils.decodeHex(PropertiesUtils.getInstance()
			.getProperty("encrypt.defaultKey", "9f58a20946b47e190003ec716c1c457d"));
	private static final boolean STORE_BASE64 = PropertiesUtils.getInstance()
			.getPropertyToBoolean("encrypt.storeBase64", "false");

	/**
	 * 生成 AES 密钥,返回字节数组, 默认长度为128位(16字节)
	 */
	public static byte[] genKey() {
		return genKey(DEFAULT_KEY_SIZE);
	}

	/**
	 * 生成 AES 密钥, 返回字节数组, 默认长度为128位(16字节)
	 */
	public static String genKeyString() {
		return EncodeUtils.encodeHex(genKey());
	}

	/**
	 * 生成 AES 密钥, 可选长度为128,192,256位
	 */
	public static byte[] genKey(int keySize) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			keyGenerator.init(keySize);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 生成随机向量, 默认大小为cipher.getBlockSize(), 16字节
	 */
	public static byte[] genIV() {
		byte[] bytes = new byte[DEFAULT_IV_SIZE];
		RANDOM.nextBytes(bytes);
		return bytes;
	}

	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符串
	 * @author ThinkGem
	 */
	public static String encode(String input) {
		if (STORE_BASE64) {
			return EncodeUtils.encodeBase64(encode(input.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY));
		}
		return EncodeUtils.encodeHex(encode(input.getBytes(StandardCharsets.UTF_8), DEFAULT_KEY));
	}
	
	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符数组
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static byte[] encode(byte[] input, byte[] key) {
		return aes(input, key, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符串
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static String encode(String input, String key) {
		if (STORE_BASE64) {
			return EncodeUtils.encodeBase64(encode(input.getBytes(StandardCharsets.UTF_8), EncodeUtils.decodeHex(key)));
		}
		return EncodeUtils.encodeHex(encode(input.getBytes(StandardCharsets.UTF_8), EncodeUtils.decodeHex(key)));
	}

	/**
	 * 使用 AES 加密原始字符串
	 * @param input 原始输入字符数组
	 * @param key 符合要求的密钥
	 * @param iv 初始向量
	 * @author ThinkGem
	 */
	public static byte[] encode(byte[] input, byte[] key, byte[] iv) {
		return aes(input, key, iv, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * @param input Hex 或 Base64 编码的加密字符串
	 * @author ThinkGem
	 */
	public static String decode(String input) {
		if (STORE_BASE64) {
			return new String(decode(EncodeUtils.decodeBase64(input), DEFAULT_KEY), StandardCharsets.UTF_8);
		}
		return new String(decode(EncodeUtils.decodeHex(input), DEFAULT_KEY), StandardCharsets.UTF_8);
	}
	
	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * @param input 加密输入字符数组
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static byte[] decode(byte[] input, byte[] key) {
		return aes(input, key, Cipher.DECRYPT_MODE);
	}

	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * @param input Hex 或 Base64 编码的加密字符串
	 * @param key 符合要求的密钥
	 * @author ThinkGem
	 */
	public static String decode(String input, String key) {
		if (STORE_BASE64) {
			return new String(decode(EncodeUtils.decodeBase64(input), EncodeUtils.decodeHex(key)), StandardCharsets.UTF_8);
		}
		return new String(decode(EncodeUtils.decodeHex(input), EncodeUtils.decodeHex(key)), StandardCharsets.UTF_8);
	}

	/**
	 * 使用 AES 解密数据, 返回原始字符串
	 * @param input Hex编码的加密字符串
	 * @param key 符合AES要求的密钥
	 * @param iv 初始向量
	 * @author ThinkGem
	 */
	public static byte[] decode(byte[] input, byte[] key, byte[] iv) {
		return aes(input, key, iv, Cipher.DECRYPT_MODE);
	}

	/**
	 * 使用 AES 加密或解密无编码的原始字节数组, 返回无编码的字节数组结果
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 * @author ThinkGem
	 */
	private static byte[] aes(byte[] input, byte[] key, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(AES);
			SecretKey secretKey = new SecretKeySpec(key, AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 使用 AES 加密或解密无编码的原始字节数组, 返回无编码的字节数组结果
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @param iv 初始向量
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 * @author ThinkGem
	 */
	private static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(AES_CBC);
			SecretKey secretKey = new SecretKeySpec(key, AES);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(mode, secretKey, ivSpec);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
}