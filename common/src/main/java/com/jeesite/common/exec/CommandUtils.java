/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Command
 * @author ThinkGem
 * @version 2017年2月17日
 */
public class CommandUtils {

	public static String execute(String command) throws IOException {
		return execute(command, "GBK");
	}
	
	public static String execute(String command, String charsetName) throws IOException {
		Process process = Runtime.getRuntime().exec(command);
		// 记录dos命令的返回信息
		StringBuilder sb = new StringBuilder();
		// 获取返回信息的流
		InputStream in = process.getInputStream();
		Reader reader = new InputStreamReader(in, charsetName);
		BufferedReader bReader = new BufferedReader(reader);
		String res = bReader.readLine();
		while (res != null) {
			sb.append(res);
			sb.append("\n");
			res = bReader.readLine();
		}
		bReader.close();
		reader.close();
		return sb.toString();
	}
	
}
