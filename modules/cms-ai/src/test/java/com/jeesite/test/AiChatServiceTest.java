/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.cms.ai.service.CmsAiChatService;
import com.jeesite.modules.sys.entity.Area;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

/**
 * AI 对话单元测试
 * @author ThinkGem
 * @version 2025-06-06
 */
@ActiveProfiles("test")
@SpringBootApplication
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(properties = {"spring.ai.tool-calls=true"})
public class AiChatServiceTest extends BaseSpringContextTests {

	@Autowired
	private CmsAiChatService cmsAiChatService;

	@Test
	public void test01Text() {
		logger.info("===== 聊天对话，文本输出");
		String message = "你好";
		String text = cmsAiChatService.chatText(message);
		System.out.println(text);
	}

	@Test
	public void test02Json() {
		logger.info("===== 聊天对话，结构化输出 JSON");
		String message = "张三";
		Map<String, Object> map = cmsAiChatService.chatJson(message);
		System.out.println(JsonMapper.toJson(map));
	}

	@Test
	public void test03Tool() {
		logger.info("===== 聊天对话，结构化输出 Tool Calling");
		String message = "打开客厅的灯";
		Map<String, Object> map = cmsAiChatService.chatJson(message);
		System.out.println(JsonMapper.toJson(map));
		message = "关闭客厅的灯";
		map = cmsAiChatService.chatJson(message);
		System.out.println(JsonMapper.toJson(map));
	}

	@Test
	public void test04Entity() {
		logger.info("===== 聊天对话，结构化输出 Entity");
		String message = "北京";
		List<Area> list = cmsAiChatService.chatArea(message);
		System.out.println(JsonMapper.toJson(list));
	}


}
