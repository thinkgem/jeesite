/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.mapper;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.mapper.JsonMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Json工具测试类
 * @author ThinkGem
 * @version 2023-6-9
 */
public class JsonMapperTest {

	public static void main(String[] args) {
		List<Map<String, Object>> list = ListUtils.newArrayList();
		Map<String, Object> map = MapUtils.newHashMap();
		map.put("id", 1);
		map.put("pId", -1);
		map.put("name", "根节点");
		list.add(map);
		map = MapUtils.newHashMap();
		map.put("id", 2);
		map.put("pId", 1);
		map.put("name", "你好");
		map.put("open", true);
		map.put("date", new Date());
		list.add(map);
		String json = JsonMapper.toJson(list);
		System.out.println(json);
		List<Map<String, Object>> map2 = JsonMapper.fromJson(json, List.class);
		System.out.println(map2);
		Map<String, Object> map3 = JsonMapper.fromJson("{extendS1:{title:'站牌号',"
				+ "sort:1,type:'text',maxlength:0,maxlength:30},extendS2:{title:'规模分类',"
				+ "sort:2,type:'dict',dictType:'scope_category'}}", Map.class);
		System.out.println(map3);
		List<String> list2 = JsonMapper.fromJson("[1,2]", List.class);
		System.out.println(list2);
	}
	
}
