/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * MAC地址工具
 * 
 * @author ThinkGem
 * @version 2014-6-18
 */
public class MacUtils {

	/**
	 * 获取当前操作系统名称. return 操作系统名称 例如:windows,Linux,Unix等.
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	/**
	 * 获取Unix网卡的mac地址.
	 * 
	 * @return mac地址
	 */
	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			/**
			 * Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
			 */
			process = Runtime.getRuntime().exec("ifconfig eth0");
			bufferedReader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				/**
				 * 寻找标示字符串[hwaddr]
				 */
				index = line.toLowerCase().indexOf("hwaddr");
				/**
				 * 找到了
				 */
				if (index != -1) {
					/**
					 * 取出mac地址并去除2边空格
					 */
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * 获取Linux网卡的mac地址.
	 * 
	 * @return mac地址
	 */
	public static String getLinuxMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			/**
			 * linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
			 */
			process = Runtime.getRuntime().exec("ifconfig eth0");
			bufferedReader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("硬件地址");
				/**
				 * 找到了
				 */
				if (index != -1) {
					/**
					 * 取出mac地址并去除2边空格
					 */
					mac = line.substring(index + 4).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}
		
		// 取不到，试下Unix取发
		if (mac == null){
			return getUnixMACAddress();
		}

		return mac;
	}

	/**
	 * 获取widnows网卡的mac地址.
	 * 
	 * @return mac地址
	 */
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			/**
			 * windows下的命令，显示信息中包含有mac地址信息
			 */
			process = Runtime.getRuntime().exec("ipconfig /all");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				/**
				 * 寻找标示字符串[physical address 或  物理地址]
				 */
				if (line.split("-").length == 6){
					index = line.indexOf(":");
					if (index != -1) {
						/**
						 * 取出mac地址并去除2边空格
						 */
						mac = line.substring(index + 1).trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	public static String getMac(){
		String os = getOSName();
		String mac;
		if (os.startsWith("windows")) {
			mac = getWindowsMACAddress();
		} else if (os.startsWith("linux")) {
			mac = getLinuxMACAddress();
		} else {
			mac = getUnixMACAddress();
		}
		return mac == null ? "" : mac;
	}
	
	/**
	 * 测试用的main方法.
	 * 
	 * @param argc 运行参数.
	 */
	public static void main(String[] argc) {
		String os = getOSName();
		System.out.println("os: " + os);
		if (os.startsWith("windows")) {
			String mac = getWindowsMACAddress();
			System.out.println("mac: " + mac);
		} else if (os.startsWith("linux")) {
			String mac = getLinuxMACAddress();
			System.out.println("mac: " + mac);
		} else {
			String mac = getUnixMACAddress();
			System.out.println("mac: " + mac);
		}
	}

}
