/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.codec;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.jeesite.common.lang.ExceptionUtils;

/**
 * AES加密解密工具类
 * @author ThinkGem
 */
public class AesUtils {
	
	private static final String AES = "AES";
	private static final String AES_CBC = "AES/CBC/PKCS5Padding";
	private static final int DEFAULT_AES_KEYSIZE = 128; 			// 生成AES密钥, 默认长度为128位(16字节).
	private static final int DEFAULT_IVSIZE = 16; 					// 生成随机向量, 默认大小为cipher.getBlockSize(), 16字节
	private static final SecureRandom RANDOM = new SecureRandom();	// 用于 生成 generateIV随机数对象

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final byte[] DEFAULT_KEY = new byte[]{-97,88,-94,9,70,-76,126,25,0,3,-20,113,108,28,69,125}; 

	/**
	 * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
	 */
	public static String genKeyString() {
		return EncodeUtils.encodeHex(genKey(DEFAULT_AES_KEYSIZE));
	}
	
	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input 原始输入字符数组
	 */
	public static String encode(String input) {
		try {
			return EncodeUtils.encodeHex(encode(input.getBytes(DEFAULT_URL_ENCODING), DEFAULT_KEY));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input 原始输入字符数组
	 * @param key 符合AES要求的密钥
	 */
	public static String encode(String input, String key) {
		try {
			return EncodeUtils.encodeHex(encode(input.getBytes(DEFAULT_URL_ENCODING), EncodeUtils.decodeHex(key)));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 */
	public static String decode(String input) {
		try {
			return new String(decode(EncodeUtils.decodeHex(input), DEFAULT_KEY), DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param key 符合AES要求的密钥
	 */
	public static String decode(String input, String key) {
		try {
			return new String(decode(EncodeUtils.decodeHex(input), EncodeUtils.decodeHex(key)), DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
	 */
	public static byte[] genKey() {
		return genKey(DEFAULT_AES_KEYSIZE);
	}

	/**
	 * 生成AES密钥,可选长度为128,192,256位.
	 */
	public static byte[] genKey(int keysize) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			keyGenerator.init(keysize);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	/**
	 * 生成随机向量,默认大小为cipher.getBlockSize(), 16字节.
	 */
	public static byte[] genIV() {
		byte[] bytes = new byte[DEFAULT_IVSIZE];
		RANDOM.nextBytes(bytes);
		return bytes;
	}
	
	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input 原始输入字符数组
	 * @param key 符合AES要求的密钥
	 */
	public static byte[] encode(byte[] input, byte[] key) {
		return aes(input, key, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input 原始输入字符数组
	 * @param key 符合AES要求的密钥
	 * @param iv 初始向量
	 */
	public static byte[] encode(byte[] input, byte[] key, byte[] iv) {
		return aes(input, key, iv, Cipher.ENCRYPT_MODE);
	}
	
	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param key 符合AES要求的密钥
	 */
	public static byte[] decode(byte[] input, byte[] key) {
		return aes(input, key, Cipher.DECRYPT_MODE);
	}

	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param key 符合AES要求的密钥
	 * @param iv 初始向量
	 */
	public static byte[] decode(byte[] input, byte[] key, byte[] iv) {
		return aes(input, key, iv, Cipher.DECRYPT_MODE);
	}

	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] aes(byte[] input, byte[] key, int mode) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @param iv 初始向量
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, AES);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance(AES_CBC);
			cipher.init(mode, secretKey, ivSpec);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
//	public static void main(String[] args) {
//
//		String s = "hello word!";
//		System.out.println(s);
//		
//		String k = genKeyString();
//		System.out.println(k);
//		String ss = encode(s, k);
//		System.out.println(ss);
//		String sss = decode(ss, k);
//		System.out.println(sss);
//		
//	}
	
}