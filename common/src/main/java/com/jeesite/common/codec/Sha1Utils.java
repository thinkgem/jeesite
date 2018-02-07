/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.codec;

import java.io.IOException;
import java.io.InputStream;

/**
 * SHA-1不可逆加密工具类
 * @author ThinkGem
 */
public class Sha1Utils {

	private static final String SHA1 = "SHA-1";

	/**
	 * 生成随机的Byte[]作为salt密钥.
	 * @param numBytes byte数组的大小
	 */
	public static byte[] genSalt(int numBytes) {
		return DigestUtils.genSalt(numBytes);
	}

	/**
	 * 对输入字符串进行sha1散列.
	 */
	public static byte[] sha1(byte[] input) {
		return DigestUtils.digest(input, SHA1, null, 1);
	}

	/**
	 * 对输入字符串进行sha1散列.
	 */
	public static byte[] sha1(byte[] input, byte[] salt) {
		return DigestUtils.digest(input, SHA1, salt, 1);
	}

	/**
	 * 对输入字符串进行sha1散列.
	 */
	public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return DigestUtils.digest(input, SHA1, salt, iterations);
	}

	/**
	 * 对文件进行sha1散列.
	 */
	public static byte[] sha1(InputStream input) throws IOException {
		return DigestUtils.digest(input, SHA1);
	}

}
