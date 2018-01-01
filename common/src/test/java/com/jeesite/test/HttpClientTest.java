package com.jeesite.test;

import java.util.Map;

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.web.http.HttpClientUtils;

public class HttpClientTest {

	public static void main(String[] args) {
		
		String url = "http://:8080/multData/a/login";

		Map<String, String> map = MapUtils.newHashMap();
		
		map.put("currentDataSource", "86fecee8d2e04bbe8e6c23dff9338f66");
		map.put("mobileLogin", "true");
		map.put("username", "system");
		map.put("password", "admin");
		
		String res = HttpClientUtils.ajaxPost(url, map);
		System.out.println(res);
		
	}
}
