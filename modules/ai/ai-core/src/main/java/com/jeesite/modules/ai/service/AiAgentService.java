/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.modules.ai.dao.AiAgentDao;
import com.jeesite.modules.ai.entity.*;
import com.jeesite.modules.ai.factory.tool.callback.AiAskUserTool;
import com.jeesite.modules.ai.factory.tool.callback.AiTodoWriteTool;
import com.jeesite.modules.ai.factory.tool.callback.AiToolCallbackWrapper;
import com.jeesite.modules.ai.utils.AiMcpUtils;
import com.jeesite.modules.ai.utils.AiModelUtils;
import com.jeesite.modules.ai.utils.AiToolUtils;
import com.jeesite.modules.ai.utils.AiVectorUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AI智能体 Service
 * <p>
 * 由 {@code AiCoreAutoConfiguration} 注入，业务可通过定义同类型 Bean 覆盖（二开：继承本类并注册 Bean）。
 * <p>
 * 智能体绑定的工具、MCP 服务、知识库通过关联表维护（编辑页面选择）：
 * <ul>
 *   <li>ai_agent_tool：智能体与工具的关联（工具编码）</li>
 *   <li>ai_agent_mcp：智能体与 MCP 服务的关联（挂载后自动获得其全部工具）</li>
 *   <li>ai_agent_vector：智能体与向量库绑定的关联（绑定已包含向量库、向量模型、检索条数与相似度阈值）</li>
 * </ul>
 * 其它参数通过 {@code options} 配置：
 * <ul>
 *   <li>options.qaPromptTemplate：知识库问答提示词模板，须包含 {question_answer_context} 占位符，
 *       可用 {query} 引用用户问题，为空则使用 {@link #DEFAULT_QA_PROMPT_TEMPLATE}</li>
 *   <li>options.todoWrite：是否开启任务计划工具（把多步骤任务拆成任务清单并实时下发进度）</li>
 * </ul>
 * @author ThinkGem
 * @version 2026-11-14
 */
public class AiAgentService extends CrudService<AiAgentDao, AiAgent> {

	/**
	 * 开启任务计划工具（options.todoWrite）时追加的系统提示词
	 * <p>
	 * 模型默认很少主动调用 {@code todo_write}，不引导则前端任务清单始终为空。
	 */
	public static final String TODO_WRITE_PROMPT = """

			任务规划要求：
			1. 需求包含 2 个及以上步骤、或需要连续调用 2 次以上工具、或涉及多个文件 / 多个子任务时，
			   第一轮回答必须先用 todo_write 建立任务清单，再开始执行；
			   计划必须通过 todo_write 外化，不要只在深度思考内容里规划；
			2. 即使多个操作可以在同一轮并行调用（例如同时查用户、查时间、改状态），也要先把它们写成任务清单；
			3. 任务是「工作单元」，不等于一次工具调用：一个任务可能包含多次工具调用、分析推理、查询确认，
			   也可能不需要调用任何工具（例如梳理方案、汇总结论），不要为每次工具调用新建任务；
			4. 任务之间可以有先后依赖和中断（例如中途等用户选择方向、等待确认），中断恢复后继续推进未完成的任务；
			5. 开始某项任务前，调用 todo_write 把该任务改为 in_progress，同一时刻最多只保留一个 in_progress；
			6. 该任务的全部工作都完成后再改为 completed，不要因为调用了一次工具就标记完成，也不要把多项攒到最后一次性更新；
			7. 任务受阻或不再需要时标记 cancelled，并在回答里说明原因；
			8. 每次提交完整的任务列表（含已完成项），不要只提交本次变更的项；
			9. 简单的单步问答不要建任务清单。
			""";

	/**
	 * 任务方向确认的引导提示词（模型可调用 ask_user 向用户提问时追加）
	 */
	public static final String ASK_USER_PROMPT = """

			任务方向确认：遇到下列任一情况，必须先用 ask_user 与用户确认，不得自行假设后直接执行：
			1. 存在多个明显不同的方向或方案（改哪个对象、做哪个范围、用哪种实现），选错代价明显；
			2. 缺少推进必需的关键信息（对象、范围、环境、数据源、时间范围），且无法从上下文或历史消息推断；
			3. 操作有破坏性或不可逆（删除、覆盖、批量修改、发布上线、变更权限）；
			4. 需求内部矛盾，或与现有实现冲突，需要用户取舍。

			以下情况不要提问：能从上下文推断、有明确默认值、单步且影响很小的改动。

			提问方式：
			- 必须调用 ask_user 工具，不要在回答正文里直接向用户发问，否则界面无法显示可选项；
			- header 用一句话说清要用户决定什么；question 简要说明各选项差异；
			- options 给 2-4 个，label 必填且互斥，description 说明该选项的影响或后果；
			- allowCustom 保持 true，允许用户自由补充；
			- 用户回答后按选择执行，不要重复询问同一个问题。
			""";

	/**
	 * 知识库问答默认提示词模板（{question_answer_context} 为检索到的参考资料，{query} 为用户问题）
	 */
	public static final String DEFAULT_QA_PROMPT_TEMPLATE = """
			你是一个智能助手，可以基于以下参考资料回答用户的问题。
			如果参考资料中没有相关信息，请明确告知用户，不要编造内容。

			参考资料：
			{question_answer_context}

			问题：{query}
			""";

	protected final AiAgentToolService aiAgentToolService;
	protected final AiAgentMcpService aiAgentMcpService;
	protected final AiAgentVectorService aiAgentVectorService;

	/**
	 * 构造注入
	 * <p>
	 * 模型、工具、MCP、向量库均通过对应的 Utils 静态调用，不再注入 Service，
	 * 避免与 Utils 中的懒加载 Bean 重复依赖。
	 * @param aiAgentToolService 智能体与工具关联 Service
	 * @param aiAgentMcpService 智能体与MCP服务关联 Service
	 * @param aiAgentVectorService 智能体与向量库绑定关联 Service
	 */
	public AiAgentService(AiAgentToolService aiAgentToolService, AiAgentMcpService aiAgentMcpService,
			AiAgentVectorService aiAgentVectorService) {
		this.aiAgentToolService = aiAgentToolService;
		this.aiAgentMcpService = aiAgentMcpService;
		this.aiAgentVectorService = aiAgentVectorService;
	}

	/**
	 * 获取智能体当前配置的模型（含模型选项，用于价格计算）
	 * @param aiAgent 智能体
	 */
	public AiModel getAgentModel(AiAgent aiAgent) {
		if (aiAgent == null || aiAgent.getModel() == null
				|| StringUtils.isBlank(aiAgent.getModel().getModelCode())) {
			throw new ServiceException("智能体未配置模型");
		}
		return AiModelUtils.getModelOrThrow(aiAgent.getModel().getModelCode());
	}

	/**
	 * 按智能体构建 ChatClient（模型、系统提示词、工具、知识库检索）
	 * @param aiAgent 智能体
	 */
	public ChatClient getChatClient(AiAgent aiAgent) {
		return getChatClient(aiAgent, null);
	}

	/**
	 * 按智能体构建 ChatClient，并绑定本轮运行的事件流
	 * @param aiAgent 智能体
	 * @param runId 本轮对话运行编号（工具调用事件按它回传，为空则不推送事件）
	 */
	public ChatClient getChatClient(AiAgent aiAgent, String runId) {
		ChatClient.Builder builder = AiModelUtils.chatClientBuilder(getAgentModel(aiAgent));
		String systemPrompt = StringUtils.defaultString(aiAgent.getSystemPrompt());
		// 开启任务计划工具时追加引导：模型默认很少主动调用 todo_write，不引导则任务清单永远是空的
		if (isTodoWriteEnabled(aiAgent)) {
			systemPrompt = systemPrompt + TODO_WRITE_PROMPT;
		}
		// 流式对话注册了提问工具，追加引导，避免模型在方向不明时直接猜测执行
		if (StringUtils.isNotBlank(runId)) {
			systemPrompt = systemPrompt + ASK_USER_PROMPT;
		}
		if (StringUtils.isNotBlank(systemPrompt)) {
			builder.defaultSystem(systemPrompt);
		}
		List<ToolCallback> toolCallbacks = getAgentToolCallbacks(aiAgent, runId);
		if (ListUtils.isNotEmpty(toolCallbacks)) {
			builder.defaultTools(toolCallbacks);
		}
		List<Advisor> advisors = getAgentAdvisors(aiAgent);
		if (ListUtils.isNotEmpty(advisors)) {
			builder.defaultAdvisors(advisors);
		}
		return builder.build();
	}

	/**
	 * 获取智能体绑定的工具回调（工具 + MCP 服务），并绑定本轮运行编号
	 * @param aiAgent 智能体
	 * @param runId 本轮对话运行编号（工具调用事件按它回传）
	 */
	public List<ToolCallback> getAgentToolCallbacks(AiAgent aiAgent, String runId) {
		List<ToolCallback> callbacks = ListUtils.newArrayList();
		// 工具名称统一使用工具编码（模型侧函数名），编码为主键，天然不会重名
		Set<String> usedNames = new HashSet<>();
		for (String toolCode : getAgentToolCodes(aiAgent)) {
			for (ToolCallback callback : AiToolUtils.getToolCallbacks(ListUtils.newArrayList(toolCode))) {
				addToolCallback(callbacks, usedNames, callback, toolCode, runId);
			}
		}
		// MCP 服务挂载的工具：按与同步一致的编码规则（mcpCode_工具名）命名，
		// 与工具列表中已选工具编码相同的自动跳过，避免同一工具重复注册
		for (String mcpCode : getAgentMcpCodes(aiAgent)) {
			try {
				for (ToolCallback callback : AiMcpUtils.getToolCallbacks(mcpCode)) {
					ToolDefinition definition = callback.getToolDefinition();
					if (definition == null || StringUtils.isBlank(definition.name())) {
						continue;
					}
					addToolCallback(callbacks, usedNames, callback,
							AiToolUtils.buildMcpToolCode(mcpCode, definition.name()), runId);
				}
			} catch (Exception e) {
				// MCP 服务异常不影响对话，记录日志后跳过
				logger.warn("MCP 服务加载失败：{}，{}", mcpCode, e.getMessage());
			}
		}
		// 任务计划工具：仅在流式对话（有 runId）时注册，事件随流下发，前端渲染任务进度
		if (StringUtils.isNotBlank(runId) && isTodoWriteEnabled(aiAgent)) {
			callbacks.add(new AiTodoWriteTool(runId));
		}
		// 任务方向确认工具：方向不明确时向用户提问，阻塞等待选择后继续
		if (StringUtils.isNotBlank(runId)) {
			callbacks.add(new AiAskUserTool(runId));
		}
		// 排查用：日志级别调为 debug 可确认本轮实际下发给模型的工具清单（含 todo_write 是否注册）
		if (logger.isDebugEnabled()) {
			List<String> toolNames = ListUtils.newArrayList();
			for (ToolCallback callback : callbacks) {
				if (callback.getToolDefinition() != null) {
					toolNames.add(callback.getToolDefinition().name());
				}
			}
			logger.debug("本轮下发给模型的工具（{} 个）：{}", toolNames.size(), toolNames);
		}
		return callbacks;
	}

	/**
	 * 是否开启任务计划工具（智能体选项 options.todoWrite）
	 * @param aiAgent 智能体
	 */
	protected boolean isTodoWriteEnabled(AiAgent aiAgent) {
		Map<String, Object> options = aiAgent == null ? null : aiAgent.getOptionMap();
		Object value = options == null ? null : options.get(AiTodoWriteTool.OPTION_ENABLED);
		if (value instanceof Boolean bool) {
			return bool;
		}
		return "true".equalsIgnoreCase(StringUtils.defaultString(value == null ? null : value.toString()));
	}

	/**
	 * 以工具编码作为模型侧函数名包装工具回调，并绑定本轮运行的事件流
	 * <p>
	 * 仅修改工具定义中的名称，执行仍委托原回调，因此不影响本地 Bean/HTTP 工具的调用逻辑，
	 * 也不影响 MCP 工具按远端原始名称调用；工具编码相同即同一工具，重复的跳过。
	 * @param callbacks 回调集合
	 * @param usedNames 已注册的工具名称（即工具编码）
	 * @param callback 工具回调
	 * @param toolCode 工具编码
	 * @param runId 本轮对话运行编号（为空则只变更名称、不推送事件）
	 */
	protected void addToolCallback(List<ToolCallback> callbacks, Set<String> usedNames,
			ToolCallback callback, String toolCode, String runId) {
		if (callback.getToolDefinition() == null) {
			return;
		}
		String toolName = AiToolCallbackWrapper.normalizeName(toolCode);
		if (StringUtils.isBlank(toolName) || !usedNames.add(toolName)) {
			// 名称为空或同一工具（编码相同）已注册，跳过
			return;
		}
		callbacks.add(new AiToolCallbackWrapper(callback, toolName, runId));
	}

	/**
	 * 获取智能体关联的工具编码（关联表 ai_agent_tool）
	 * @param aiAgent 智能体
	 */
	public List<String> getAgentToolCodes(AiAgent aiAgent) {
		if (aiAgent == null) {
			return ListUtils.newArrayList();
		}
		return aiAgentToolService.findToolCodes(aiAgent.getId());
	}

	/**
	 * 获取智能体关联的 MCP 服务编码（关联表 ai_agent_mcp）
	 * @param aiAgent 智能体
	 */
	public List<String> getAgentMcpCodes(AiAgent aiAgent) {
		if (aiAgent == null) {
			return ListUtils.newArrayList();
		}
		return aiAgentMcpService.findMcpCodes(aiAgent.getId());
	}

	/**
	 * 获取智能体关联的向量库业务类型（关联表 ai_agent_vector）
	 * @param aiAgent 智能体
	 */
	public List<String> getAgentVectorBizTypes(AiAgent aiAgent) {
		if (aiAgent == null) {
			return ListUtils.newArrayList();
		}
		return aiAgentVectorService.findVectorBizTypes(aiAgent.getId());
	}

	/**
	 * 获取智能体关联的向量库业务配置（仅状态正常的配置，顺序与选择顺序一致）
	 * @param aiAgent 智能体
	 */
	public List<AiVectorBiz> getAgentVectorBizList(AiAgent aiAgent) {
		List<String> vectorBizTypes = getAgentVectorBizTypes(aiAgent);
		List<AiVectorBiz> result = ListUtils.newArrayList();
		if (ListUtils.isEmpty(vectorBizTypes)) {
			return result;
		}
		Map<String, AiVectorBiz> vectorBizMap = MapUtils.newHashMap();
		AiVectorBiz where = new AiVectorBiz();
		where.setStatus(AiVectorBiz.STATUS_NORMAL);
		where.sqlMap().getWhere().and("biz_type", QueryType.IN, vectorBizTypes);
		for (AiVectorBiz vectorBiz : AiVectorUtils.getAiVectorBizService().findList(where)) {
			vectorBizMap.put(vectorBiz.getBizType(), vectorBiz);
		}
		for (String vectorBizType : vectorBizTypes) {
			AiVectorBiz vectorBiz = vectorBizMap.get(vectorBizType);
			if (vectorBiz != null) {
				result.add(vectorBiz);
			}
		}
		return result;
	}

	/**
	 * 填充智能体关联的工具、MCP 服务与向量库绑定（表单回显）
	 * @param aiAgent 智能体
	 */
	public void fillRelations(AiAgent aiAgent) {
		if (aiAgent == null) {
			return;
		}
		List<String> toolCodes = getAgentToolCodes(aiAgent);
		List<String> mcpCodes = getAgentMcpCodes(aiAgent);
		List<String> vectorBizTypes = getAgentVectorBizTypes(aiAgent);
		aiAgent.setToolCodes(String.join(",", toolCodes));
		aiAgent.setMcpCodes(String.join(",", mcpCodes));
		aiAgent.setVectorBizTypes(String.join(",", vectorBizTypes));
		// 表单用于展示已选名称，编码与名称顺序一一对应（一次查询取回全部名称，避免逐条查询）
		Map<String, String> toolNameMap = getToolNameMap(toolCodes);
		List<String> names = ListUtils.newArrayList();
		for (String toolCode : toolCodes) {
			names.add(StringUtils.defaultIfBlank(toolNameMap.get(toolCode), toolCode));
		}
		aiAgent.setToolNames(String.join(",", names));
		Map<String, String> mcpNameMap = getMcpNameMap(mcpCodes);
		names = ListUtils.newArrayList();
		for (String mcpCode : mcpCodes) {
			names.add(StringUtils.defaultIfBlank(mcpNameMap.get(mcpCode), mcpCode));
		}
		aiAgent.setMcpNames(String.join(",", names));
		Map<String, String> vectorBizNameMap = getVectorBizNameMap(vectorBizTypes);
		names = ListUtils.newArrayList();
		for (String vectorBizType : vectorBizTypes) {
			names.add(StringUtils.defaultIfBlank(vectorBizNameMap.get(vectorBizType), vectorBizType));
		}
		aiAgent.setVectorBizNames(String.join(",", names));
	}

	/**
	 * 批量查询工具名称（编码 -> 名称）
	 * @param toolCodes 工具编码集合
	 */
	protected Map<String, String> getToolNameMap(List<String> toolCodes) {
		Map<String, String> nameMap = MapUtils.newHashMap();
		if (ListUtils.isEmpty(toolCodes)) {
			return nameMap;
		}
		AiTool where = new AiTool();
		where.sqlMap().getWhere().and("tool_code", QueryType.IN, toolCodes);
		for (AiTool aiTool : AiToolUtils.findList(where)) {
			nameMap.put(aiTool.getToolCode(), aiTool.getToolName());
		}
		return nameMap;
	}

	/**
	 * 批量查询 MCP 名称（编码 -> 名称）
	 * @param mcpCodes MCP 编码集合
	 */
	protected Map<String, String> getMcpNameMap(List<String> mcpCodes) {
		Map<String, String> nameMap = MapUtils.newHashMap();
		if (ListUtils.isEmpty(mcpCodes)) {
			return nameMap;
		}
		AiMcp where = new AiMcp();
		where.sqlMap().getWhere().and("mcp_code", QueryType.IN, mcpCodes);
		for (AiMcp aiMcp : AiMcpUtils.findList(where)) {
			nameMap.put(aiMcp.getMcpCode(), aiMcp.getMcpName());
		}
		return nameMap;
	}

	/**
	 * 批量查询向量库业务名称（业务类型 -> 业务名称）
	 * @param vectorBizTypes 向量库业务类型集合
	 */
	protected Map<String, String> getVectorBizNameMap(List<String> vectorBizTypes) {
		Map<String, String> nameMap = MapUtils.newHashMap();
		if (ListUtils.isEmpty(vectorBizTypes)) {
			return nameMap;
		}
		AiVectorBiz where = new AiVectorBiz();
		where.sqlMap().getWhere().and("biz_type", QueryType.IN, vectorBizTypes);
		for (AiVectorBiz vectorBiz : AiVectorUtils.getAiVectorBizService().findList(where)) {
			nameMap.put(vectorBiz.getBizType(),
					StringUtils.defaultIfBlank(vectorBiz.getBizName(), vectorBiz.getBizType()));
		}
		return nameMap;
	}

	/**
	 * 拆分逗号分隔的编码串（表单提交的关联工具/MCP）
	 * @param codes 逗号分隔的编码串
	 */
	protected List<String> splitCodes(String codes) {
		List<String> result = ListUtils.newArrayList();
		if (StringUtils.isBlank(codes)) {
			return result;
		}
		for (String code : StringUtils.split(codes, ",")) {
			String text = StringUtils.trimToNull(code);
			if (text != null) {
				result.add(text);
			}
		}
		return result;
	}

	/**
	 * 获取知识库检索增强（按绑定的向量库生成 RAG 顾问）
	 * <p>
	 * 检索条数、相似度阈值、向量模型均取关联表对应的向量库绑定配置。
	 * @param aiAgent 智能体
	 */
	public List<Advisor> getAgentAdvisors(AiAgent aiAgent) {
		List<Advisor> advisors = ListUtils.newArrayList();
		for (AiVectorBiz vectorBiz : getAgentVectorBizList(aiAgent)) {
			try {
				VectorStore vectorStore = AiVectorUtils.getVectorStore(vectorBiz);
				advisors.add(buildVectorAdvisor(aiAgent, vectorStore, buildSearchRequest(vectorBiz)));
			} catch (Exception e) {
				logger.warn("知识库加载失败：{}（{}），{}", vectorBiz.getBizType(),
						vectorBiz.getVectorCode(), e.getMessage());
			}
		}
		return advisors;
	}

	/**
	 * 按向量库绑定的配置构建检索请求（未配置时取推荐默认值）
	 * <p>
	 * 强制附加业务类型（bizType）过滤：同一索引（集合/表）可能被多个业务共用，
	 * 不隔离会把其它业务的数据一起检索出来，导致回答串联到无关内容。
	 * @param bind 向量库绑定
	 */
	protected SearchRequest buildSearchRequest(AiVectorBiz bind) {
		Integer topK = bind.getTopK();
		Double threshold = bind.getSimilarityThreshold();
		SearchRequest.Builder builder = SearchRequest.builder()
				.topK(topK != null && topK > 0 ? topK : AiVectorBiz.DEFAULT_TOP_K)
				.similarityThreshold(threshold != null ? threshold.floatValue()
						: AiVectorBiz.DEFAULT_SIMILARITY_THRESHOLD.floatValue());
		String filterExpression = AiVectorUtils.buildFilterExpression("bizType", bind.getBizType(), null);
		if (StringUtils.isNotBlank(filterExpression)) {
			builder.filterExpression(filterExpression);
		}
		return builder.build();
	}

	/**
	 * 构建知识库问答顾问（提示词模板取 options.qaPromptTemplate，未配置时使用默认模板）
	 * @param aiAgent 智能体
	 * @param vectorStore 向量库
	 * @param searchRequest 检索参数
	 */
	protected QuestionAnswerAdvisor buildVectorAdvisor(AiAgent aiAgent, VectorStore vectorStore,
			SearchRequest searchRequest) {
		return QuestionAnswerAdvisor.builder(vectorStore)
				.searchRequest(searchRequest)
				.promptTemplate(new PromptTemplate(getQaPromptTemplate(aiAgent)))
				.build();
	}

	/**
	 * 获取知识库问答提示词模板（未配置或缺少检索资料占位符时回退默认模板）
	 * @param aiAgent 智能体
	 */
	protected String getQaPromptTemplate(AiAgent aiAgent) {
		Map<String, Object> options = aiAgent == null ? null : aiAgent.getOptionMap();
		String template = options == null ? null : ObjectUtils.toString(options.get("qaPromptTemplate"));
		if (StringUtils.isBlank(template)) {
			return DEFAULT_QA_PROMPT_TEMPLATE;
		}
		if (!template.contains("{question_answer_context}")) {
			logger.warn("智能体 {} 的知识库提示词缺少 question_answer_context 占位符，已使用默认模板",
					aiAgent.getId());
			return DEFAULT_QA_PROMPT_TEMPLATE;
		}
		return template;
	}

	/**
	 * 获取单条数据
	 * @param aiAgent 主键
	 */
	@Override
	public AiAgent get(AiAgent aiAgent) {
		return super.get(aiAgent);
	}

	/**
	 * 查询列表数据
	 * @param aiAgent 查询条件
	 */
	@Override
	public List<AiAgent> findList(AiAgent aiAgent) {
		return super.findList(aiAgent);
	}

	/**
	 * 查询分页数据
	 * @param aiAgent 查询条件
	 * @param aiAgent page 分页对象
	 */
	@Override
	public Page<AiAgent> findPage(AiAgent aiAgent) {
		return super.findPage(aiAgent);
	}

	/**
	 * 保存数据（插入或更新）
	 * @param aiAgent 数据对象
	 */
	@Override
	@Transactional
	public void save(AiAgent aiAgent) {
		// 表单选择的关联工具、MCP、向量库（逗号分隔）：保存后重建关联表
		List<String> toolCodes = splitCodes(aiAgent.getToolCodes());
		List<String> mcpCodes = splitCodes(aiAgent.getMcpCodes());
		List<String> vectorBizTypes = splitCodes(aiAgent.getVectorBizTypes());
		super.save(aiAgent);
		// 新增时主键在插入时生成，关联表须在保存之后写入
		if (aiAgent.getToolCodes() != null) {
			aiAgentToolService.saveAgentTools(aiAgent.getId(), toolCodes);
		}
		if (aiAgent.getMcpCodes() != null) {
			aiAgentMcpService.saveAgentMcp(aiAgent.getId(), mcpCodes);
		}
		if (aiAgent.getVectorBizTypes() != null) {
			aiAgentVectorService.saveAgentVectors(aiAgent.getId(), vectorBizTypes);
		}
	}

	/**
	 * 更新状态
	 * @param aiAgent 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(AiAgent aiAgent) {
		super.updateStatus(aiAgent);
	}

	/**
	 * 删除数据
	 * @param aiAgent 数据对象
	 */
	@Override
	@Transactional
	public void delete(AiAgent aiAgent) {
		super.delete(aiAgent);
		// 智能体删除后，关联的工具、MCP 服务与向量库绑定一并清理
		aiAgentToolService.deleteByAgentId(aiAgent.getId());
		aiAgentMcpService.deleteByAgentId(aiAgent.getId());
		aiAgentVectorService.deleteByAgentId(aiAgent.getId());
	}

}
