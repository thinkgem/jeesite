/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.network;

import com.jeesite.common.network.MacUtils;

/**
 * MAC地址工具测试类
 * @author ThinkGem
 * @version 2023-6-9
 */
public class MacUtilsTest {

	public static void main(String[] argc) {
		String os = MacUtils.getOSName();
		System.out.println("os: " + os);
		if (os.startsWith("windows")) {
			String mac = MacUtils.getWindowsMACAddress();
			System.out.println("mac: " + mac);
		} else if (os.startsWith("linux")) {
			String mac = MacUtils.getLinuxMACAddress();
			System.out.println("mac: " + mac);
		} else {
			String mac = MacUtils.getUnixMACAddress();
			System.out.println("mac: " + mac);
		}
	}

}
