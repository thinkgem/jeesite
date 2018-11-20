package com.jeesite.test;

import java.util.List;
import java.util.Map;

import com.jeesite.common.codec.DesUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.web.http.HttpClientUtils;

/**
 * 手机接入测试类
 * @author ThinkGem
 * @version 2016-10-21
 */
public class MobileAppTest {
	
	public static void main(String[] args) {

		String url = null;
		
		// 获取权限信息
		url = "/index?permi=true";
		System.out.println("result: " + post(url, null));

		// 获取部门列表数据
		url = "/sys/office/treeData";
		System.out.println("result: " + post(url, null));

		// 获取用户列表数据
		url = "/sys/empUser/treeData";
		System.out.println("result: " + post(url, null));

	}
	
	/**
	 * 服务器地址
	 */
	private static String servUrl = "http://127.0.0.1:8980/js/a";
	
	/**
	 * 登录地址及登录信息
	 */
	private static String loginUrl = "/login"
			+ "?username=" + DesUtils.encode("system", "thinkgem,jeesite,com")
			+ "&password=" + DesUtils.encode("admin", "thinkgem,jeesite,com")
			+ "&param_deviceType=mobileApp&param_lang=zh_CN&__sid="; 
	
	/**
	 * 存储的会话编号，则通过getSid()获取。
	 */
	private static String __sid = "";

	
	/**
	 * 获取有效的会话编号
	 */
	public static void refreshSid(){
		List<Map<String, Object>> result = post(loginUrl, null);
		if (result.size() == 1){
			Map<String, Object> res = result.get(0);
			if ("true".equals(res.get("result"))){
				__sid = (String)res.get("sessionid");
			}else{
				throw new RuntimeException("操作失败！错误信息：" + res.get("message"));
			}
		}
	}
	
	/**
	 * 发送post请求
	 */
	public static List<Map<String, Object>> post(String url, Map<String, String> data){
		List<Map<String, Object>> result = ListUtils.newArrayList();
		for (int i = 0; i < 2; i++){
			if (data == null){
				data = MapUtils.newHashMap();
			}
			data.put("__sid", __sid); // 设置 SessionID
			String body = HttpClientUtils.ajaxPost(servUrl + url, data);
			System.out.println("post: " + servUrl + url + ", param: " + data + " , body: " + body);
			result = JsonMapper.fromJsonForMapList(body);
			if (result.size() == 1 && "login".equals(result.get(0).get("result"))){
				refreshSid(); // 刷新SessionID再重新尝试
			}else{
				break;
			}
		}
		return result;
	}
	
}
