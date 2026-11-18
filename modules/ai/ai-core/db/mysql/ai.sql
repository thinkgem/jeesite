SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- AI智能体
CREATE TABLE js_ai_agent
(
	id varchar(64) NOT NULL COMMENT '编号',
	agent_name varchar(100) COMMENT '智能体名称',
	agent_avatar varchar(100) COMMENT '智能体头像',
	agent_intro varchar(100) COMMENT '智能体简介',
	model_code varchar(64) COMMENT '模型编码',
	system_prompt text COMMENT '系统提示词',
	welcome varchar(200) COMMENT '欢迎语',
	problem varchar(500) COMMENT '预设问题',
	shortcut varchar(500) COMMENT '快捷指令',
	placeholder varchar(200) COMMENT '输入框提示',
	options longtext COMMENT '配置选项（JSON）',
	weight decimal(10) COMMENT '权重',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (id)
) COMMENT = 'AI智能体';


-- 智能体与MCP服务关联表
CREATE TABLE js_ai_agent_mcp
(
	agent_id varchar(64) NOT NULL COMMENT '智能体编号',
	mcp_code varchar(64) NOT NULL COMMENT 'MCP编码',
	PRIMARY KEY (agent_id, mcp_code)
) COMMENT = '智能体与MCP服务关联表';


-- 智能体与工具关联表
CREATE TABLE js_ai_agent_tool
(
	agent_id varchar(64) NOT NULL COMMENT '智能体编号',
	tool_code varchar(64) NOT NULL COMMENT '工具编码',
	PRIMARY KEY (agent_id, tool_code)
) COMMENT = '智能体与工具关联表';


-- 智能体与向量库关联表
CREATE TABLE js_ai_agent_vector
(
	agent_id varchar(64) NOT NULL COMMENT '智能体编号',
	vector_biz_type varchar(64) NOT NULL COMMENT '业务类型',
	CONSTRAINT pk_ai_agent_vector PRIMARY KEY (agent_id, vector_biz_type)
) COMMENT = '智能体与向量库关联表';


-- AI聊天对话
CREATE TABLE js_ai_chat
(
	id varchar(64) NOT NULL COMMENT '编号',
	agent_id varchar(64) COMMENT '智能体编码',
	chat_title varchar(100) COMMENT '对话标题',
	chat_type varchar(32) COMMENT '对话类型（text、image）',
	weight decimal(10) COMMENT '权重',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	CONSTRAINT pk_ai_chat PRIMARY KEY (id)
) COMMENT = 'AI聊天对话';


-- AI聊天消息内容
CREATE TABLE js_ai_chat_message
(
	id varchar(64) NOT NULL COMMENT '编号',
	conv_id varchar(64) NOT NULL COMMENT '对话编号',
	parent_id varchar(64) COMMENT '回复的消息编号',
	msg_type varchar(100) COMMENT '消息类型',
	msg_text longtext COMMENT '消息内容',
	msg_think longtext COMMENT '思考内容',
	msg_metadata longtext COMMENT '消息元数据',
	total_tokens decimal(10) COMMENT '消耗Token数',
	total_cost decimal(20,6) COMMENT '消耗金额',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	model_code varchar(64) COMMENT '模型编码',
	CONSTRAINT pk_ai_chat_message PRIMARY KEY (id)
) COMMENT = 'AI聊天消息内容';


-- AI MCP服务表
CREATE TABLE js_ai_mcp
(
	mcp_code varchar(64) NOT NULL COMMENT 'MCP编码',
	mcp_name varchar(100) NOT NULL COMMENT 'MCP名称',
	mcp_desc varchar(500) COMMENT 'MCP描述',
	mcp_type varchar(32) NOT NULL COMMENT '传输类型（stdio、sse、streamable_http）',
	mcp_url varchar(255) COMMENT '服务地址',
	options longtext COMMENT '配置选项（JSON）',
	weight decimal(10) COMMENT '权重',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (mcp_code)
) COMMENT = 'AI MCP服务表';


-- AI模型注册
CREATE TABLE js_ai_model
(
	model_code varchar(64) NOT NULL COMMENT '模型编码',
	model_name varchar(100) NOT NULL COMMENT '模型名称',
	model_provider varchar(32) NOT NULL COMMENT '模型提供商',
	model_icon varchar(200) COMMENT '模型图标',
	model_type varchar(32) NOT NULL COMMENT '模型类型',
	model_desc varchar(500) COMMENT '模型描述',
	base_url varchar(255) COMMENT '接口地址',
	api_key varchar(255) COMMENT '接口Key',
	options longtext COMMENT '配置选项（JSON）',
	weight decimal(10) NOT NULL COMMENT '权重',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (model_code)
) COMMENT = 'AI模型注册';


-- AI工具注册表
CREATE TABLE js_ai_tool
(
	tool_code varchar(64) NOT NULL COMMENT '工具编码',
	tool_name varchar(200) NOT NULL COMMENT '工具名称（@Tool name / MCP 远端工具名）',
	tool_desc varchar(500) COMMENT '工具描述',
	tool_type varchar(32) NOT NULL COMMENT '工具类型（bean、mcp、http）',
	tool_bean varchar(255) COMMENT '工具实现Bean名称',
	mcp_code varchar(64) COMMENT '所属MCP编码',
	tool_url varchar(255) COMMENT '请求地址',
	tool_method varchar(10) COMMENT '请求方式（GET、POST）',
	tool_schema longtext COMMENT '参数定义（JSON Schema）',
	options longtext COMMENT '配置选项（JSON）',
	weight decimal(10) COMMENT '权重',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	CONSTRAINT pk_ai_tool PRIMARY KEY (tool_code)
) COMMENT = 'AI工具注册表';


-- AI向量库
CREATE TABLE js_ai_vector
(
	vector_code varchar(64) NOT NULL COMMENT '向量库编码',
	vector_name varchar(100) COMMENT '向量库名称',
	vector_desc varchar(500) COMMENT '向量库描述',
	vector_provider varchar(64) COMMENT '向量库提供商',
	base_url varchar(255) COMMENT '服务地址',
	username varchar(100) COMMENT '用户名',
	password varchar(255) COMMENT '密码或密钥',
	options longtext COMMENT '配置选项（JSON）',
	weight decimal(10) NOT NULL COMMENT '权重',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	CONSTRAINT pk_ai_vector PRIMARY KEY (vector_code)
) COMMENT = 'AI向量库';


-- AI向量库业务配置
CREATE TABLE js_ai_vector_biz
(
	biz_type varchar(64) NOT NULL COMMENT '业务类型',
	biz_name varchar(100) NOT NULL COMMENT '业务名称',
	vector_code varchar(64) NOT NULL COMMENT '向量库编码',
	model_code varchar(64) NOT NULL COMMENT '向量模型编码',
	index_name varchar(100) NOT NULL COMMENT '索引名称',
	dimensions decimal(10) NOT NULL COMMENT '向量维度',
	top_k decimal(10) NOT NULL COMMENT '检索条数',
	similarity_threshold decimal(4,2) NOT NULL COMMENT '相似度阈值',
	options longtext COMMENT '配置选项（JSON）',
	weight decimal(10) NOT NULL COMMENT '权重',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	CONSTRAINT pk_ai_vector_biz PRIMARY KEY (biz_type)
) COMMENT = 'AI向量库业务配置';



/* Create Indexes */

CREATE INDEX idx_ai_vector_biz_vc ON js_ai_vector_biz (vector_code ASC);
CREATE INDEX idx_ai_vector_biz_mc ON js_ai_vector_biz (model_code ASC);



