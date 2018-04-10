/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.idgen;

import java.security.SecureRandom;
import java.util.UUID;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author ThinkGem
 * @version 2014-8-19
 */
public class IdGenerate {

	private static SecureRandom random = new SecureRandom();
	private static IdWorker idWorker = new IdWorker(-1, -1);
	
	/**
	 * 生成UUID, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
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
			int len = str.length() - 1;
			int lastNotNumIndex = 0;
			for (int i = len; i >= 0; i--) {
				if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
					lastNotNumIndex = i;
					break;
				}
			}
			// 如果最后一位是数字，并且last索引位置还在最后，则代表是纯数字，则最后一个不是数字的索引为-1
			if ((str.charAt(len) >= '0' && str.charAt(len) <= '9') && (lastNotNumIndex == len)) {
				lastNotNumIndex = -1;
			}
			String prefix = str.substring(0, lastNotNumIndex + 1);
			String numStr = str.substring(lastNotNumIndex + 1, str.length());
			long num = ObjectUtils.toLong(numStr);
//			System.out.println("处理前："+str);
			str = prefix + StringUtils.leftPad(String.valueOf(num + 1), numStr.length(), "0");
//			System.out.println("处理后："+str);
			return str;
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		System.out.println(uuid());
//		System.out.println(nextId());
//		System.out.println(nextCode("8"));
//		System.out.println(nextCode("09"));
//		System.out.println(nextCode("009"));
//		System.out.println(nextCode("E09"));
//		System.out.println(nextCode("EC09"));
//		System.out.println(nextCode("EC0101"));
//		System.out.println(nextCode("EC0109"));
//		System.out.println(nextCode("EC02T03"));
//		System.out.println(nextCode("EC02T099"));
//		System.out.println(nextCode("EC02T100"));
//		System.out.println(nextCode("EC02T10A"));
////		// 数值型ID重复验证测试
////		Set<String> set = SetUtils.newHashSet();
////		try{
////			for (int i=0; i<100; i++){
////				String id = String.valueOf(nextId());
////				if (set.contains(id)){
////					throw new Exception(id + " exists");
////				}
////				set.add(id);
////				System.out.println(id);
////				Thread.sleep(100);
////			}
////		}catch (Exception e) {
////			e.printStackTrace();
////		}
//	}

}
