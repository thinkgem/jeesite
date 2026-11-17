

/* Create Tables */

-- AI智能体
CREATE TABLE js_ai_agent
(
	id varchar(64) NOT NULL,
	agent_name varchar(100),
	agent_avatar varchar(100),
	agent_intro varchar(100),
	model_code varchar(64),
	system_prompt clob,
	welcome varchar(200),
	problem varchar(500),
	shortcut varchar(500),
	placeholder varchar(200),
	options clob,
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);


-- 智能体与知识库关联表
CREATE TABLE js_ai_agent_knowledge
(
	agent_id varchar(64) NOT NULL,
	knowledge_id varchar(64) NOT NULL,
	PRIMARY KEY (agent_id, knowledge_id)
);


-- 智能体与MCP服务关联表
CREATE TABLE js_ai_agent_mcp
(
	agent_id varchar(64) NOT NULL,
	mcp_code varchar(64) NOT NULL,
	PRIMARY KEY (agent_id, mcp_code)
);


-- 智能体与工具关联表
CREATE TABLE js_ai_agent_tool
(
	agent_id varchar(64) NOT NULL,
	tool_code varchar(64) NOT NULL,
	PRIMARY KEY (agent_id, tool_code)
);


-- 智能体与向量库绑定关联表
CREATE TABLE js_ai_agent_vector
(
	agent_id varchar(64) NOT NULL,
	vector_biz_type varchar(64) NOT NULL,
	PRIMARY KEY (agent_id, vector_biz_type)
);


-- AI聊天对话
CREATE TABLE js_ai_chat
(
	id varchar(64) NOT NULL,
	agent_id varchar(64),
	chat_title varchar(100),
	chat_type varchar(32),
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	CONSTRAINT pk_ai_chat PRIMARY KEY (id)
);


-- AI聊天消息内容
CREATE TABLE js_ai_chat_message
(
	id varchar(64) NOT NULL,
	conv_id varchar(64) NOT NULL,
	parent_id varchar(64),
	msg_type varchar(100),
	msg_text clob,
	msg_think clob,
	msg_metadata clob,
	total_tokens decimal(10),
	total_cost decimal(20,6),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	model_code varchar(64),
	CONSTRAINT pk_ai_chat_message PRIMARY KEY (id)
);


-- AI知识库
CREATE TABLE js_ai_knowledge
(
	id varchar(64) NOT NULL,
	office_code varchar(64),
	know_title varchar(100) NOT NULL,
	know_type char(1),
	know_desc varchar(500),
	know_icon varchar(100),
	vector_code varchar(64),
	model_code varchar(64),
	weight decimal(10) DEFAULT 0,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	vector_status char(1) DEFAULT '0',
	chunk_count decimal(10),
	result_msg varchar(500),
	CONSTRAINT pk_ai_knowledge PRIMARY KEY (id)
);


-- AI知识库文件上传表
CREATE TABLE js_ai_knowledge_upload
(
	id varchar(64) NOT NULL,
	file_id varchar(64) NOT NULL,
	file_name varchar(500) NOT NULL,
	file_type varchar(20) NOT NULL,
	file_sort decimal(10),
	biz_key varchar(64),
	biz_type varchar(64),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	CONSTRAINT pk_ai_knowledge_upload PRIMARY KEY (id)
);


-- AI MCP服务表
CREATE TABLE js_ai_mcp
(
	mcp_code varchar(64) NOT NULL,
	mcp_name varchar(100) NOT NULL,
	mcp_desc varchar(500),
	mcp_type varchar(32) NOT NULL,
	mcp_url varchar(255),
	options clob,
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (mcp_code)
);


-- AI模型注册
CREATE TABLE js_ai_model
(
	model_code varchar(64) NOT NULL,
	model_name varchar(100) NOT NULL,
	model_provider varchar(32) NOT NULL,
	model_icon varchar(200),
	model_type varchar(32) NOT NULL,
	model_desc varchar(500),
	base_url varchar(255),
	api_key varchar(255),
	options clob,
	weight decimal(10) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (model_code)
);


-- AI工具注册表
CREATE TABLE js_ai_tool
(
	tool_code varchar(64) NOT NULL,
	tool_name varchar(200) NOT NULL,
	tool_desc varchar(500),
	tool_type varchar(32) NOT NULL,
	tool_method varchar(10),
	tool_url varchar(255),
	tool_schema clob,
	tool_bean varchar(255),
	mcp_code varchar(64),
	options clob,
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	CONSTRAINT pk_ai_tool PRIMARY KEY (tool_code)
);


-- AI向量库
CREATE TABLE js_ai_vector
(
	vector_code varchar(64) NOT NULL,
	vector_name varchar(100),
	vector_desc varchar(500),
	vector_provider varchar(64),
	options clob,
	weight decimal(10) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	base_url varchar(255),
	username varchar(100),
	password varchar(255),
	CONSTRAINT pk_ai_vector PRIMARY KEY (vector_code)
);


-- AI向量库业务配置
CREATE TABLE js_ai_vector_biz
(
	biz_type varchar(64) NOT NULL,
	biz_name varchar(100) NOT NULL,
	vector_code varchar(64) NOT NULL,
	model_code varchar(64) NOT NULL,
	index_name varchar(100) NOT NULL,
	dimensions decimal(10) NOT NULL,
	top_k decimal(10) NOT NULL,
	similarity_threshold decimal(4,2) NOT NULL,
	options clob,
	weight decimal(10) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	CONSTRAINT pk_ai_vector_biz PRIMARY KEY (biz_type)
);



/* Comments */

COMMENT ON TABLE js_ai_agent IS 'AI智能体';
COMMENT ON COLUMN js_ai_agent.id IS '编号';
COMMENT ON COLUMN js_ai_agent.agent_name IS '智能体名称';
COMMENT ON COLUMN js_ai_agent.agent_avatar IS '智能体头像';
COMMENT ON COLUMN js_ai_agent.agent_intro IS '智能体简介';
COMMENT ON COLUMN js_ai_agent.model_code IS '模型编码';
COMMENT ON COLUMN js_ai_agent.system_prompt IS '系统提示词';
COMMENT ON COLUMN js_ai_agent.welcome IS '欢迎语';
COMMENT ON COLUMN js_ai_agent.problem IS '预设问题';
COMMENT ON COLUMN js_ai_agent.shortcut IS '快捷指令';
COMMENT ON COLUMN js_ai_agent.placeholder IS '输入框提示';
COMMENT ON COLUMN js_ai_agent.options IS '配置选项（JSON）';
COMMENT ON COLUMN js_ai_agent.weight IS '权重';
COMMENT ON COLUMN js_ai_agent.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_agent.create_by IS '创建者';
COMMENT ON COLUMN js_ai_agent.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_agent.update_by IS '更新者';
COMMENT ON COLUMN js_ai_agent.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_agent.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_agent.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_agent.corp_name IS '租户名称';
COMMENT ON TABLE js_ai_agent_knowledge IS '智能体与知识库关联表';
COMMENT ON COLUMN js_ai_agent_knowledge.agent_id IS '智能体编号';
COMMENT ON COLUMN js_ai_agent_knowledge.knowledge_id IS '知识库编号';
COMMENT ON TABLE js_ai_agent_mcp IS '智能体与MCP服务关联表';
COMMENT ON COLUMN js_ai_agent_mcp.agent_id IS '智能体编号';
COMMENT ON COLUMN js_ai_agent_mcp.mcp_code IS 'MCP编码';
COMMENT ON TABLE js_ai_agent_tool IS '智能体与工具关联表';
COMMENT ON COLUMN js_ai_agent_tool.agent_id IS '智能体编号';
COMMENT ON COLUMN js_ai_agent_tool.tool_code IS '工具编码';

COMMENT ON TABLE js_ai_agent_vector IS '智能体与向量库绑定关联表';
COMMENT ON COLUMN js_ai_agent_vector.agent_id IS '智能体编号';
COMMENT ON COLUMN js_ai_agent_vector.vector_biz_type IS '向量库业务类型';
COMMENT ON TABLE js_ai_chat IS 'AI聊天对话';
COMMENT ON COLUMN js_ai_chat.id IS '编号';
COMMENT ON COLUMN js_ai_chat.agent_id IS '智能体编码';
COMMENT ON COLUMN js_ai_chat.chat_title IS '对话标题';
COMMENT ON COLUMN js_ai_chat.chat_type IS '对话类型（text、image）';
COMMENT ON COLUMN js_ai_chat.weight IS '权重';
COMMENT ON COLUMN js_ai_chat.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_chat.create_by IS '创建者';
COMMENT ON COLUMN js_ai_chat.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_chat.update_by IS '更新者';
COMMENT ON COLUMN js_ai_chat.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_chat.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_chat.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_chat.corp_name IS '租户名称';
COMMENT ON TABLE js_ai_chat_message IS 'AI聊天消息内容';
COMMENT ON COLUMN js_ai_chat_message.id IS '编号';
COMMENT ON COLUMN js_ai_chat_message.conv_id IS '对话编号';
COMMENT ON COLUMN js_ai_chat_message.parent_id IS '回复的消息编号';
COMMENT ON COLUMN js_ai_chat_message.msg_type IS '消息类型';
COMMENT ON COLUMN js_ai_chat_message.msg_text IS '消息内容';
COMMENT ON COLUMN js_ai_chat_message.msg_think IS '思考内容';
COMMENT ON COLUMN js_ai_chat_message.msg_metadata IS '消息元数据';
COMMENT ON COLUMN js_ai_chat_message.total_tokens IS '消耗Token数';
COMMENT ON COLUMN js_ai_chat_message.total_cost IS '消耗金额';
COMMENT ON COLUMN js_ai_chat_message.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_chat_message.create_by IS '创建者';
COMMENT ON COLUMN js_ai_chat_message.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_chat_message.update_by IS '更新者';
COMMENT ON COLUMN js_ai_chat_message.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_chat_message.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_chat_message.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_chat_message.corp_name IS '租户名称';
COMMENT ON COLUMN js_ai_chat_message.model_code IS '模型编码';
COMMENT ON TABLE js_ai_knowledge IS 'AI知识库';
COMMENT ON COLUMN js_ai_knowledge.id IS '编号';
COMMENT ON COLUMN js_ai_knowledge.office_code IS '部门编码';
COMMENT ON COLUMN js_ai_knowledge.know_title IS '知识库标题';
COMMENT ON COLUMN js_ai_knowledge.know_type IS '知识库类型';
COMMENT ON COLUMN js_ai_knowledge.know_desc IS '知识库描述';
COMMENT ON COLUMN js_ai_knowledge.know_icon IS '知识库图标';
COMMENT ON COLUMN js_ai_knowledge.vector_code IS '向量库编码';
COMMENT ON COLUMN js_ai_knowledge.model_code IS '向量模型编码';
COMMENT ON COLUMN js_ai_knowledge.weight IS '权重';
COMMENT ON COLUMN js_ai_knowledge.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_knowledge.create_by IS '创建者';
COMMENT ON COLUMN js_ai_knowledge.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_knowledge.update_by IS '更新者';
COMMENT ON COLUMN js_ai_knowledge.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_knowledge.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_knowledge.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_knowledge.corp_name IS '租户名称';
COMMENT ON COLUMN js_ai_knowledge.vector_status IS '向量化状态（0未处理 1处理中 2已完成 3失败）';
COMMENT ON COLUMN js_ai_knowledge.chunk_count IS '切片数量';
COMMENT ON COLUMN js_ai_knowledge.result_msg IS '处理结果信息';
COMMENT ON TABLE js_ai_knowledge_upload IS 'AI知识库文件上传表';
COMMENT ON COLUMN js_ai_knowledge_upload.id IS '编号';
COMMENT ON COLUMN js_ai_knowledge_upload.file_id IS '文件编号';
COMMENT ON COLUMN js_ai_knowledge_upload.file_name IS '文件名称';
COMMENT ON COLUMN js_ai_knowledge_upload.file_type IS '文件分类（image、media、file）';
COMMENT ON COLUMN js_ai_knowledge_upload.file_sort IS '文件排序（升序）';
COMMENT ON COLUMN js_ai_knowledge_upload.biz_key IS '业务主键';
COMMENT ON COLUMN js_ai_knowledge_upload.biz_type IS '业务类型';
COMMENT ON COLUMN js_ai_knowledge_upload.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_knowledge_upload.create_by IS '创建者';
COMMENT ON COLUMN js_ai_knowledge_upload.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_knowledge_upload.update_by IS '更新者';
COMMENT ON COLUMN js_ai_knowledge_upload.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_knowledge_upload.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_knowledge_upload.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_knowledge_upload.corp_name IS '租户名称';
COMMENT ON TABLE js_ai_mcp IS 'AI MCP服务表';
COMMENT ON COLUMN js_ai_mcp.mcp_code IS 'MCP编码';
COMMENT ON COLUMN js_ai_mcp.mcp_name IS 'MCP名称';
COMMENT ON COLUMN js_ai_mcp.mcp_desc IS 'MCP描述';
COMMENT ON COLUMN js_ai_mcp.mcp_type IS '传输类型（stdio、sse、streamable_http）';
COMMENT ON COLUMN js_ai_mcp.mcp_url IS '服务地址';
COMMENT ON COLUMN js_ai_mcp.options IS '配置选项（JSON）';
COMMENT ON COLUMN js_ai_mcp.weight IS '权重';
COMMENT ON COLUMN js_ai_mcp.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_mcp.create_by IS '创建者';
COMMENT ON COLUMN js_ai_mcp.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_mcp.update_by IS '更新者';
COMMENT ON COLUMN js_ai_mcp.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_mcp.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_mcp.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_mcp.corp_name IS '租户名称';
COMMENT ON TABLE js_ai_model IS 'AI模型注册';
COMMENT ON COLUMN js_ai_model.model_code IS '模型编码';
COMMENT ON COLUMN js_ai_model.model_name IS '模型名称';
COMMENT ON COLUMN js_ai_model.model_provider IS '模型提供商';
COMMENT ON COLUMN js_ai_model.model_icon IS '模型图标';
COMMENT ON COLUMN js_ai_model.model_type IS '模型类型';
COMMENT ON COLUMN js_ai_model.model_desc IS '模型描述';
COMMENT ON COLUMN js_ai_model.base_url IS '接口地址';
COMMENT ON COLUMN js_ai_model.api_key IS '接口Key';
COMMENT ON COLUMN js_ai_model.options IS '配置选项（JSON）';
COMMENT ON COLUMN js_ai_model.weight IS '权重';
COMMENT ON COLUMN js_ai_model.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_model.create_by IS '创建者';
COMMENT ON COLUMN js_ai_model.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_model.update_by IS '更新者';
COMMENT ON COLUMN js_ai_model.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_model.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_model.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_model.corp_name IS '租户名称';
COMMENT ON TABLE js_ai_tool IS 'AI工具注册表';
COMMENT ON COLUMN js_ai_tool.tool_code IS '工具编码';
COMMENT ON COLUMN js_ai_tool.tool_name IS '工具名称（@Tool name / MCP 远端工具名）';
COMMENT ON COLUMN js_ai_tool.tool_desc IS '工具描述';
COMMENT ON COLUMN js_ai_tool.tool_type IS '工具类型（bean、mcp、http）';
COMMENT ON COLUMN js_ai_tool.tool_method IS '请求方式（GET、POST）';
COMMENT ON COLUMN js_ai_tool.tool_url IS '请求地址';
COMMENT ON COLUMN js_ai_tool.tool_schema IS '参数定义（JSON Schema）';
COMMENT ON COLUMN js_ai_tool.tool_bean IS '工具实现Bean名称';
COMMENT ON COLUMN js_ai_tool.mcp_code IS '所属MCP编码';
COMMENT ON COLUMN js_ai_tool.options IS '配置选项（JSON）';
COMMENT ON COLUMN js_ai_tool.weight IS '权重';
COMMENT ON COLUMN js_ai_tool.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_tool.create_by IS '创建者';
COMMENT ON COLUMN js_ai_tool.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_tool.update_by IS '更新者';
COMMENT ON COLUMN js_ai_tool.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_tool.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_tool.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_tool.corp_name IS '租户名称';
COMMENT ON TABLE js_ai_vector IS 'AI向量库';
COMMENT ON COLUMN js_ai_vector.vector_code IS '向量库编码';
COMMENT ON COLUMN js_ai_vector.vector_name IS '向量库名称';
COMMENT ON COLUMN js_ai_vector.vector_desc IS '向量库描述';
COMMENT ON COLUMN js_ai_vector.vector_provider IS '向量库提供商';
COMMENT ON COLUMN js_ai_vector.options IS '配置选项（JSON）';
COMMENT ON COLUMN js_ai_vector.weight IS '权重';
COMMENT ON COLUMN js_ai_vector.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_vector.create_by IS '创建者';
COMMENT ON COLUMN js_ai_vector.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_vector.update_by IS '更新者';
COMMENT ON COLUMN js_ai_vector.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_vector.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_vector.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_vector.corp_name IS '租户名称';
COMMENT ON COLUMN js_ai_vector.base_url IS '服务地址';
COMMENT ON COLUMN js_ai_vector.username IS '用户名';
COMMENT ON COLUMN js_ai_vector.password IS '密码或密钥';
COMMENT ON TABLE js_ai_vector_biz IS 'AI向量库业务配置';
COMMENT ON COLUMN js_ai_vector_biz.biz_type IS '业务类型';
COMMENT ON COLUMN js_ai_vector_biz.biz_name IS '业务名称';
COMMENT ON COLUMN js_ai_vector_biz.vector_code IS '向量库编码';
COMMENT ON COLUMN js_ai_vector_biz.model_code IS '向量模型编码';
COMMENT ON COLUMN js_ai_vector_biz.index_name IS '索引名称';
COMMENT ON COLUMN js_ai_vector_biz.dimensions IS '向量维度';
COMMENT ON COLUMN js_ai_vector_biz.top_k IS '检索条数';
COMMENT ON COLUMN js_ai_vector_biz.similarity_threshold IS '相似度阈值';
COMMENT ON COLUMN js_ai_vector_biz.options IS '配置选项（JSON）';
COMMENT ON COLUMN js_ai_vector_biz.weight IS '权重';
COMMENT ON COLUMN js_ai_vector_biz.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_ai_vector_biz.create_by IS '创建者';
COMMENT ON COLUMN js_ai_vector_biz.create_date IS '创建时间';
COMMENT ON COLUMN js_ai_vector_biz.update_by IS '更新者';
COMMENT ON COLUMN js_ai_vector_biz.update_date IS '更新时间';
COMMENT ON COLUMN js_ai_vector_biz.remarks IS '备注信息';
COMMENT ON COLUMN js_ai_vector_biz.corp_code IS '租户代码';
COMMENT ON COLUMN js_ai_vector_biz.corp_name IS '租户名称';



