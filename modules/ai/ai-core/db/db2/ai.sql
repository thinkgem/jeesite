

/* Create Tables */

-- AI智能体
CREATE TABLE js_ai_agent
(
	id varchar(64) NOT NULL,
	agent_name vargraphic(100),
	agent_avatar varchar(100),
	agent_intro vargraphic(100),
	model_code varchar(64),
	system_prompt clob,
	welcome vargraphic(200),
	problem vargraphic(500),
	shortcut vargraphic(500),
	placeholder vargraphic(200),
	options clob,
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
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


-- 智能体与向量库关联表
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
	chat_title vargraphic(100),
	chat_type varchar(32),
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
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
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	model_code varchar(64),
	CONSTRAINT pk_ai_chat_message PRIMARY KEY (id)
);


-- AI知识库
CREATE TABLE js_ai_knowledge
(
	id varchar(64) NOT NULL,
	office_code varchar(64),
	know_title vargraphic(100) NOT NULL,
	know_type char(1),
	know_desc vargraphic(500),
	know_icon varchar(100),
	vector_code varchar(64),
	model_code varchar(64),
	weight decimal(10) DEFAULT 0,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	vector_status char(1) DEFAULT '0',
	chunk_count decimal(10),
	result_msg vargraphic(500),
	CONSTRAINT pk_ai_knowledge PRIMARY KEY (id)
);


-- AI知识库文件上传表
CREATE TABLE js_ai_knowledge_upload
(
	id varchar(64) NOT NULL,
	file_id varchar(64) NOT NULL,
	file_name vargraphic(500) NOT NULL,
	file_type varchar(20) NOT NULL,
	file_sort decimal(10),
	biz_key varchar(64),
	biz_type varchar(64),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	CONSTRAINT pk_ai_knowledge_upload PRIMARY KEY (id)
);


-- AI MCP服务表
CREATE TABLE js_ai_mcp
(
	mcp_code varchar(64) NOT NULL,
	mcp_name vargraphic(100) NOT NULL,
	mcp_desc vargraphic(500),
	mcp_type varchar(32) NOT NULL,
	mcp_url varchar(255),
	options clob,
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (mcp_code)
);


-- AI模型注册
CREATE TABLE js_ai_model
(
	model_code varchar(64) NOT NULL,
	model_name vargraphic(100) NOT NULL,
	model_provider varchar(32) NOT NULL,
	model_icon varchar(200),
	model_type varchar(32) NOT NULL,
	model_desc vargraphic(500),
	base_url varchar(255),
	api_key varchar(255),
	options clob,
	weight decimal(10) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (model_code)
);


-- AI工具注册表
CREATE TABLE js_ai_tool
(
	tool_code varchar(64) NOT NULL,
	tool_name vargraphic(200) NOT NULL,
	tool_desc vargraphic(500),
	tool_type varchar(32) NOT NULL,
	tool_bean varchar(255),
	mcp_code varchar(64),
	tool_url varchar(255),
	tool_method varchar(10),
	tool_schema clob,
	options clob,
	weight decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	CONSTRAINT pk_ai_tool PRIMARY KEY (tool_code)
);


-- AI向量库
CREATE TABLE js_ai_vector
(
	vector_code varchar(64) NOT NULL,
	vector_name varchar(100),
	vector_desc vargraphic(500),
	vector_provider varchar(64),
	options clob,
	weight decimal(10) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	base_url varchar(255),
	username varchar(100),
	password varchar(255),
	CONSTRAINT pk_ai_vector PRIMARY KEY (vector_code)
);


-- AI向量库业务配置
CREATE TABLE js_ai_vector_biz
(
	biz_type varchar(64) NOT NULL,
	biz_name vargraphic(100) NOT NULL,
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
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	CONSTRAINT pk_ai_vector_biz PRIMARY KEY (biz_type)
);



/* Create Indexes */

CREATE INDEX idx_ai_vector_biz_vc ON js_ai_vector_biz (vector_code);
CREATE INDEX idx_ai_vector_biz_mc ON js_ai_vector_biz (model_code);



