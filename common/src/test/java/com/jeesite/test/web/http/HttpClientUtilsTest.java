/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.web.http;

import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.web.http.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP客户端测试工具类（支持HTTPS）
 * @author ThinkGem
 * @version 2017-3-27
 */
public class HttpClientUtilsTest {

	public static void main(String[] args) {
		String url = "https://vue.jeesite.com/js/a/sys/corpAdmin/treeData";
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("isShowCode", "true");
		dataMap.put("param1", "你好");
		System.out.println(HttpClientUtils.get(url, dataMap));
		System.out.println(HttpClientUtils.ajaxGet(url, dataMap));
		System.out.println(HttpClientUtils.post(url, dataMap));
		System.out.println(HttpClientUtils.ajaxPost(url, dataMap));
		System.out.println(HttpClientUtils.ajaxPostJson(url, JsonMapper.toJson(dataMap)));
	}

}
