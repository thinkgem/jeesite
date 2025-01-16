/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
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
 */
public class RsaUtilsTest {

	public static void main(String[] args) {

		String s = "Hello word! 你好，中文！";
		System.out.println(s);

		String[] keys = RsaUtils.genKeys();
		System.out.println("公钥：" + keys[0]);
		PublicKey publicKey = RsaUtils.toPublicKey(keys[0]);
		System.out.println("私钥：" + keys[1]);
		PrivateKey privateKey = RsaUtils.toPrivateKey(keys[1]);

		byte[] data = RsaUtils.encode(s.getBytes(), publicKey);
		String dataString = EncodeUtils.encodeBase64(data);
		System.out.println("加密数据：" + dataString);

		byte[] data2 = RsaUtils.decode(data, privateKey);
		String dataString2 = new String(data2, StandardCharsets.UTF_8);
		System.out.println("解密数据：" + dataString2);

		byte[] sign = RsaUtils.sign(s.getBytes(), privateKey);
		System.out.println("数据签名：" + EncodeUtils.encodeBase64(sign));

		boolean b = RsaUtils.verify(s.getBytes(), publicKey, sign);
		System.out.println("数据验签：" + b);
	}

}