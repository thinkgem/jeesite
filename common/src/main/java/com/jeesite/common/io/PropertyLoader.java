/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.common.io;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.parser.ParserConfig;

/**
 * 配置文件加载（Boot）
 * @author ThinkGem
 * @version 2018-10-16
 */
public class PropertyLoader implements org.springframework.boot.env.PropertySourceLoader, Ordered{
	
	private static boolean isLoadPropertySource = false;
	
	@Override
	public String[] getFileExtensions() {
		return new String[] { "properties", "yml", "yaml" };
	}
	
	@Override
	public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
		if (!isLoadPropertySource){
			isLoadPropertySource = true;
			ParserConfig.getGlobalInstance().setSafeMode(true); // 开启 FastJSON 安全模式
			Properties properties = PropertiesUtils.getInstance().getProperties();
			return Collections.singletonList(new OriginTrackedMapPropertySource("jeesite", properties));
		}
		return Collections.emptyList();
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
	
}
