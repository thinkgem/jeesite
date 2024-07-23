/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.codec;

import com.jeesite.common.lang.ExceptionUtils;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;

/**
 * 不可逆加密工具类
 * @author ThinkGem
 */
public class DigestUtils {

	public static final String SHA1 = "SHA-1";
	public static final String MD5 = "MD5";
	public static final String SM3 = "SM3";

	private static final SecureRandom random = new SecureRandom();

	/**
	 * 生成随机的 Byte[] 作为 salt 密钥.
	 * @param numBytes byte 数组的大小
	 */
	public static byte[] genSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}

	/**
	 * 生成随机的 Byte[] 作为 salt 密钥，返回 HEX 值
	 * @param numBytes byte 数组的大小
	 */
	public static String genSaltString(int numBytes) {
		return EncodeUtils.encodeHex(genSalt(numBytes));
	}

	/**
	 * 获取 MessageDigest
	 */
	private static MessageDigest getMessageDigest(String algorithm) throws GeneralSecurityException {
		if (SM3.equals(algorithm)) {
			return MessageDigest.getInstance(algorithm, BouncyCastleProvider.PROVIDER_NAME);
		} else {
			return MessageDigest.getInstance(algorithm);
		}
	}
	
	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 * @param input 需要散列的字符串
	 * @param algorithm 散列算法（"SHA-1"、"MD5"、"SM3"）
	 * @param salt 可为空
	 * @param iterations 迭代次数
	 * @return
	 */
	public static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = getMessageDigest(algorithm);
			if (salt != null) {
				digest.update(salt);
			}
			byte[] result = digest.digest(input);
			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * 对文件进行sha1散列.
	 * @param input 需要散列的流
	 * @param algorithm 散列算法（"SHA-1"、"MD5"、"SM3"）
	 */
	public static byte[] digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = getMessageDigest(algorithm);
			int bufferLength = 8 * 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);
			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}
			return messageDigest.digest();
		} catch (GeneralSecurityException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
}
