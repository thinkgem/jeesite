/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.web.http;

import com.jeesite.common.io.PropertiesUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 统一包装结果输出类：{ code: 200, msg: "", data: {} | [] }
 * @author ThinkGem
 * @version 2024-07-24
 */
public class ResultUtils {

	private static final boolean isDefaultResult = PropertiesUtils.getInstance()
			.getPropertyToBoolean("web.isDefaultResult", "false");
	private static final String resultParamName = PropertiesUtils.getInstance()
			.getProperty("web.resultParamName", "__data");
	private static final String headerParamName = PropertiesUtils.getInstance()
			.getProperty("web.headerParamName", "x-data");

	/**
	 * 设置 web.isResult 参数可强制全局使用统一结果输出，否则，传递 __data=true 参数，或 x-data 请求头为 true 时启用
	 * @author ThinkGem
	 */
	public static Object result(Object data, HttpServletRequest request, HttpServletResponse response) {
		if (request != null && response != null && (isDefaultResult || (
				"true".equals(request.getParameter(resultParamName))
						|| "true".equals(request.getHeader(headerParamName))))) {
			Object msg = request.getAttribute("message");
			return new Result(response.getStatus(), msg, data);
		}
		return data;
	}

	/**
	 * 结果对象
	 * @author ThinkGem
	 * @version 2024-07-24
	 */
	private static class Result {
		private int code;
		private String msg;
		private Object data;

		public Result(int code, Object msg, Object data) {
			this.code = code;
			this.msg = msg != null ? msg.toString() : null;
			this.data = data;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	}

}
