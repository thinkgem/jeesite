/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.io;

import com.alibaba.fastjson.parser.ParserConfig;
import com.jeesite.common.lang.StringUtils;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 配置文件加载（Boot）
 * @author ThinkGem
 * @version 2020-10-26
 */
public class PropertyLoader implements org.springframework.boot.env.PropertySourceLoader, Ordered{
	
	private static boolean isLoadJeeSitePropertySource = false;
	private final PropertiesPropertySourceLoader propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();
	private final YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
	
	@Override
	public String[] getFileExtensions() {
		return new String[] { "properties", "yml", "yaml" };
	}
	
	@Override
	public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
		List<PropertySource<?>> propertySources = new ArrayList<>();
		if (!isLoadJeeSitePropertySource) {
			isLoadJeeSitePropertySource = true;
			try {
				// 默认开启 FastJSON 1.x 的，安全模式
				ParserConfig.getGlobalInstance().setSafeMode(true);
			} catch (NoSuchMethodError ignored) {
				// 兼容 FastJSON 2.x 的调用，忽略异常
			}
			Properties properties = PropertiesUtils.getInstance().getProperties();
			propertySources.add(new OriginTrackedMapPropertySource("jeesite", properties));
		} else {
			String ext = FileUtils.getFileExtension(resource.getFilename());
			if (StringUtils.inString(ext, propertiesPropertySourceLoader.getFileExtensions())) {
				propertySources.addAll(propertiesPropertySourceLoader.load(name, resource));
			}else if (StringUtils.inString(ext, yamlPropertySourceLoader.getFileExtensions())) {
				propertySources.addAll(yamlPropertySourceLoader.load(name, resource));
			}
		}
		return propertySources;
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
	
}
