/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.idgen;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.StringUtils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author ThinkGem
 * @version 2014-8-19
 */
public class IdGen {

	private static final SecureRandom random = new SecureRandom();
	private static final IdWorker idWorker = new IdWorker(-1, -1);
	
	/**
	 * 生成UUID, 中间无-分割.
	 */
	public static String uuid() {
		return StringUtils.replace(UUID.randomUUID().toString(),"-", "");
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return EncodeUtils.encodeBase62(randomBytes);
	}

	/**
	 * 随机小写字符串
	 */
	public static String randomString(int length) {
		return randomBase62(length).toLowerCase();
	}

	/**
	 * 随机简短小写字符串（对重复频率要求不高的使用）
	 */
	public static String randomShortString() {
		return randomBase62(4).toLowerCase();
	}

	/**
	 * 使用SecureRandom随机生成指定范围的Integer. 
	 */
	public static int randomInt(int min, int max) {
		return random.nextInt(max) % (max - min + 1) + min;
	}

	/**
	 * 使用SecureRandom随机生成Long.
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}
	
	/**
	 * 获取新唯一编号（18为数值）
	 * 来自于twitter项目snowflake的id产生方案，全局唯一，时间有序。
	 * 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
	 */
	public static String nextId() {
		return String.valueOf(idWorker.nextId());
	}
	
	/**
	 * 获取新代码编号
	 */
	public static String nextCode(String code){
		if (code != null){
			String str = code.trim();
			int lastNotNumIndex = -1;
			int len = str.length() - 1;
			for (int i = len; i >= 0; i--) {
				if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
					lastNotNumIndex = i;
				}else{
					break;
				}
			}
			String prefix = str;
			String prevNum = "000";
			if (lastNotNumIndex != -1){
				prefix = str.substring(0, lastNotNumIndex);
				prevNum = str.substring(lastNotNumIndex, str.length());
			}
			String nextNum = new BigDecimal(prevNum).add(BigDecimal.ONE).toString();
			str = prefix + StringUtils.leftPad(nextNum, prevNum.length(), "0");
			return str;
		}
		return null;
	}

}
