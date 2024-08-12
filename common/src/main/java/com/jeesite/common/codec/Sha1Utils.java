/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * SHA-1 加密工具类，散列加密，不可逆加密
 * @author ThinkGem
 */
public class Sha1Utils {

	/**
	 * 生成随机的 Byte[] 作为 salt 密钥.
	 * @param numBytes byte数组的大小
	 */
	public static byte[] genSalt(int numBytes) {
		return DigestUtils.genSalt(numBytes);
	}

	/**
	 * 生成随机的 Byte[] 作为 salt 密钥，返回 HEX 值
	 * @param numBytes byte 数组的大小
	 */
	public static String genSaltString(int numBytes) {
		return DigestUtils.genSaltString(numBytes);
	}

	/**
	 * 对输入字符串进行 SHA-1 散列.
	 */
	public static byte[] sha1(byte[] input) {
		return DigestUtils.digest(input, DigestUtils.SHA1, null, 1);
	}

	/**
	 * 对输入字符串进行 SHA-1 散列.
	 */
	public static String sha1(String input) {
		return EncodeUtils.encodeHex(sha1(input.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * 对输入字符串进行 SHA-1 散列.
	 */
	public static byte[] sha1(byte[] input, byte[] salt) {
		return DigestUtils.digest(input, DigestUtils.SHA1, salt, 1);
	}

	/**
	 * 对输入字符串进行 SHA-1 散列.
	 */
	public static String sha1(String data, String salt) {
		return EncodeUtils.encodeHex(sha1(data.getBytes(StandardCharsets.UTF_8), EncodeUtils.decodeHex(salt)));
	}

	/**
	 * 对输入字符串进行 SHA-1 散列.
	 */
	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return DigestUtils.digest(input, DigestUtils.SHA1, salt, iterations);
	}

	/**
	 * 对输入字符串进行 SHA-1 散列.
	 */
	public static String sha1(String input, String salt, int iterations) {
		return EncodeUtils.encodeHex(sha1(input.getBytes(StandardCharsets.UTF_8), EncodeUtils.decodeHex(salt), iterations));
	}

	/**
	 * 对文件进行 SHA-1 散列.
	 */
	public static byte[] sha1(InputStream input) throws IOException {
		return DigestUtils.digest(input, DigestUtils.SHA1);
	}

}
