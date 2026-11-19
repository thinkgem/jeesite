/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.tests.BaseSpringContextTests;
import com.jeesite.modules.ai.entity.AiModel;
import com.jeesite.modules.ai.utils.AiModelUtils;
import com.jeesite.modules.ai.utils.AiToolUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * AI 模型单元测试（全部使用「AI模型」ai_model 中的动态配置，通过 {@link AiModelUtils} 调用）
 * <p>
 * 覆盖：
 * <ol>
 *   <li>AI 对话（文本输出）</li>
 *   <li>AI 结构化输出（JSON、Java 对象）</li>
 *   <li>AI 向量模型（nomic-embed-text-latest）</li>
 *   <li>AI Tool 工具调用（本地 @Tool Bean / 「工具管理」配置的工具）</li>
 * </ol>
 * 前置条件：
 * <ul>
 *   <li>数据库中已注册模型：qwen3-8b、nomic-embed-text-latest
 *       （模型编码见本类常量 MODEL_CODE_CHAT、MODEL_CODE_EMBEDDING；
 *       生图/识图见 {@link AiModelImageTest}，MCP 见 {@link AiModelMcpTest}）</li>
 * </ul>
 * @author ThinkGem
 * @version 2026-11-19
 */
@ActiveProfiles("unit_test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(classes = ApplicationTest.class)
public class AiModelChatTest extends BaseSpringContextTests {

	/** 结构化输出时的系统提示词：强制模型只输出纯 JSON */
	private static final String JSON_ONLY_PROMPT = """
			你是一个严格的数据提取和格式化工具，你的唯一任务是输出合法、标准的 JSON。
			必须遵守以下规则：
			1. 仅输出纯 JSON 字符串（对象或数组），不要输出任何解释性文字；
			2. 严禁使用 Markdown 代码块标记（即不要包含 ```json 或 ```）；
			3. 字段名必须与要求一致，字段值不能为空（无法确定时使用空字符串）。
			""";

	/** 对话模型编码（ai_model.model_code） */
	private static final String MODEL_CODE_CHAT = "qwen3-8b";

	/** 向量模型编码 */
	private static final String MODEL_CODE_EMBEDDING = "nomic-embed-text-latest";

	/** 工具编码（「工具管理」ai_tool.tool_code，多个用逗号分隔；为空时使用本地 @Tool Bean） */
	private static final String TOOL_CODES = "";

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

	// ==================== 1. 文本对话 ====================

	@Test
	public void test01Text() {
		logger.info("===== AI 对话，文本输出，模型：{}", MODEL_CODE_CHAT);
		AiModel aiModel = getModelOrSkip(MODEL_CODE_CHAT, "对话");
		String text = AiModelUtils.getChatClient(aiModel)
				.prompt()
				.system("你是一个乐于助人的中文助手，回答简洁。")
				.user("你好，请用一句话介绍一下你自己。")
				.call()
				.content();
		System.out.println("文本输出：" + text);
		assertNotNull(text);
		assertFalse(text.isBlank());
	}

	// ==================== 2. 结构化输出 ====================

	/**
	 * 结构化输出 JSON（Map），验证模型返回的内容可被解析为 JSON 对象
	 */
	@Test
	public void test02Json() {
		logger.info("===== AI 结构化输出，JSON（Map），模型：{}", MODEL_CODE_CHAT);
		AiModel aiModel = getModelOrSkip(MODEL_CODE_CHAT, "对话");
		Map<String, Object> json = AiModelUtils.getChatClient(aiModel)
				.prompt()
				.system(JSON_ONLY_PROMPT)
				.user("从下面文本中提取人员信息，字段为 name、gender、age、city："
						+ "张三，男，17 岁，现居北京市朝阳区。")
				.call()
				.entity(new MapOutputConverter());
		System.out.println("JSON 输出：" + JsonMapper.toJson(json));
		assertNotNull(json);
		assertFalse(json.isEmpty());
	}

	/**
	 * 结构化输出 Java 对象（单个对象、对象列表）
	 */
	@Test
	public void test03Entity() {
		logger.info("===== AI 结构化输出，Java 对象，模型：{}", MODEL_CODE_CHAT);
		AiModel aiModel = getModelOrSkip(MODEL_CODE_CHAT, "对话");
		ChatClient chatClient = AiModelUtils.getChatClient(aiModel);

		PersonInfo person = chatClient.prompt()
				.system(JSON_ONLY_PROMPT)
				.user("从下面文本中提取人员信息：李四，女，28 岁，现居上海市浦东新区。")
				.call()
				.entity(PersonInfo.class);
		System.out.println("Java 对象：" + JsonMapper.toJson(person));
		assertNotNull(person);
		assertTrue(StringUtils.isNotBlank(person.getName()));

		List<PersonInfo> list = chatClient.prompt()
				.system(JSON_ONLY_PROMPT)
				.user("从下面文本中提取全部人员信息：张三，男，17 岁，北京市；李四，女，28 岁，上海市。")
				.call()
				.entity(new ParameterizedTypeReference<List<PersonInfo>>() {});
		System.out.println("Java 对象列表：" + JsonMapper.toJson(list));
		assertNotNull(list);
		assertFalse(list.isEmpty());
	}

	// ==================== 3. 向量模型 ====================

	@Test
	public void test04Embedding() {
		logger.info("===== AI 向量模型，模型：{}", MODEL_CODE_EMBEDDING);
		AiModel aiModel = getModelOrSkip(MODEL_CODE_EMBEDDING, "向量");
		EmbeddingModel embeddingModel = AiModelUtils.getEmbeddingModel(aiModel.getModelCode());
		float[] vector = embeddingModel.embed("JeeSite AI 单元测试");
		assertNotNull(vector);
		assertTrue(vector.length > 0);
		System.out.println("向量维度：" + vector.length);
	}

	// ==================== 4. AI Tool 工具调用 ====================

	@Test
	public void test05Tool() {
		logger.info("===== AI Tool 工具调用，模型：{}", MODEL_CODE_CHAT);
		AiModel aiModel = getModelOrSkip(MODEL_CODE_CHAT, "对话");
		List<ToolCallback> callbacks = findToolCallbacks();
		assumeTrue(!callbacks.isEmpty(), "未找到可用工具");
		callbacks.forEach(callback -> logger.info("工具：{} - {}",
				callback.getToolDefinition().name(), callback.getToolDefinition().description()));

		// 直接调用工具，验证工具实现本身可用
		callbacks.stream()
				.filter(callback -> "server-time".equals(callback.getToolDefinition().name()))
				.findFirst()
				.ifPresent(callback -> {
					String toolResult = callback.call("{}");
					System.out.println("工具直调结果：" + toolResult);
					assertTrue(StringUtils.isNotBlank(toolResult));
				});

		ChatClient chatClient = AiModelUtils.chatClientBuilder(aiModel).defaultTools(callbacks).build();
		String content;
		try {
			// 模型自动进行 function calling：server-time、root-light-switch
			content = chatClient.prompt()
					.system("你可以调用工具完成用户请求，回答简洁。")
					.user("请告诉我当前服务器的日期和时间，然后帮我打开客厅的灯。")
					.call()
					.content();
		} catch (Exception e) {
			assumeTrue(false, "工具调用失败（模型可能不支持 function calling）：" + e.getMessage());
			return;
		}
		System.out.println("工具调用结果：" + content);
		assertNotNull(content);
		assertFalse(content.isBlank());
	}

	/**
	 * 查找工具回调：
	 * <ol>
	 *   <li>配置了 ai.test.tool.codes 时，按「工具管理」中的工具编码读取</li>
	 *   <li>否则使用本地 @Tool Bean（{@link TestAiTools}）</li>
	 * </ol>
	 */
	protected List<ToolCallback> findToolCallbacks() {
		if (StringUtils.isNotBlank(TOOL_CODES)) {
			try {
				List<ToolCallback> callbacks = AiToolUtils.getToolCallbacks(List.of(StringUtils.split(TOOL_CODES)));
				if (!callbacks.isEmpty()) {
					return callbacks;
				}
			} catch (Exception e) {
				logger.warn("读取「工具管理」配置失败：{}，回退本地 @Tool Bean", e.getMessage());
			}
		}
		return List.of(ToolCallbacks.from(new TestAiTools()));
	}

	/**
	 * 结构化输出的人员信息（Spring AI BeanOutputConverter 按字段生成 JSON Schema）
	 */
	public static class PersonInfo {

		private String name;
		private String gender;
		private Integer age;
		private String city;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}
	}

	/**
	 * 本地测试用 AI 工具（不依赖 ai-tools 模块）
	 */
	public static class TestAiTools {

		/**
		 * 获取服务器时间
		 */
		@Tool(name = "server-time", description = "当前服务器日期时间，格式为 yyyy-MM-dd HH:mm:ss。")
		public String getCurrentDateTime() {
			String dateTime = "当前日期时间：" + DateUtils.getDateTime();
			logger().info("当前日期时间 ============== {}", dateTime);
			return dateTime;
		}

		/**
		 * 开关房间的灯
		 */
		@Tool(name = "root-light-switch",
				description = "控制指定房间的灯光开关。需要提供房间名称（如 '客厅'、'卧室'）和目标状态（true 表示开灯，false 表示关灯）。")
		public String roomLightSwitch(
				@ToolParam(description = "要控制的房间名称，例如：'客厅'、'卧室'、'餐厅'、'厨房'") String roomName,
				@ToolParam(description = "灯光目标状态：true 表示打开灯，false 表示关闭灯") boolean on) {
			String message = roomName + " 房间里的灯被 " + (on ? "打开" : "关闭");
			logger().info("房间灯光开关 ============== {}", message);
			return String.format("""
				{
				  "message": "%s",
				  "roomName": "%s",
				  "on": %s
				}
				""", message, roomName, on);
		}

		private static org.slf4j.Logger logger() {
			return org.slf4j.LoggerFactory.getLogger(TestAiTools.class);
		}
	}

}
