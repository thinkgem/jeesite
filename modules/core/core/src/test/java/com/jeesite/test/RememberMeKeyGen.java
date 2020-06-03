/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.apache.shiro.crypto.AesCipherService;

/**
 * v4.1.8 开始将不为记住我功能，设置默认密钥，即启动系统时生成新密钥。
 * 这样会造成一个问题，比如：重启服务后，记住登录的用户因为解密失败，而需要重新登录。
 * 为了解决这个问题，您可以通过这个类获取一个新密钥，设置到 shiro.rememberMe.secretKey 中即可。
 * 另外，如果你从配置文件里将 shiro.rememberMe.secretKey 设置为空，启动系统时也会自动设置一个新的密钥。
 * @author ThinkGem
 * @version 2019年11月6日
 */
public class RememberMeKeyGen {

	public static void main(String[] args) {
		byte[] cipherKey = new AesCipherService().generateNewKey().getEncoded();
		String secretKey = org.apache.shiro.codec.Base64.encodeToString(cipherKey);
		System.out.println("shiro.rememberMe.secretKey = " + secretKey);
	}
	
}
