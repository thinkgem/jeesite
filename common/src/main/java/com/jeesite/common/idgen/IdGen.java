/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.idgen;

import java.security.SecureRandom;
import java.util.UUID;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.StringUtils;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author ThinkGem
 * @version 2014-8-19
 */
public class IdGen {

	private static SecureRandom random = new SecureRandom();
	private static IdWorker idWorker = new IdWorker(-1, -1);
	
	/**
	 * 生成UUID, 中间无-分割.
	 */
	public static String uuid() {
		return StringUtils.replace(UUID.randomUUID().toString(),"-", "");
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
	 * 使用SecureRandom随机生成指定范围的Integer. 
	 */
	public static int randomInt(int min, int max) {
		return random.nextInt(max) % (max - min + 1) + min;
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
			String nextNum = String.valueOf(Long.valueOf(prevNum) + 1);
			str = prefix + StringUtils.leftPad(nextNum, prevNum.length(), "0");
			return str;
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		System.out.println(nextCode("8") + " = 9");
//		System.out.println(nextCode("09") + " = 10");
//		System.out.println(nextCode("009") + " = 010");
//		System.out.println(nextCode("T09") + " = T10");
//		System.out.println(nextCode("TG09") + " = TG10");
//		System.out.println(nextCode("TG0101") + " = TG0102");
//		System.out.println(nextCode("TG0109") + " = TG0110");
//		System.out.println(nextCode("TG02T03") + " = TG02T04");
//		System.out.println(nextCode("TG02T099") + " = TG02T100");
//		System.out.println(nextCode("TG02T100") + " = TG02T101");
//		System.out.println(nextCode("TG02T10A") + " = TG02T10A001");
//		System.out.println(nextCode("1123117153417957377") + " = 1123117153417957379");
//		System.out.println(nextCode("0040009") + " = 0040010");
//		System.out.println(uuid());
//		System.out.println(nextId());
//		// 数值型ID重复验证测试
//		Set<String> set = SetUtils.newHashSet();
//		try{
//			for (int i=0; i<100; i++){
//				String id = String.valueOf(nextId());
//				if (set.contains(id)){
//					throw new Exception(id + " exists");
//				}
//				set.add(id);
//				System.out.println(id);
//				Thread.sleep(100);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
