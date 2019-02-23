
/* Drop Tables */

DROP TABLE js_gen_table_column;
DROP TABLE js_gen_table;
DROP TABLE js_sys_company_office;
DROP TABLE js_sys_employee_post;
DROP TABLE js_sys_user_data_scope;
DROP TABLE js_sys_user_role;
DROP TABLE js_sys_user;
DROP TABLE js_sys_employee;
DROP TABLE js_sys_company;
DROP TABLE js_sys_area;
DROP TABLE js_sys_config;
DROP TABLE js_sys_dict_data;
DROP TABLE js_sys_dict_type;
DROP TABLE js_sys_file_upload;
DROP TABLE js_sys_file_entity;
DROP TABLE js_sys_job_log;
DROP TABLE js_sys_job;
DROP TABLE js_sys_lang;
DROP TABLE js_sys_log;
DROP TABLE js_sys_role_menu;
DROP TABLE js_sys_menu;
DROP TABLE js_sys_module;
DROP TABLE js_sys_msg_inner_record;
DROP TABLE js_sys_msg_inner;
DROP TABLE js_sys_msg_push;
DROP TABLE js_sys_msg_pushed;
DROP TABLE js_sys_msg_template;
DROP TABLE js_sys_office;
DROP TABLE js_sys_post;
DROP TABLE js_sys_role_data_scope;
DROP TABLE js_sys_role;




/* Create Tables */

-- 代码生成表
CREATE TABLE js_gen_table
(
	table_name varchar(64) NOT NULL,
	class_name varchar(100) NOT NULL,
	comments varchar(500) NOT NULL,
	parent_table_name varchar(64),
	parent_table_fk_name varchar(64),
	data_source_name varchar(64),
	tpl_category varchar(200),
	package_name varchar(500),
	module_name varchar(30),
	sub_module_name varchar(30),
	function_name varchar(200),
	function_name_simple varchar(50),
	function_author varchar(50),
	gen_base_dir varchar(1000),
	options varchar(1000),
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (table_name)
);


-- 代码生成表列
CREATE TABLE js_gen_table_column
(
	id varchar(64) NOT NULL,
	table_name varchar(64) NOT NULL,
	column_name varchar(64) NOT NULL,
	column_sort decimal(10),
	column_type varchar(100) NOT NULL,
	column_label varchar(50),
	comments varchar(500) NOT NULL,
	attr_name varchar(200) NOT NULL,
	attr_type varchar(200) NOT NULL,
	is_pk char(1),
	is_null char(1),
	is_insert char(1),
	is_update char(1),
	is_list char(1),
	is_query char(1),
	query_type varchar(200),
	is_edit char(1),
	show_type varchar(200),
	options varchar(1000),
	PRIMARY KEY (id)
);


-- 行政区划
CREATE TABLE js_sys_area
(
	area_code varchar(100) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(1000) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(1000) NOT NULL,
	area_name varchar(100) NOT NULL,
	area_type char(1),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (area_code)
);


-- 公司表
CREATE TABLE js_sys_company
(
	company_code varchar(64) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(1000) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(1000) NOT NULL,
	view_code varchar(100) NOT NULL,
	company_name varchar(200) NOT NULL,
	full_name varchar(200) NOT NULL,
	area_code varchar(100),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 varchar(500),
	extend_s2 varchar(500),
	extend_s3 varchar(500),
	extend_s4 varchar(500),
	extend_s5 varchar(500),
	extend_s6 varchar(500),
	extend_s7 varchar(500),
	extend_s8 varchar(500),
	extend_i1 decimal(19),
	extend_i2 decimal(19),
	extend_i3 decimal(19),
	extend_i4 decimal(19),
	extend_f1 decimal(19,4),
	extend_f2 decimal(19,4),
	extend_f3 decimal(19,4),
	extend_f4 decimal(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (company_code)
);


-- 公司部门关联表
CREATE TABLE js_sys_company_office
(
	company_code varchar(64) NOT NULL,
	office_code varchar(64) NOT NULL,
	PRIMARY KEY (company_code, office_code)
);


-- 参数配置表
CREATE TABLE js_sys_config
(
	id varchar(64) NOT NULL,
	config_name varchar(100) NOT NULL,
	config_key varchar(100) NOT NULL,
	config_value varchar(1000),
	is_sys char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 字典数据表
CREATE TABLE js_sys_dict_data
(
	dict_code varchar(64) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(1000) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(1000) NOT NULL,
	dict_label varchar(100) NOT NULL,
	dict_value varchar(100) NOT NULL,
	dict_type varchar(100) NOT NULL,
	is_sys char(1) NOT NULL,
	description varchar(500),
	css_style varchar(500),
	css_class varchar(500),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 varchar(500),
	extend_s2 varchar(500),
	extend_s3 varchar(500),
	extend_s4 varchar(500),
	extend_s5 varchar(500),
	extend_s6 varchar(500),
	extend_s7 varchar(500),
	extend_s8 varchar(500),
	extend_i1 decimal(19),
	extend_i2 decimal(19),
	extend_i3 decimal(19),
	extend_i4 decimal(19),
	extend_f1 decimal(19,4),
	extend_f2 decimal(19,4),
	extend_f3 decimal(19,4),
	extend_f4 decimal(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (dict_code)
);


-- 字典类型表
CREATE TABLE js_sys_dict_type
(
	id varchar(64) NOT NULL,
	dict_name varchar(100) NOT NULL,
	dict_type varchar(100) NOT NULL,
	is_sys char(1) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 员工表
CREATE TABLE js_sys_employee
(
	emp_code varchar(64) NOT NULL,
	emp_name varchar(100) NOT NULL,
	emp_name_en varchar(100),
	office_code varchar(64) NOT NULL,
	office_name varchar(100) NOT NULL,
	company_code varchar(64),
	company_name varchar(200),
	status char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (emp_code)
);


-- 员工与岗位关联表
CREATE TABLE js_sys_employee_post
(
	emp_code varchar(64) NOT NULL,
	post_code varchar(64) NOT NULL,
	PRIMARY KEY (emp_code, post_code)
);


-- 文件实体表
CREATE TABLE js_sys_file_entity
(
	file_id varchar(64) NOT NULL,
	file_md5 varchar(64) NOT NULL UNIQUE,
	file_path varchar(1000) NOT NULL,
	file_content_type varchar(200) NOT NULL,
	file_extension varchar(100) NOT NULL,
	file_size decimal(31) NOT NULL,
	PRIMARY KEY (file_id)
);


-- 文件上传表
CREATE TABLE js_sys_file_upload
(
	id varchar(64) NOT NULL,
	file_id varchar(64) NOT NULL,
	file_name varchar(500) NOT NULL,
	file_type varchar(20) NOT NULL,
	biz_key varchar(64),
	biz_type varchar(64),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 作业调度表
CREATE TABLE js_sys_job
(
	job_name varchar(64) NOT NULL,
	job_group varchar(64) NOT NULL,
	description varchar(100) NOT NULL,
	invoke_target varchar(1000) NOT NULL,
	cron_expression varchar(255) NOT NULL,
	misfire_instruction decimal(1) NOT NULL,
	concurrent char(1) NOT NULL,
	status char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (job_name, job_group)
);


-- 作业调度日志表
CREATE TABLE js_sys_job_log
(
	id varchar(64) NOT NULL,
	job_name varchar(64) NOT NULL,
	job_group varchar(64) NOT NULL,
	job_type varchar(50),
	job_event varchar(200),
	job_message varchar(500),
	is_exception char(1),
	exception_info clob,
	create_date timestamp,
	PRIMARY KEY (id)
);


-- 国际化语言
CREATE TABLE js_sys_lang
(
	id varchar(64) NOT NULL,
	module_code varchar(64) NOT NULL,
	lang_code varchar(500) NOT NULL,
	lang_text varchar(500) NOT NULL,
	lang_type varchar(50) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 操作日志表
CREATE TABLE js_sys_log
(
	id varchar(64) NOT NULL,
	log_type varchar(50) NOT NULL,
	log_title varchar(500) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_by_name varchar(100) NOT NULL,
	create_date timestamp NOT NULL,
	request_uri varchar(500),
	request_method varchar(10),
	request_params clob,
	diff_modify_data clob,
	biz_key varchar(64),
	biz_type varchar(64),
	remote_addr varchar(255) NOT NULL,
	server_addr varchar(255) NOT NULL,
	is_exception char(1),
	exception_info clob,
	user_agent varchar(500),
	device_name varchar(100),
	browser_name varchar(100),
	execute_time decimal(19),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);


-- 菜单表
CREATE TABLE js_sys_menu
(
	menu_code varchar(64) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(1000) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(1000) NOT NULL,
	menu_name varchar(100) NOT NULL,
	menu_type char(1) NOT NULL,
	menu_href varchar(1000),
	menu_target varchar(20),
	menu_icon varchar(100),
	menu_color varchar(50),
	permission varchar(1000),
	weight decimal(4),
	is_show char(1) NOT NULL,
	sys_code varchar(64) NOT NULL,
	module_codes varchar(500) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	extend_s1 varchar(500),
	extend_s2 varchar(500),
	extend_s3 varchar(500),
	extend_s4 varchar(500),
	extend_s5 varchar(500),
	extend_s6 varchar(500),
	extend_s7 varchar(500),
	extend_s8 varchar(500),
	extend_i1 decimal(19),
	extend_i2 decimal(19),
	extend_i3 decimal(19),
	extend_i4 decimal(19),
	extend_f1 decimal(19,4),
	extend_f2 decimal(19,4),
	extend_f3 decimal(19,4),
	extend_f4 decimal(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (menu_code)
);


-- 模块表
CREATE TABLE js_sys_module
(
	module_code varchar(64) NOT NULL,
	module_name varchar(100) NOT NULL,
	description varchar(500),
	main_class_name varchar(500),
	current_version varchar(50),
	upgrade_info varchar(300),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (module_code)
);


-- 内部消息
CREATE TABLE js_sys_msg_inner
(
	id varchar(64) NOT NULL,
	msg_title varchar(200) NOT NULL,
	content_level char(1) NOT NULL,
	content_type char(1),
	msg_content clob NOT NULL,
	receive_type char(1) NOT NULL,
	receive_codes clob NOT NULL,
	receive_names clob NOT NULL,
	send_user_code varchar(64) NOT NULL,
	send_user_name varchar(100) NOT NULL,
	send_date timestamp NOT NULL,
	is_attac char(1),
	notify_types varchar(100) NOT NULL,
	status char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 内部消息发送记录表
CREATE TABLE js_sys_msg_inner_record
(
	id varchar(64) NOT NULL,
	msg_inner_id varchar(64) NOT NULL,
	receive_user_code varchar(64),
	receive_user_name varchar(100) NOT NULL,
	read_status char(1) NOT NULL,
	read_date timestamp,
	is_star char(1),
	PRIMARY KEY (id)
);


-- 消息推送表
CREATE TABLE js_sys_msg_push
(
	id varchar(64) NOT NULL,
	msg_type varchar(16) NOT NULL,
	msg_title varchar(200) NOT NULL,
	msg_content clob NOT NULL,
	biz_key varchar(64),
	biz_type varchar(64),
	receive_code varchar(64) NOT NULL,
	receive_user_code varchar(64) NOT NULL,
	receive_user_name varchar(100) NOT NULL,
	send_user_code varchar(64) NOT NULL,
	send_user_name varchar(100) NOT NULL,
	send_date timestamp NOT NULL,
	is_merge_push char(1),
	plan_push_date timestamp,
	push_number int,
	push_return_code varchar(200),
	push_return_msg_id varchar(200),
	push_return_content clob,
	push_status char(1),
	push_date timestamp,
	read_status char(1),
	read_date timestamp,
	PRIMARY KEY (id)
);


-- 消息已推送表
CREATE TABLE js_sys_msg_pushed
(
	id varchar(64) NOT NULL,
	msg_type varchar(16) NOT NULL,
	msg_title varchar(200) NOT NULL,
	msg_content clob NOT NULL,
	biz_key varchar(64),
	biz_type varchar(64),
	receive_code varchar(64) NOT NULL,
	receive_user_code varchar(64) NOT NULL,
	receive_user_name varchar(100) NOT NULL,
	send_user_code varchar(64) NOT NULL,
	send_user_name varchar(100) NOT NULL,
	send_date timestamp NOT NULL,
	is_merge_push char(1),
	plan_push_date timestamp,
	push_number int,
	push_return_content clob,
	push_return_code varchar(200),
	push_return_msg_id varchar(200),
	push_status char(1),
	push_date timestamp,
	read_status char(1),
	read_date timestamp,
	PRIMARY KEY (id)
);


-- 消息模板
CREATE TABLE js_sys_msg_template
(
	id varchar(64) NOT NULL,
	module_code varchar(64),
	tpl_key varchar(100) NOT NULL,
	tpl_name varchar(100) NOT NULL,
	tpl_type varchar(16) NOT NULL,
	tpl_content clob NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 组织机构表
CREATE TABLE js_sys_office
(
	office_code varchar(64) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(1000) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(1000) NOT NULL,
	view_code varchar(100) NOT NULL,
	office_name varchar(100) NOT NULL,
	full_name varchar(200) NOT NULL,
	office_type char(1) NOT NULL,
	leader varchar(100),
	phone varchar(100),
	address varchar(255),
	zip_code varchar(100),
	email varchar(300),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 varchar(500),
	extend_s2 varchar(500),
	extend_s3 varchar(500),
	extend_s4 varchar(500),
	extend_s5 varchar(500),
	extend_s6 varchar(500),
	extend_s7 varchar(500),
	extend_s8 varchar(500),
	extend_i1 decimal(19),
	extend_i2 decimal(19),
	extend_i3 decimal(19),
	extend_i4 decimal(19),
	extend_f1 decimal(19,4),
	extend_f2 decimal(19,4),
	extend_f3 decimal(19,4),
	extend_f4 decimal(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (office_code)
);


-- 员工岗位表
CREATE TABLE js_sys_post
(
	post_code varchar(64) NOT NULL,
	post_name varchar(100) NOT NULL,
	post_type varchar(100),
	post_sort decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (post_code)
);


-- 角色表
CREATE TABLE js_sys_role
(
	role_code varchar(64) NOT NULL,
	role_name varchar(100) NOT NULL,
	role_type varchar(100),
	role_sort decimal(10),
	is_sys char(1),
	user_type varchar(16),
	data_scope char(1),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (role_code)
);


-- 角色数据权限表
CREATE TABLE js_sys_role_data_scope
(
	role_code varchar(64) NOT NULL,
	ctrl_type varchar(20) NOT NULL,
	ctrl_data varchar(64) NOT NULL,
	ctrl_permi varchar(64) NOT NULL,
	PRIMARY KEY (role_code, ctrl_type, ctrl_data, ctrl_permi)
);


-- 角色与菜单关联表
CREATE TABLE js_sys_role_menu
(
	role_code varchar(64) NOT NULL,
	menu_code varchar(64) NOT NULL,
	PRIMARY KEY (role_code, menu_code)
);


-- 用户表
CREATE TABLE js_sys_user
(
	user_code varchar(100) NOT NULL,
	login_code varchar(100) NOT NULL,
	user_name varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	email varchar(300),
	mobile varchar(100),
	phone varchar(100),
	sex char(1),
	avatar varchar(1000),
	sign varchar(200),
	wx_openid varchar(100),
	mobile_imei varchar(100),
	user_type varchar(16) NOT NULL,
	ref_code varchar(64),
	ref_name varchar(100),
	mgr_type char(1) NOT NULL,
	pwd_security_level decimal(1),
	pwd_update_date timestamp,
	pwd_update_record varchar(1000),
	pwd_question varchar(200),
	pwd_question_answer varchar(200),
	pwd_question_2 varchar(200),
	pwd_question_answer_2 varchar(200),
	pwd_question_3 varchar(200),
	pwd_question_answer_3 varchar(200),
	pwd_quest_update_date timestamp,
	last_login_ip varchar(100),
	last_login_date timestamp,
	freeze_date timestamp,
	freeze_cause varchar(200),
	user_weight decimal(8) DEFAULT 0,
	status char NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 varchar(500),
	extend_s2 varchar(500),
	extend_s3 varchar(500),
	extend_s4 varchar(500),
	extend_s5 varchar(500),
	extend_s6 varchar(500),
	extend_s7 varchar(500),
	extend_s8 varchar(500),
	extend_i1 decimal(19),
	extend_i2 decimal(19),
	extend_i3 decimal(19),
	extend_i4 decimal(19),
	extend_f1 decimal(19,4),
	extend_f2 decimal(19,4),
	extend_f3 decimal(19,4),
	extend_f4 decimal(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (user_code)
);


-- 用户数据权限表
CREATE TABLE js_sys_user_data_scope
(
	user_code varchar(100) NOT NULL,
	ctrl_type varchar(20) NOT NULL,
	ctrl_data varchar(64) NOT NULL,
	ctrl_permi varchar(64) NOT NULL,
	PRIMARY KEY (user_code, ctrl_type, ctrl_data, ctrl_permi)
);


-- 用户与角色关联表
CREATE TABLE js_sys_user_role
(
	user_code varchar(100) NOT NULL,
	role_code varchar(64) NOT NULL,
	PRIMARY KEY (user_code, role_code)
);



/* Create Indexes */

CREATE INDEX idx_gen_table_ptn ON js_gen_table (parent_table_name);
CREATE INDEX idx_gen_table_column_tn ON js_gen_table_column (table_name);
CREATE INDEX idx_sys_area_pc ON js_sys_area (parent_code);
CREATE INDEX idx_sys_area_ts ON js_sys_area (tree_sort);
CREATE INDEX idx_sys_area_status ON js_sys_area (status);
CREATE INDEX idx_sys_area_pcs ON js_sys_area (parent_codes);
CREATE INDEX idx_sys_area_tss ON js_sys_area (tree_sorts);
CREATE INDEX idx_sys_company_cc ON js_sys_company (corp_code);
CREATE INDEX idx_sys_company_pc ON js_sys_company (parent_code);
CREATE INDEX idx_sys_company_ts ON js_sys_company (tree_sort);
CREATE INDEX idx_sys_company_status ON js_sys_company (status);
CREATE INDEX idx_sys_company_vc ON js_sys_company (view_code);
CREATE INDEX idx_sys_company_pcs ON js_sys_company (parent_codes);
CREATE INDEX idx_sys_company_tss ON js_sys_company (tree_sorts);
CREATE UNIQUE INDEX idx_sys_config_key ON js_sys_config (config_key);
CREATE INDEX idx_sys_dict_data_cc ON js_sys_dict_data (corp_code);
CREATE INDEX idx_sys_dict_data_dt ON js_sys_dict_data (dict_type);
CREATE INDEX idx_sys_dict_data_pc ON js_sys_dict_data (parent_code);
CREATE INDEX idx_sys_dict_data_status ON js_sys_dict_data (status);
CREATE INDEX idx_sys_dict_data_pcs ON js_sys_dict_data (parent_codes);
CREATE INDEX idx_sys_dict_data_ts ON js_sys_dict_data (tree_sort);
CREATE INDEX idx_sys_dict_data_tss ON js_sys_dict_data (tree_sorts);
CREATE INDEX idx_sys_dict_data_dv ON js_sys_dict_data (dict_value);
CREATE INDEX idx_sys_dict_type_is ON js_sys_dict_type (is_sys);
CREATE INDEX idx_sys_dict_type_status ON js_sys_dict_type (status);
CREATE INDEX idx_sys_employee_cco ON js_sys_employee (company_code);
CREATE INDEX idx_sys_employee_cc ON js_sys_employee (corp_code);
CREATE INDEX idx_sys_employee_ud ON js_sys_employee (update_date);
CREATE INDEX idx_sys_employee_oc ON js_sys_employee (office_code);
CREATE INDEX idx_sys_employee_status ON js_sys_employee (status);
CREATE INDEX idx_sys_file_entity_md5 ON js_sys_file_entity (file_md5);
CREATE INDEX idx_sys_file_entity_size ON js_sys_file_entity (file_size);
CREATE INDEX idx_sys_file_biz_ft ON js_sys_file_upload (file_type);
CREATE INDEX idx_sys_file_biz_fi ON js_sys_file_upload (file_id);
CREATE INDEX idx_sys_file_biz_status ON js_sys_file_upload (status);
CREATE INDEX idx_sys_file_biz_cb ON js_sys_file_upload (create_by);
CREATE INDEX idx_sys_file_biz_ud ON js_sys_file_upload (update_date);
CREATE INDEX idx_sys_file_biz_bt ON js_sys_file_upload (biz_type);
CREATE INDEX idx_sys_file_biz_bk ON js_sys_file_upload (biz_key);
CREATE INDEX idx_sys_job_status ON js_sys_job (status);
CREATE INDEX idx_sys_job_log_jn ON js_sys_job_log (job_name);
CREATE INDEX idx_sys_job_log_jg ON js_sys_job_log (job_group);
CREATE INDEX idx_sys_job_log_t ON js_sys_job_log (job_type);
CREATE INDEX idx_sys_job_log_e ON js_sys_job_log (job_event);
CREATE INDEX idx_sys_job_log_ie ON js_sys_job_log (is_exception);
CREATE INDEX idx_sys_lang_code ON js_sys_lang (lang_code);
CREATE INDEX idx_sys_lang_type ON js_sys_lang (lang_type);
CREATE INDEX idx_sys_log_cb ON js_sys_log (create_by);
CREATE INDEX idx_sys_log_cc ON js_sys_log (corp_code);
CREATE INDEX idx_sys_log_lt ON js_sys_log (log_type);
CREATE INDEX idx_sys_log_bk ON js_sys_log (biz_key);
CREATE INDEX idx_sys_log_bt ON js_sys_log (biz_type);
CREATE INDEX idx_sys_log_ie ON js_sys_log (is_exception);
CREATE INDEX idx_sys_log_cd ON js_sys_log (create_date);
CREATE INDEX idx_sys_menu_pc ON js_sys_menu (parent_code);
CREATE INDEX idx_sys_menu_ts ON js_sys_menu (tree_sort);
CREATE INDEX idx_sys_menu_status ON js_sys_menu (status);
CREATE INDEX idx_sys_menu_mt ON js_sys_menu (menu_type);
CREATE INDEX idx_sys_menu_pss ON js_sys_menu (parent_codes);
CREATE INDEX idx_sys_menu_tss ON js_sys_menu (tree_sorts);
CREATE INDEX idx_sys_menu_sc ON js_sys_menu (sys_code);
CREATE INDEX idx_sys_menu_is ON js_sys_menu (is_show);
CREATE INDEX idx_sys_menu_mcs ON js_sys_menu (module_codes);
CREATE INDEX idx_sys_menu_wt ON js_sys_menu (weight);
CREATE INDEX idx_sys_module_status ON js_sys_module (status);
CREATE INDEX idx_sys_msg_inner_cb ON js_sys_msg_inner (create_by);
CREATE INDEX idx_sys_msg_inner_status ON js_sys_msg_inner (status);
CREATE INDEX idx_sys_msg_inner_cl ON js_sys_msg_inner (content_level);
CREATE INDEX idx_sys_msg_inner_sc ON js_sys_msg_inner (send_user_code);
CREATE INDEX idx_sys_msg_inner_sd ON js_sys_msg_inner (send_date);
CREATE INDEX idx_sys_msg_inner_r_mi ON js_sys_msg_inner_record (msg_inner_id);
CREATE INDEX idx_sys_msg_inner_r_ruc ON js_sys_msg_inner_record (receive_user_code);
CREATE INDEX idx_sys_msg_inner_r_status ON js_sys_msg_inner_record (read_status);
CREATE INDEX idx_sys_msg_inner_r_star ON js_sys_msg_inner_record (is_star);
CREATE INDEX idx_sys_msg_push_type ON js_sys_msg_push (msg_type);
CREATE INDEX idx_sys_msg_push_rc ON js_sys_msg_push (receive_code);
CREATE INDEX idx_sys_msg_push_uc ON js_sys_msg_push (receive_user_code);
CREATE INDEX idx_sys_msg_push_suc ON js_sys_msg_push (send_user_code);
CREATE INDEX idx_sys_msg_push_pd ON js_sys_msg_push (plan_push_date);
CREATE INDEX idx_sys_msg_push_ps ON js_sys_msg_push (push_status);
CREATE INDEX idx_sys_msg_push_rs ON js_sys_msg_push (read_status);
CREATE INDEX idx_sys_msg_push_bk ON js_sys_msg_push (biz_key);
CREATE INDEX idx_sys_msg_push_bt ON js_sys_msg_push (biz_type);
CREATE INDEX idx_sys_msg_push_imp ON js_sys_msg_push (is_merge_push);
CREATE INDEX idx_sys_msg_pushed_type ON js_sys_msg_pushed (msg_type);
CREATE INDEX idx_sys_msg_pushed_rc ON js_sys_msg_pushed (receive_code);
CREATE INDEX idx_sys_msg_pushed_uc ON js_sys_msg_pushed (receive_user_code);
CREATE INDEX idx_sys_msg_pushed_suc ON js_sys_msg_pushed (send_user_code);
CREATE INDEX idx_sys_msg_pushed_pd ON js_sys_msg_pushed (plan_push_date);
CREATE INDEX idx_sys_msg_pushed_ps ON js_sys_msg_pushed (push_status);
CREATE INDEX idx_sys_msg_pushed_rs ON js_sys_msg_pushed (read_status);
CREATE INDEX idx_sys_msg_pushed_bk ON js_sys_msg_pushed (biz_key);
CREATE INDEX idx_sys_msg_pushed_bt ON js_sys_msg_pushed (biz_type);
CREATE INDEX idx_sys_msg_pushed_imp ON js_sys_msg_pushed (is_merge_push);
CREATE INDEX idx_sys_msg_tpl_key ON js_sys_msg_template (tpl_key);
CREATE INDEX idx_sys_msg_tpl_type ON js_sys_msg_template (tpl_type);
CREATE INDEX idx_sys_msg_tpl_status ON js_sys_msg_template (status);
CREATE INDEX idx_sys_office_cc ON js_sys_office (corp_code);
CREATE INDEX idx_sys_office_pc ON js_sys_office (parent_code);
CREATE INDEX idx_sys_office_pcs ON js_sys_office (parent_codes);
CREATE INDEX idx_sys_office_status ON js_sys_office (status);
CREATE INDEX idx_sys_office_ot ON js_sys_office (office_type);
CREATE INDEX idx_sys_office_vc ON js_sys_office (view_code);
CREATE INDEX idx_sys_office_ts ON js_sys_office (tree_sort);
CREATE INDEX idx_sys_office_tss ON js_sys_office (tree_sorts);
CREATE INDEX idx_sys_post_cc ON js_sys_post (corp_code);
CREATE INDEX idx_sys_post_status ON js_sys_post (status);
CREATE INDEX idx_sys_post_ps ON js_sys_post (post_sort);
CREATE INDEX idx_sys_role_cc ON js_sys_role (corp_code);
CREATE INDEX idx_sys_role_is ON js_sys_role (is_sys);
CREATE INDEX idx_sys_role_status ON js_sys_role (status);
CREATE INDEX idx_sys_role_rs ON js_sys_role (role_sort);
CREATE INDEX idx_sys_user_lc ON js_sys_user (login_code);
CREATE INDEX idx_sys_user_email ON js_sys_user (email);
CREATE INDEX idx_sys_user_mobile ON js_sys_user (mobile);
CREATE INDEX idx_sys_user_wo ON js_sys_user (wx_openid);
CREATE INDEX idx_sys_user_imei ON js_sys_user (mobile_imei);
CREATE INDEX idx_sys_user_rt ON js_sys_user (user_type);
CREATE INDEX idx_sys_user_rc ON js_sys_user (ref_code);
CREATE INDEX idx_sys_user_mt ON js_sys_user (mgr_type);
CREATE INDEX idx_sys_user_us ON js_sys_user (user_weight);
CREATE INDEX idx_sys_user_ud ON js_sys_user (update_date);
CREATE INDEX idx_sys_user_status ON js_sys_user (status);
CREATE INDEX idx_sys_user_cc ON js_sys_user (corp_code);



