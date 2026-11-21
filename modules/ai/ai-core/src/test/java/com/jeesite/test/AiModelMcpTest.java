/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.entity.AiMcp;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.factory.AiMcpFactory;
import com.jeesite.modules.ai.utils.AiMcpUtils;
import com.jeesite.modules.ai.utils.AiModelUtils;
import io.modelcontextprotocol.client.McpSyncClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * AI MCP 工具调用单元测试（使用「AI模型」ai_model 中的动态配置，通过 {@link AiModelUtils} 调用）
 * <p>
 * MCP 工具来源：
 * <ol>
 *   <li>优先使用「MCP服务」（ai_mcp）中配置并启用的服务</li>
 *   <li>未配置时直接连接本机 jeesite-web-ai-mcp（临时客户端，测试结束释放）</li>
 * </ol>
 * 前置条件：
 * <ul>
 *   <li>数据库中已注册对话模型 qwen3-8b（{@link #MODEL_CODE_CHAT}），且支持 function calling</li>
 *   <li>先启动 jeesite-web-ai-mcp（默认 http://127.0.0.1:8992/api/v1/mcp），
 *       或在「MCP服务」中配置可用的 streamable_http / sse 服务；服务不可用时自动跳过</li>
 * </ul>
 * @author ThinkGem
 * @version 2026-11-19
 */
@ActiveProfiles("unit_test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(classes = ApplicationTest.class)
public class AiModelMcpTest extends BaseSpringContextTests {

	/** 对话模型编码（ai_model.model_code） */
	private static final String MODEL_CODE_CHAT = "qwen3-8b";

	/** MCP 服务编码（本机 jeesite-web-ai-mcp） */
	private static final String MCP_CODE = "jeesite-ai-mcp";

	/** MCP 服务地址（jeesite-web-ai-mcp 默认端口 8992，Streamable HTTP 端点 /api/v1/mcp） */
	private static final String MCP_URL = "http://127.0.0.1:8992/api/v1/mcp";

	/** 临时创建的 MCP 客户端（测试结束后释放） */
	private McpSyncClient mcpClient;
	/** 临时 MCP 客户端对应的工厂（用于关闭连接） */
	private AiMcpFactory mcpClientFactory;

	@AfterEach
	public void closeMcpClient() {
		if (mcpClient != null) {
			if (mcpClientFactory != null) {
				mcpClientFactory.close(mcpClient);
			} else {
				try {
					mcpClient.closeGracefully();
				} catch (Exception ignored) {
					mcpClient.close();
				}
			}
			mcpClient = null;
			mcpClientFactory = null;
		}
	}

	/**
	 * 获取模型，不存在、停用或未配置接口地址时跳过当前测试
	 * @param modelCode 模型编码
	 * @param modelType 模型用途（用于跳过提示）
	 */
	protected AiModel getModelOrSkip(String modelCode, String modelType) {
		AiModel aiModel;
		try {
			aiModel = AiModelUtils.getModel(modelCode);
		} catch (Exception e) {
			assumeTrue(false, "读取「AI模型」配置失败：" + e.getMessage());
			return null;
		}
		assumeTrue(aiModel != null, "未在「AI模型」中配置" + modelType + "模型：" + modelCode);
		assumeTrue(AiModel.STATUS_NORMAL.equals(aiModel.getStatus()), "模型已停用：" + modelCode);
		assumeTrue(StringUtils.isNotBlank(aiModel.getBaseUrl()), "模型未配置接口地址：" + modelCode);
		return aiModel;
	}

	/**
	 * MCP 工具调用：模型根据用户问题自动调用 MCP 服务端暴露的工具
	 */
	@Test
	public void test01Mcp() {
		logger.info("===== MCP 工具调用（jeesite-web-ai-mcp），模型：{}", MODEL_CODE_CHAT);
		AiModel aiModel = getModelOrSkip(MODEL_CODE_CHAT, "对话");
		List<ToolCallback> callbacks = findMcpToolCallbacks();
		assumeTrue(!callbacks.isEmpty(), "未获取到 MCP 工具");
		callbacks.forEach(callback -> logger.info("MCP 工具：{} - {}",
				callback.getToolDefinition().name(), callback.getToolDefinition().description()));

		ChatClient chatClient = AiModelUtils.chatClientBuilder(aiModel).defaultTools(callbacks).build();
		String content;
		try {
			// 模型自动调用 MCP 服务端暴露的工具（server-time 等）
			content = chatClient.prompt()
					.system("你可以调用 MCP 工具完成用户请求，回答简洁。")
					.user("请告诉我当前服务器的日期和时间。")
					.call()
					.content();
		} catch (Exception e) {
			assumeTrue(false, "MCP 工具调用失败（模型可能不支持 function calling）：" + e.getMessage());
			return;
		}
		System.out.println("MCP 工具调用结果：" + content);
		assertNotNull(content);
		assertFalse(content.isBlank());
	}

	/**
	 * 获取 MCP 工具回调：
	 * <ol>
	 *   <li>优先使用「MCP服务」中配置并启用的服务</li>
	 *   <li>未配置时直接连接本机 jeesite-web-ai-mcp（临时客户端，测试结束释放）</li>
	 * </ol>
	 */
	protected List<ToolCallback> findMcpToolCallbacks() {
		// 1. 直接命中配置的 MCP 服务编码
		try {
			AiMcp configured = AiMcpUtils.getMcp(MCP_CODE);
			if (configured != null && AiMcp.STATUS_NORMAL.equals(configured.getStatus())) {
				List<ToolCallback> callbacks = AiMcpUtils.getToolCallbacks(MCP_CODE);
				if (!callbacks.isEmpty()) {
					logger.info("使用已配置的 MCP 服务：{}（{}）", configured.getMcpName(), configured.getMcpUrl());
					return callbacks;
				}
			}
		} catch (Exception e) {
			logger.warn("读取 MCP 服务配置失败：{}", e.getMessage());
		}
		// 2. 使用「MCP服务」中任一个可用的 HTTP 传输服务
		try {
			AiMcp where = new AiMcp();
			where.setStatus(AiMcp.STATUS_NORMAL);
			for (AiMcp mcp : AiMcpUtils.findList(where)) {
				if (!AiMcp.TYPE_STREAMABLE_HTTP.equals(mcp.getMcpType())
						&& !AiMcp.TYPE_SSE.equals(mcp.getMcpType())) {
					continue;
				}
				try {
					List<ToolCallback> callbacks = AiMcpUtils.getToolCallbacks(mcp.getMcpCode());
					if (!callbacks.isEmpty()) {
						logger.info("使用 MCP 服务：{}（{}）", mcp.getMcpName(), mcp.getMcpUrl());
						return callbacks;
					}
				} catch (Exception e) {
					logger.warn("MCP 服务不可用，已跳过：{}，{}", mcp.getMcpCode(), e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.warn("查询 MCP 服务列表失败：{}", e.getMessage());
		}
		// 3. 未配置时，连接本机 jeesite-web-ai-mcp（需先启动该服务）
		assumeTrue(isServerAvailable(MCP_URL), "MCP 服务端未启动：" + MCP_URL
				+ "，请先启动 jeesite-web-ai-mcp，或在「MCP服务」中配置可用服务");
		AiMcp aiMcp = new AiMcp();
		aiMcp.setMcpCode(MCP_CODE);
		aiMcp.setMcpName(MCP_CODE);
		aiMcp.setMcpType(AiMcp.TYPE_STREAMABLE_HTTP);
		aiMcp.setMcpUrl(MCP_URL);
		AiMcpFactory factory = AiMcpUtils.getAiMcpFactory(aiMcp);
		try {
			mcpClient = factory.getMcpClient(aiMcp);
			mcpClientFactory = factory;
		} catch (Exception e) {
			assumeTrue(false, "MCP 连接失败：" + e.getMessage());
			return List.of();
		}
		List<ToolCallback> callbacks = factory.getToolCallbacks(List.of(mcpClient));
		logger.info("已连接 MCP 服务：{}，工具数量：{}", MCP_URL, callbacks.size());
		return callbacks;
	}

	/**
	 * 探测服务端口是否可连接（避免 MCP 连接超时长时间阻塞）
	 */
	protected boolean isServerAvailable(String url) {
		URI uri;
		try {
			uri = URI.create(url);
		} catch (Exception e) {
			return false;
		}
		String host = uri.getHost();
		if (StringUtils.isBlank(host)) {
			return false;
		}
		int port = uri.getPort();
		if (port <= 0) {
			port = "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
		}
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), 2000);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
