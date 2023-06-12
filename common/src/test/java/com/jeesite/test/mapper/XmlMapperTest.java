/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.mapper;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.mapper.XmlMapper;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * XML工具测试类
 * @author ThinkGem
 * @version 2023-6-9
 */
public class XmlMapperTest {

	public static void main(String[] args) throws Exception {
		File file = new File(FileUtils.getProjectPath()
				+ "/../modules/core/src/main/resources/config/logger-core.xml");
		String xml = FileUtils.readFileToString(file, Charset.defaultCharset());

		XmlMapper m = XmlMapper.getInstance();
		System.out.println(m.readValue(xml, List.class));
		System.out.println(m.readValue(xml, Map.class));

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
		list.add(map);

		String s = m.writeValueAsString(list);
		System.out.println(s);

		list = m.readValue(s, List.class);
		System.out.println(list);

	}
	
}
