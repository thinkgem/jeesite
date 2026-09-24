/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.exec;

import com.jeesite.common.lang.StringUtils;

import java.io.*;

/**
 * Command
 * <p>
 * 注意：执行外部命令存在命令注入风险，请勿将不可信数据（如用户输入、请求参数）直接拼接到命令中，
 * 推荐使用 {@link #execute(String[], String)} 按参数数组的方式传递命令，参数不会被当作命令解析。
 * @author ThinkGem
 * @version 2026-09-23
 */
public class CommandUtils {

	/** 命令及参数中禁止出现的元字符，防止命令注入攻击 */
	private static final String[] UNSAFE_COMMAND_CHARS = {"\"", "`", "&", "|", ";", "$", "^", "<", ">", "%", "!", "\r", "\n"};

	/** 禁止调用的系统命令解释器，防止间接命令注入攻击 */
	private static final String[] UNSAFE_COMMANDS = {"cmd", "cmd.exe", "sh", "bash", "zsh",
			"ksh", "csh", "powershell", "powershell.exe", "pwsh", "pwsh.exe"};

	/**
	 * 执行命令（命令字符串方式，内部按空白符拆分为参数数组，请勿拼接不可信数据）
	 */
	public static String execute(String command) throws IOException {
		return execute(command, "GBK");
	}

	/**
	 * 执行命令（命令字符串方式，内部按空白符拆分为参数数组，请勿拼接不可信数据）
	 */
	public static String execute(String command, String charsetName) throws IOException {
		if (StringUtils.isBlank(command)) {
			throw new IllegalArgumentException("执行的命令不能为空");
		}
		return execute(StringUtils.split(command.trim()), charsetName);
	}

	/**
	 * 执行命令（推荐方式：按参数数组传递命令，参数不会被当作命令解析，安全性更高）
	 */
	public static String execute(String[] command) throws IOException {
		return execute(command, "GBK");
	}

	/**
	 * 执行命令（推荐方式：按参数数组传递命令，参数不会被当作命令解析，安全性更高）
	 */
	public static String execute(String[] command, String charsetName) throws IOException {
		// 防止命令注入：校验命令及参数的安全性
		checkCommand(command);
		// 采用 ProcessBuilder 按参数数组执行，不经过 shell 解析，避免命令注入
		Process process = new ProcessBuilder(command).start();
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

	/**
	 * 校验命令及参数的安全性，防止命令注入攻击
	 */
	public static void checkCommand(String[] command) {
		if (command == null || command.length == 0 || StringUtils.isBlank(command[0])) {
			throw new IllegalArgumentException("执行的命令不能为空");
		}
		for (String arg : command) {
			if (StringUtils.containsAny(arg, UNSAFE_COMMAND_CHARS)) {
				throw new IllegalArgumentException("执行的命令包含非法字符，已拒绝执行：" + arg);
			}
		}
		// 禁止调用系统命令解释器，避免间接命令注入
		String execName = StringUtils.lowerCase(StringUtils.substringAfterLast(command[0].trim(), "/"));
		execName = StringUtils.lowerCase(StringUtils.substringAfterLast(execName, "\\"));
		for (String unsafe : UNSAFE_COMMANDS) {
			if (unsafe.equals(execName)) {
				throw new IllegalArgumentException("禁止调用系统命令解释器，已拒绝执行：" + command[0]);
			}
		}
	}

}
