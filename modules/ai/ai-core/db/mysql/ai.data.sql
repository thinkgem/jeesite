-- ----------------------------------------------------------------------------
-- AI 模块初始演示数据（MySQL）
-- 说明：
--   1. 需先执行同目录 ai.sql 建表；本脚本使用 REPLACE INTO，可重复执行。
--   2. 模型的 api_key 为占位值，请在「AI模型」页面替换为真实 Key 后再使用。
--   3. corp_code / corp_name 取默认租户，多租户环境请按实际租户替换。
--   4. 字典（ai_model_provider、ai_model_type、ai_tool_type、ai_tool_method、
--      ai_mcp_type、ai_vector_provider、ai_vector_biz_type、ai_knowledge_type）
--      需在「字典管理」中已配置，否则页面下拉为空；
--      ai_vector_biz_type（向量库业务类型）建议值：filemanager=文件管理、cms=内容管理，
--      与业务代码中 getByBizType(...) 传入的类型保持一致。
--   5. 涉及的外部地址（api.example.com、mcp.example.com）为演示占位，
--      工具/MCP 的“测试”按钮会连接失败，属预期。
-- ----------------------------------------------------------------------------

SET NAMES utf8mb4;

-- ============================================================================
-- 1. AI 模型
-- ============================================================================
REPLACE INTO js_ai_model
(model_code, model_name, model_provider, model_icon, model_type, model_desc,
 base_url, api_key, options, weight, status, create_by, create_date, update_by, update_date,
 remarks, corp_code, corp_name)
VALUES
('deepseek-chat', 'DeepSeek V3', 'deepseek', 'i-simple-icons:deepseek', 'chat',
 'DeepSeek 通用对话模型，适合中文问答与工具调用',
 'https://api.deepseek.com', 'sk-请替换为真实Key',
 '{"chat":{"model":"deepseek-chat","temperature":0.7}}',
 10, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite'),

('qwen-plus', '通义千问 Plus', 'alibaba', 'i-simple-icons:alibabacloud', 'chat',
 '通义千问 Plus，支持长上下文',
 'https://dashscope.aliyuncs.com/compatible-mode', 'sk-请替换为真实Key',
 '{"chat":{"model":"qwen-plus","temperature":0.7}}',
 20, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite'),

('text-embedding-v3', '通义文本向量 V3', 'alibaba', 'i-simple-icons:alibabacloud', 'embedding',
 '文本向量模型，供知识库切片向量化与检索使用',
 'https://dashscope.aliyuncs.com/compatible-mode', 'sk-请替换为真实Key',
 '{"embedding":{"model":"text-embedding-v3","dimensions":1024}}',
 30, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite');


-- ============================================================================
-- 2. AI 向量库
--    simple：内存向量库，无需外部依赖，适合快速演示（重启后需重建）
--    pgvector：需要 PostgreSQL + pgvector 扩展
-- ============================================================================
REPLACE INTO js_ai_vector
(vector_code, vector_name, vector_desc, vector_provider, options, weight,
 status, create_by, create_date, update_by, update_date, remarks, corp_code, corp_name,
 base_url, username, password)
VALUES
('kb-vector', '本地演示向量库', 'SimpleVectorStore 内存实现，开箱即用，适合功能演示', 'memory',
 '{}', 10, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite',
 NULL, NULL, NULL),

('kb-pgvector', 'PostgreSQL 向量库', 'pgvector 实现，需先执行 CREATE EXTENSION vector', 'pgvector',
 '{"initializeSchema":true}', 20, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite',
 'jdbc:postgresql://127.0.0.1:5432/jeesite', 'postgres', 'postgres');


-- ============================================================================
-- 3. AI 工具
--    bean   ：对应 Spring Bean（ai-tools 模块提供 aiTestAiTools、aiUserTools、aiImageTools）
--    http   ：按 tool_schema 生成参数，调用 tool_url
--    mcp    ：来自 MCP 服务，tool_name 为远端工具名
-- ============================================================================
REPLACE INTO js_ai_tool
(tool_code, tool_name, tool_desc, tool_type, tool_method, tool_url, tool_schema,
 tool_bean, mcp_code, options, weight, status,
 create_by, create_date, update_by, update_date, remarks, corp_code, corp_name)
VALUES
('get_weather', '天气查询', '根据城市名称查询当前天气。当用户询问天气、气温、是否下雨时调用。',
 'http', 'GET', 'https://api.example.com/v1/weather',
 '{"type":"object","properties":{"city":{"type":"string","description":"城市名称，如：北京"}},"required":["city"]}',
 NULL, NULL,
 '{"headers":{"X-Api-Key":"demo-key"},"timeout":15}',
 10, '0', 'admin', NOW(), 'admin', NOW(), '演示数据（地址为占位）', '0', 'JeeSite'),

('get_current_time', 'server_time', '获取服务器当前日期与时间。当用户询问今天日期、现在几点、星期几时调用。',
 'bean', NULL, NULL, NULL,
 'aiTestAiTools', NULL, NULL,
 20, '0', 'admin', NOW(), 'admin', NOW(), '演示数据（ai-tools 模块 LocalTestAiTools）', '0', 'JeeSite'),

('current_user', 'current_user', '获取当前登录用户的基本信息，包含用户名、姓名、部门等。当用户询问“我是谁”、当前账号信息时调用。',
 'bean', NULL, NULL, NULL,
 'aiUserTools', NULL, NULL,
 21, '0', 'admin', NOW(), 'admin', NOW(), '演示数据（ai-tools 模块 LocalUserAiTools）', '0', 'JeeSite'),

('find_user_info', 'find_user_info', '根据登录名（用户名）或员工姓名模糊查询用户信息，结果包含用户名、姓名、部门等。',
 'bean', NULL, NULL, NULL,
 'aiUserTools', NULL, NULL,
 22, '0', 'admin', NOW(), 'admin', NOW(), '演示数据（ai-tools 模块 LocalUserAiTools）', '0', 'JeeSite'),

('mcp_demo_echo', 'echo', '由演示 MCP 服务提供的回显工具，用于验证 MCP 工具调用链路。',
 'mcp', NULL, NULL, NULL,
 NULL, 'demo-mcp', NULL,
 30, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite');


-- ============================================================================
-- 4. AI MCP 服务
-- ============================================================================
REPLACE INTO js_ai_mcp
(mcp_code, mcp_name, mcp_desc, mcp_type, mcp_url, options, weight, status,
 create_by, create_date, update_by, update_date, remarks, corp_code, corp_name)
VALUES
('demo-mcp', '演示 MCP 服务', 'SSE 方式连接的示例 MCP 服务，地址为占位', 'sse',
 'https://mcp.example.com', '{"endpoint":"/sse","timeout":60}', 10, '0',
 'admin', NOW(), 'admin', NOW(), '演示数据（地址为占位）', '0', 'JeeSite');


-- ============================================================================
-- 5. AI 知识库 + 知识库文件
--    biz_type = 'ai_knowledge'，biz_key = 知识库编号
-- ============================================================================
REPLACE INTO js_ai_knowledge
(id, office_code, know_title, know_type, know_desc, know_icon, vector_code, model_code, weight,
 vector_status, chunk_count, result_msg, status,
 create_by, create_date, update_by, update_date, remarks, corp_code, corp_name)
VALUES
('kb_product', NULL, '产品使用手册', '1', 'JeeSite 产品操作说明、功能讲解与最佳实践文档。',
 'i-ant-design:book-outlined', 'kb-vector', 'text-embedding-v3', 10,
 '2', 128, '向量化完成（演示数据）', '0',
 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite'),

('kb_faq', NULL, '常见问题解答', '2', '售前售后、部署运维等常见问题与解答。',
 'i-ant-design:question-circle-outlined', 'kb-vector', 'text-embedding-v3', 20,
 '2', 64, '向量化完成（演示数据）', '0',
 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite');

REPLACE INTO js_ai_knowledge_upload
(id, file_id, file_name, file_type, file_sort, biz_key, biz_type, status,
 create_by, create_date, update_by, update_date, remarks, corp_code, corp_name)
VALUES
('kbup_001', 'demo_file_001', '产品使用手册V1.2.pdf', 'file', 1, 'kb_product', 'ai_knowledge', '0',
 'admin', NOW(), 'admin', NOW(), '演示数据（文件记录，附件可后补）', '0', 'JeeSite'),

('kbup_002', 'demo_file_002', '常见问题100问.docx', 'file', 2, 'kb_faq', 'ai_knowledge', '0',
 'admin', NOW(), 'admin', NOW(), '演示数据（文件记录，附件可后补）', '0', 'JeeSite');


-- ============================================================================
-- 6. AI 智能体 + 能力关联
--    options.tools   ：工具编码
--    options.mcp     ：MCP 服务编码
--    options.vectors ：知识库（向量库）编号
--    options.topK / similarityThreshold：检索条数与相似度阈值
-- ============================================================================
REPLACE INTO js_ai_agent
(id, agent_name, agent_avatar, agent_intro, model_code, system_prompt,
 welcome, problem, shortcut, placeholder, options, weight, status,
 create_by, create_date, update_by, update_date, remarks, corp_code, corp_name)
VALUES
('agent_assistant', '通用智能助手', 'i-ant-design:robot-outlined',
 '通用问答助手，支持工具调用与知识库检索',
 'deepseek-chat',
 '你是 JeeSite AI 通用助手。回答简洁准确；需要实时数据时优先调用可用工具；涉及产品文档内容时优先参考检索到的知识库资料，并在回答中标注来源。',
 '你好，我是通用智能助手，可以帮你查天气、查时间，也能回答产品相关问题。',
 '["北京今天天气怎么样？","现在几点了？","介绍一下产品手册里的核心功能"]',
 '["/天气 北京","/时间"]',
 '请输入你的问题，我会尽力为你解答…',
 '{"tools":["get_weather","get_current_time"],"mcp":["demo-mcp"],"vectors":["kb_product","kb_faq"],"topK":6,"similarityThreshold":0.6,"viewEnabled":"1"}',
 10, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite'),

('agent_kb', '文档问答助手', 'i-ant-design:file-search-outlined',
 '专注产品文档问答，答案严格基于知识库内容',
 'deepseek-chat',
 '你是产品文档问答助手。只依据检索到的知识库内容作答；检索不到相关内容时，直接说明“知识库中没有相关信息”，不要自行编造。',
 '你好，我可以基于知识库回答产品使用、部署、配置等问题。',
 '["如何配置多租户？","数据权限怎么用？"]',
 '["/文档 数据权限"]',
 '请输入你想了解的产品问题…',
 '{"vectors":["kb_product","kb_faq"],"topK":8,"similarityThreshold":0.5}',
 20, '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite');

REPLACE INTO js_ai_agent_tool (agent_id, tool_code) VALUES
('agent_assistant', 'get_weather'),
('agent_assistant', 'get_current_time'),
('agent_assistant', 'mcp_demo_echo');

REPLACE INTO js_ai_agent_mcp (agent_id, mcp_code) VALUES
('agent_assistant', 'demo-mcp');

REPLACE INTO js_ai_agent_knowledge (agent_id, knowledge_id) VALUES
('agent_assistant', 'kb_product'),
('agent_assistant', 'kb_faq'),
('agent_kb', 'kb_product'),
('agent_kb', 'kb_faq');


-- ============================================================================
-- 7. AI 会话与消息（演示历史对话）
-- ============================================================================
REPLACE INTO js_ai_chat
(id, agent_id, chat_title, chat_type, weight, status,
 create_by, create_date, update_by, update_date, remarks, corp_code, corp_name)
VALUES
('chat_demo_001', 'agent_assistant', '演示对话：天气与产品手册', 'text', 10, '0',
 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite');

REPLACE INTO js_ai_chat_message
(id, conv_id, parent_id, msg_type, msg_text, msg_think, msg_metadata, total_tokens, total_cost,
 status, create_by, create_date, update_by, update_date, remarks, corp_code, corp_name, model_code)
VALUES
('msg_demo_001', 'chat_demo_001', NULL, 'user',
 '北京今天天气怎么样？顺便说说产品手册里有哪些核心功能。', NULL, NULL, NULL, NULL,
 '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite', NULL),

('msg_demo_002', 'chat_demo_001', 'msg_demo_001', 'assistant',
 '北京今天多云转晴，气温 12~22℃，东南风 2 级，适合出行。\n\n产品手册中的核心功能包括：用户与组织权限、数据权限、工作流（BPM）、代码生成、多租户、消息推送与定时任务等，详细说明见《产品使用手册V1.2》。',
 NULL, '{"toolCall":"get_weather","knowledge":["kb_product"]}', 386, 0.000000,
 '0', 'admin', NOW(), 'admin', NOW(), '演示数据', '0', 'JeeSite', 'deepseek-chat');


-- ============================================================================
-- 附：bean（本地 Bean）工具配置说明
-- ----------------------------------------------------------------------------
-- 1. ai-tools 模块已提供以下 Bean（类上 @AiTools("Bean名")，方法上 @Tool）：
--      aiTestAiTools -> server_time、root_light_switch
--      aiUserTools   -> current_user、find_user_info
--      aiImageTools  -> generate_image（需装载 ImageGenerateService 实现才可用）
--    配置对应关系：tool_bean = Bean 名，tool_name = @Tool name，tool_type = bean。
--    命名规则：工具名（@Tool name、tool_name）只允许字母、数字、下划线、减号，长度 1-200；
--              description 以界面「工具描述」优先，为空时用注解描述。
-- 2. 自定义工具：写一个 Spring Bean（推荐用 @AiTools("beanName") 指定 Bean 名），
--    方法加 @Tool(name = "xxx", description = "调用时机说明")，参数加 @ToolParam 描述：
--      @AiTools("aiDemoTool")
--      public class AiDemoTool {
--          @Tool(name = "get_current_time", description = "获取服务器当前日期与时间")
--          public String getCurrentTime() {
--              return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
--          }
--      }
--    再在「工具管理」新建：工具类型 = bean，实现Bean名称 = aiDemoTool，
--    工具名称 = get_current_time（必填，一个工具一条记录）。
-- ============================================================================
