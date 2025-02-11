

/* Create Tables */

-- 业务分类
CREATE TABLE ${_prefix}biz_category
(
	category_code varchar(64) NOT NULL,
	view_code varchar(500),
	category_name varchar(64) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(767) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(767) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(767) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (category_code)
);


-- 代码生成表
CREATE TABLE ${_prefix}gen_table
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
	gen_front_dir varchar(1000),
	options varchar(1000),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (table_name)
);


-- 代码生成表列
CREATE TABLE ${_prefix}gen_table_column
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
CREATE TABLE ${_prefix}sys_area
(
	area_code varchar(100) NOT NULL,
	area_name varchar(100) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(767) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(767) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(767) NOT NULL,
	area_type char(1),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (area_code)
);


-- 公司表
CREATE TABLE ${_prefix}sys_company
(
	company_code varchar(64) NOT NULL,
	view_code varchar(100) NOT NULL,
	company_name varchar(200) NOT NULL,
	full_name varchar(200) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(767) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(767) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(767) NOT NULL,
	area_code varchar(100),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
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
	extend_d1 datetime,
	extend_d2 datetime,
	extend_d3 datetime,
	extend_d4 datetime,
	extend_json varchar(1000),
	PRIMARY KEY (company_code)
);


-- 公司部门关联表
CREATE TABLE ${_prefix}sys_company_office
(
	company_code varchar(64) NOT NULL,
	office_code varchar(64) NOT NULL,
	PRIMARY KEY (company_code, office_code)
);


-- 参数配置表
CREATE TABLE ${_prefix}sys_config
(
	id varchar(64) NOT NULL,
	config_name varchar(100) NOT NULL,
	config_key varchar(100) NOT NULL,
	config_value varchar(1000),
	is_sys char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 字典数据表
CREATE TABLE ${_prefix}sys_dict_data
(
	dict_code varchar(64) NOT NULL,
	dict_label varchar(100) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(767) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(767) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(767) NOT NULL,
	dict_value varchar(500) NOT NULL,
	dict_icon varchar(100),
	dict_type varchar(100) NOT NULL,
	is_sys char(1) NOT NULL,
	description varchar(500),
	css_style varchar(500),
	css_class varchar(500),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
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
	extend_d1 datetime,
	extend_d2 datetime,
	extend_d3 datetime,
	extend_d4 datetime,
	extend_json varchar(1000),
	PRIMARY KEY (dict_code)
);


-- 字典类型表
CREATE TABLE ${_prefix}sys_dict_type
(
	id varchar(64) NOT NULL,
	dict_name varchar(100) NOT NULL,
	dict_type varchar(100) NOT NULL,
	is_sys char(1) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 员工表
CREATE TABLE ${_prefix}sys_employee
(
	emp_code varchar(64) NOT NULL,
	emp_name varchar(100) NOT NULL,
	emp_name_en varchar(100),
	emp_no varchar(100),
	office_code varchar(64) NOT NULL,
	office_name varchar(100) NOT NULL,
	company_code varchar(64),
	company_name varchar(200),
	status char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (emp_code)
);


-- 员工附属机构关系表
CREATE TABLE ${_prefix}sys_employee_office
(
	id varchar(64) NOT NULL,
	emp_code varchar(64) NOT NULL,
	office_code varchar(64) NOT NULL,
	post_code varchar(64),
	PRIMARY KEY (id)
);


-- 员工与岗位关联表
CREATE TABLE ${_prefix}sys_employee_post
(
	emp_code varchar(64) NOT NULL,
	post_code varchar(64) NOT NULL,
	PRIMARY KEY (emp_code, post_code)
);


-- 文件实体表
CREATE TABLE ${_prefix}sys_file_entity
(
	file_id varchar(64) NOT NULL,
	file_md5 varchar(64) NOT NULL,
	file_path varchar(1000) NOT NULL,
	file_content_type varchar(200) NOT NULL,
	file_extension varchar(100) NOT NULL,
	file_size decimal(31) NOT NULL,
	file_meta varchar(255),
	file_preview char(1),
	PRIMARY KEY (file_id)
);


-- 文件上传表
CREATE TABLE ${_prefix}sys_file_upload
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
	extend_d1 datetime,
	extend_d2 datetime,
	extend_d3 datetime,
	extend_d4 datetime,
	extend_json varchar(1000),
	PRIMARY KEY (id)
);


-- 作业调度表
CREATE TABLE ${_prefix}sys_job
(
	job_name varchar(64) NOT NULL,
	job_group varchar(64) NOT NULL,
	description varchar(100) NOT NULL,
	invoke_target varchar(1000) NOT NULL,
	job_type varchar(10) DEFAULT 'cron' NOT NULL,
	job_priority decimal(10),
	start_time datetime,
	start_delay decimal(19),
	repeat_interval decimal(19),
	repeat_count decimal(10),
	cron_expression varchar(255) NOT NULL,
	rule_expression varchar(255),
	misfire_instruction decimal(1) NOT NULL,
	concurrent char(1) NOT NULL,
	instance_name varchar(64) DEFAULT 'JeeSiteScheduler' NOT NULL,
	status char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (job_name, job_group)
);


-- 作业调度日志表
CREATE TABLE ${_prefix}sys_job_log
(
	id varchar(64) NOT NULL,
	job_name varchar(64) NOT NULL,
	job_group varchar(64) NOT NULL,
	job_type varchar(50),
	job_event varchar(200),
	job_message varchar(500),
	is_exception char(1),
	exception_info clob,
	create_date datetime,
	PRIMARY KEY (id)
);


-- 国际化语言
CREATE TABLE ${_prefix}sys_lang
(
	id varchar(64) NOT NULL,
	module_code varchar(64) NOT NULL,
	lang_code varchar(500) NOT NULL,
	lang_text varchar(500) NOT NULL,
	lang_type varchar(50) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 操作日志表
CREATE TABLE ${_prefix}sys_log
(
	id varchar(64) NOT NULL,
	log_type varchar(50) NOT NULL,
	log_title varchar(500) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_by_name varchar(100) NOT NULL,
	create_date datetime NOT NULL,
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
CREATE TABLE ${_prefix}sys_menu
(
	menu_code varchar(64) NOT NULL,
	menu_name varchar(100) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(767) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(767) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(767) NOT NULL,
	menu_type char(1) NOT NULL,
	menu_href varchar(1000),
	menu_target varchar(20),
	menu_icon varchar(100),
	menu_color varchar(50),
	menu_title varchar(100),
	permission varchar(1000),
	weight decimal(4),
	is_show char(1) NOT NULL,
	sys_code varchar(64) NOT NULL,
	module_codes varchar(500) NOT NULL,
	component varchar(500),
	params varchar(500),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
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
	extend_d1 datetime,
	extend_d2 datetime,
	extend_d3 datetime,
	extend_d4 datetime,
	extend_json varchar(1000),
	PRIMARY KEY (menu_code)
);


-- 菜单数据权限
CREATE TABLE ${_prefix}sys_menu_data_scope
(
	id varchar(64) NOT NULL,
	role_code varchar(64) NOT NULL,
	menu_code varchar(64) NOT NULL,
	rule_name varchar(100),
	rule_type char(1),
	rule_config clob,
	status char(1),
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 模块表
CREATE TABLE ${_prefix}sys_module
(
	module_code varchar(64) NOT NULL,
	module_name varchar(100) NOT NULL,
	description varchar(500),
	main_class_name varchar(500),
	current_version varchar(50),
	upgrade_info varchar(300),
	gen_base_dir varchar(1000),
	tpl_category varchar(200),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (module_code)
);


-- 内部消息
CREATE TABLE ${_prefix}sys_msg_inner
(
	id varchar(64) NOT NULL,
	msg_title varchar(200) NOT NULL,
	content_level char(1) NOT NULL,
	content_type char(1),
	msg_content clob NOT NULL,
	receive_type char(1) NOT NULL,
	receive_codes clob,
	receive_names clob,
	send_user_code varchar(64),
	send_user_name varchar(100),
	send_date datetime,
	is_attac char(1),
	notify_types varchar(100),
	status char(1) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 内部消息发送记录表
CREATE TABLE ${_prefix}sys_msg_inner_record
(
	id varchar(64) NOT NULL,
	msg_inner_id varchar(64) NOT NULL,
	receive_user_code varchar(64) NOT NULL,
	receive_user_name varchar(100) NOT NULL,
	read_status char(1) NOT NULL,
	read_date datetime,
	is_star char(1),
	PRIMARY KEY (id)
);


-- 消息推送表
CREATE TABLE ${_prefix}sys_msg_push
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
	send_date datetime NOT NULL,
	is_merge_push char(1),
	plan_push_date datetime,
	push_number int,
	push_return_code varchar(200),
	push_return_msg_id varchar(200),
	push_return_content clob,
	push_status char(1),
	push_date datetime,
	read_status char(1),
	read_date datetime,
	PRIMARY KEY (id)
);


-- 消息已推送表
CREATE TABLE ${_prefix}sys_msg_pushed
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
	send_date datetime NOT NULL,
	is_merge_push char(1),
	plan_push_date datetime,
	push_number int,
	push_return_content clob,
	push_return_code varchar(200),
	push_return_msg_id varchar(200),
	push_status char(1),
	push_date datetime,
	read_status char(1),
	read_date datetime,
	PRIMARY KEY (id)
);


-- 消息模板
CREATE TABLE ${_prefix}sys_msg_template
(
	id varchar(64) NOT NULL,
	module_code varchar(64),
	tpl_key varchar(100) NOT NULL,
	tpl_name varchar(100) NOT NULL,
	tpl_type varchar(16) NOT NULL,
	tpl_content clob NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
);


-- 组织机构表
CREATE TABLE ${_prefix}sys_office
(
	office_code varchar(64) NOT NULL,
	view_code varchar(100) NOT NULL,
	office_name varchar(100) NOT NULL,
	full_name varchar(200) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(767) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(767) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(767) NOT NULL,
	office_type char(1) NOT NULL,
	leader varchar(100),
	phone varchar(100),
	address varchar(255),
	zip_code varchar(100),
	email varchar(300),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
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
	extend_d1 datetime,
	extend_d2 datetime,
	extend_d3 datetime,
	extend_d4 datetime,
	extend_json varchar(1000),
	PRIMARY KEY (office_code)
);


-- 员工岗位表
CREATE TABLE ${_prefix}sys_post
(
	post_code varchar(64) NOT NULL,
	view_code varchar(100),
	post_name varchar(100) NOT NULL,
	post_type varchar(100),
	post_sort decimal(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (post_code)
);


-- 岗位与角色关联表
CREATE TABLE ${_prefix}sys_post_role
(
	post_code varchar(64) NOT NULL,
	role_code varchar(64) NOT NULL,
	PRIMARY KEY (post_code, role_code)
);


-- 角色表
CREATE TABLE ${_prefix}sys_role
(
	role_code varchar(64) NOT NULL,
	role_name varchar(100) NOT NULL,
	view_code varchar(100),
	role_type varchar(100),
	role_sort decimal(10),
	is_sys char(1),
	is_show char(1) DEFAULT '1',
	user_type varchar(16),
	desktop_url varchar(255),
	data_scope char(1),
	biz_scope varchar(255),
	sys_codes varchar(500),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
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
	extend_d1 datetime,
	extend_d2 datetime,
	extend_d3 datetime,
	extend_d4 datetime,
	extend_json varchar(1000),
	PRIMARY KEY (role_code)
);


-- 角色数据权限表
CREATE TABLE ${_prefix}sys_role_data_scope
(
	role_code varchar(64) NOT NULL,
	ctrl_type varchar(20) NOT NULL,
	ctrl_data varchar(64) NOT NULL,
	ctrl_permi varchar(64) NOT NULL,
	menu_code varchar(64) DEFAULT '0' NOT NULL,
	PRIMARY KEY (role_code, ctrl_type, ctrl_data, ctrl_permi, menu_code)
);


-- 角色与菜单关联表
CREATE TABLE ${_prefix}sys_role_menu
(
	role_code varchar(64) NOT NULL,
	menu_code varchar(64) NOT NULL,
	PRIMARY KEY (role_code, menu_code)
);


-- 用户表
CREATE TABLE ${_prefix}sys_user
(
	user_code varchar(100) NOT NULL,
	login_code varchar(100) NOT NULL,
	user_name varchar(100) NOT NULL,
	password varchar(200) NOT NULL,
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
	pwd_update_date datetime,
	pwd_update_record varchar(1000),
	pwd_question varchar(200),
	pwd_question_answer varchar(200),
	pwd_question_2 varchar(200),
	pwd_question_answer_2 varchar(200),
	pwd_question_3 varchar(200),
	pwd_question_answer_3 varchar(200),
	pwd_quest_update_date datetime,
	last_login_ip varchar(100),
	last_login_date datetime,
	freeze_date datetime,
	freeze_cause varchar(200),
	user_weight decimal(8) DEFAULT 0,
	status char NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
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
	extend_d1 datetime,
	extend_d2 datetime,
	extend_d3 datetime,
	extend_d4 datetime,
	extend_json varchar(1000),
	PRIMARY KEY (user_code)
);


-- 用户数据权限表
CREATE TABLE ${_prefix}sys_user_data_scope
(
	user_code varchar(100) NOT NULL,
	ctrl_type varchar(20) NOT NULL,
	ctrl_data varchar(64) NOT NULL,
	ctrl_permi varchar(64) NOT NULL,
	PRIMARY KEY (user_code, ctrl_type, ctrl_data, ctrl_permi)
);


-- 用户与角色关联表
CREATE TABLE ${_prefix}sys_user_role
(
	user_code varchar(100) NOT NULL,
	role_code varchar(64) NOT NULL,
	PRIMARY KEY (user_code, role_code)
);



/* Create Indexes */

CREATE INDEX idx_gen_table_ptn ON ${_prefix}gen_table (parent_table_name);
CREATE INDEX idx_gen_table_column_tn ON ${_prefix}gen_table_column (table_name);
CREATE INDEX idx_sys_area_pc ON ${_prefix}sys_area (parent_code);
CREATE INDEX idx_sys_area_ts ON ${_prefix}sys_area (tree_sort);
CREATE INDEX idx_sys_area_status ON ${_prefix}sys_area (status);
CREATE INDEX idx_sys_area_pcs ON ${_prefix}sys_area (parent_codes);
CREATE INDEX idx_sys_area_tss ON ${_prefix}sys_area (tree_sorts);
CREATE INDEX idx_sys_company_cc ON ${_prefix}sys_company (corp_code);
CREATE INDEX idx_sys_company_pc ON ${_prefix}sys_company (parent_code);
CREATE INDEX idx_sys_company_ts ON ${_prefix}sys_company (tree_sort);
CREATE INDEX idx_sys_company_status ON ${_prefix}sys_company (status);
CREATE INDEX idx_sys_company_vc ON ${_prefix}sys_company (view_code);
CREATE INDEX idx_sys_company_pcs ON ${_prefix}sys_company (parent_codes);
CREATE INDEX idx_sys_company_tss ON ${_prefix}sys_company (tree_sorts);
CREATE UNIQUE INDEX idx_sys_config_key ON ${_prefix}sys_config (config_key);
CREATE INDEX idx_sys_dict_data_cc ON ${_prefix}sys_dict_data (corp_code);
CREATE INDEX idx_sys_dict_data_dt ON ${_prefix}sys_dict_data (dict_type);
CREATE INDEX idx_sys_dict_data_pc ON ${_prefix}sys_dict_data (parent_code);
CREATE INDEX idx_sys_dict_data_status ON ${_prefix}sys_dict_data (status);
CREATE INDEX idx_sys_dict_data_pcs ON ${_prefix}sys_dict_data (parent_codes);
CREATE INDEX idx_sys_dict_data_ts ON ${_prefix}sys_dict_data (tree_sort);
CREATE INDEX idx_sys_dict_data_tss ON ${_prefix}sys_dict_data (tree_sorts);
CREATE INDEX idx_sys_dict_data_dv ON ${_prefix}sys_dict_data (dict_value);
CREATE INDEX idx_sys_dict_type_is ON ${_prefix}sys_dict_type (is_sys);
CREATE INDEX idx_sys_dict_type_status ON ${_prefix}sys_dict_type (status);
CREATE INDEX idx_sys_employee_cco ON ${_prefix}sys_employee (company_code);
CREATE INDEX idx_sys_employee_cc ON ${_prefix}sys_employee (corp_code);
CREATE INDEX idx_sys_employee_ud ON ${_prefix}sys_employee (update_date);
CREATE INDEX idx_sys_employee_oc ON ${_prefix}sys_employee (office_code);
CREATE INDEX idx_sys_employee_status ON ${_prefix}sys_employee (status);
CREATE INDEX idx_sys_file_entity_md5 ON ${_prefix}sys_file_entity (file_md5);
CREATE INDEX idx_sys_file_entity_size ON ${_prefix}sys_file_entity (file_size);
CREATE INDEX idx_sys_file_biz_ft ON ${_prefix}sys_file_upload (file_type);
CREATE INDEX idx_sys_file_biz_fi ON ${_prefix}sys_file_upload (file_id);
CREATE INDEX idx_sys_file_biz_status ON ${_prefix}sys_file_upload (status);
CREATE INDEX idx_sys_file_biz_cb ON ${_prefix}sys_file_upload (create_by);
CREATE INDEX idx_sys_file_biz_ud ON ${_prefix}sys_file_upload (update_date);
CREATE INDEX idx_sys_file_biz_bt ON ${_prefix}sys_file_upload (biz_type);
CREATE INDEX idx_sys_file_biz_bk ON ${_prefix}sys_file_upload (biz_key);
CREATE INDEX idx_sys_job_status ON ${_prefix}sys_job (status);
CREATE INDEX idx_sys_job_log_jn ON ${_prefix}sys_job_log (job_name);
CREATE INDEX idx_sys_job_log_jg ON ${_prefix}sys_job_log (job_group);
CREATE INDEX idx_sys_job_log_t ON ${_prefix}sys_job_log (job_type);
CREATE INDEX idx_sys_job_log_e ON ${_prefix}sys_job_log (job_event);
CREATE INDEX idx_sys_job_log_ie ON ${_prefix}sys_job_log (is_exception);
CREATE INDEX idx_sys_lang_code ON ${_prefix}sys_lang (lang_code);
CREATE INDEX idx_sys_lang_type ON ${_prefix}sys_lang (lang_type);
CREATE INDEX idx_sys_log_cb ON ${_prefix}sys_log (create_by);
CREATE INDEX idx_sys_log_cc ON ${_prefix}sys_log (corp_code);
CREATE INDEX idx_sys_log_lt ON ${_prefix}sys_log (log_type);
CREATE INDEX idx_sys_log_bk ON ${_prefix}sys_log (biz_key);
CREATE INDEX idx_sys_log_bt ON ${_prefix}sys_log (biz_type);
CREATE INDEX idx_sys_log_ie ON ${_prefix}sys_log (is_exception);
CREATE INDEX idx_sys_log_cd ON ${_prefix}sys_log (create_date);
CREATE INDEX idx_sys_menu_pc ON ${_prefix}sys_menu (parent_code);
CREATE INDEX idx_sys_menu_ts ON ${_prefix}sys_menu (tree_sort);
CREATE INDEX idx_sys_menu_status ON ${_prefix}sys_menu (status);
CREATE INDEX idx_sys_menu_mt ON ${_prefix}sys_menu (menu_type);
CREATE INDEX idx_sys_menu_pss ON ${_prefix}sys_menu (parent_codes);
CREATE INDEX idx_sys_menu_tss ON ${_prefix}sys_menu (tree_sorts);
CREATE INDEX idx_sys_menu_sc ON ${_prefix}sys_menu (sys_code);
CREATE INDEX idx_sys_menu_is ON ${_prefix}sys_menu (is_show);
CREATE INDEX idx_sys_menu_mcs ON ${_prefix}sys_menu (module_codes);
CREATE INDEX idx_sys_menu_wt ON ${_prefix}sys_menu (weight);
CREATE INDEX idx_sys_menu_ds_mc ON ${_prefix}sys_menu_data_scope (menu_code);
CREATE INDEX idx_sys_menu_ds_rc ON ${_prefix}sys_menu_data_scope (role_code);
CREATE INDEX idx_sys_module_status ON ${_prefix}sys_module (status);
CREATE INDEX idx_sys_msg_inner_cb ON ${_prefix}sys_msg_inner (create_by);
CREATE INDEX idx_sys_msg_inner_status ON ${_prefix}sys_msg_inner (status);
CREATE INDEX idx_sys_msg_inner_cl ON ${_prefix}sys_msg_inner (content_level);
CREATE INDEX idx_sys_msg_inner_sc ON ${_prefix}sys_msg_inner (send_user_code);
CREATE INDEX idx_sys_msg_inner_sd ON ${_prefix}sys_msg_inner (send_date);
CREATE INDEX idx_sys_msg_inner_r_mi ON ${_prefix}sys_msg_inner_record (msg_inner_id);
CREATE INDEX idx_sys_msg_inner_r_ruc ON ${_prefix}sys_msg_inner_record (receive_user_code);
CREATE INDEX idx_sys_msg_inner_r_stat ON ${_prefix}sys_msg_inner_record (read_status);
CREATE INDEX idx_sys_msg_inner_r_star ON ${_prefix}sys_msg_inner_record (is_star);
CREATE INDEX idx_sys_msg_push_type ON ${_prefix}sys_msg_push (msg_type);
CREATE INDEX idx_sys_msg_push_rc ON ${_prefix}sys_msg_push (receive_code);
CREATE INDEX idx_sys_msg_push_uc ON ${_prefix}sys_msg_push (receive_user_code);
CREATE INDEX idx_sys_msg_push_suc ON ${_prefix}sys_msg_push (send_user_code);
CREATE INDEX idx_sys_msg_push_pd ON ${_prefix}sys_msg_push (plan_push_date);
CREATE INDEX idx_sys_msg_push_ps ON ${_prefix}sys_msg_push (push_status);
CREATE INDEX idx_sys_msg_push_rs ON ${_prefix}sys_msg_push (read_status);
CREATE INDEX idx_sys_msg_push_bk ON ${_prefix}sys_msg_push (biz_key);
CREATE INDEX idx_sys_msg_push_bt ON ${_prefix}sys_msg_push (biz_type);
CREATE INDEX idx_sys_msg_push_imp ON ${_prefix}sys_msg_push (is_merge_push);
CREATE INDEX idx_sys_msg_pushed_type ON ${_prefix}sys_msg_pushed (msg_type);
CREATE INDEX idx_sys_msg_pushed_rc ON ${_prefix}sys_msg_pushed (receive_code);
CREATE INDEX idx_sys_msg_pushed_uc ON ${_prefix}sys_msg_pushed (receive_user_code);
CREATE INDEX idx_sys_msg_pushed_suc ON ${_prefix}sys_msg_pushed (send_user_code);
CREATE INDEX idx_sys_msg_pushed_pd ON ${_prefix}sys_msg_pushed (plan_push_date);
CREATE INDEX idx_sys_msg_pushed_ps ON ${_prefix}sys_msg_pushed (push_status);
CREATE INDEX idx_sys_msg_pushed_rs ON ${_prefix}sys_msg_pushed (read_status);
CREATE INDEX idx_sys_msg_pushed_bk ON ${_prefix}sys_msg_pushed (biz_key);
CREATE INDEX idx_sys_msg_pushed_bt ON ${_prefix}sys_msg_pushed (biz_type);
CREATE INDEX idx_sys_msg_pushed_imp ON ${_prefix}sys_msg_pushed (is_merge_push);
CREATE INDEX idx_sys_msg_tpl_key ON ${_prefix}sys_msg_template (tpl_key);
CREATE INDEX idx_sys_msg_tpl_type ON ${_prefix}sys_msg_template (tpl_type);
CREATE INDEX idx_sys_msg_tpl_status ON ${_prefix}sys_msg_template (status);
CREATE INDEX idx_sys_office_cc ON ${_prefix}sys_office (corp_code);
CREATE INDEX idx_sys_office_pc ON ${_prefix}sys_office (parent_code);
CREATE INDEX idx_sys_office_pcs ON ${_prefix}sys_office (parent_codes);
CREATE INDEX idx_sys_office_status ON ${_prefix}sys_office (status);
CREATE INDEX idx_sys_office_ot ON ${_prefix}sys_office (office_type);
CREATE INDEX idx_sys_office_vc ON ${_prefix}sys_office (view_code);
CREATE INDEX idx_sys_office_ts ON ${_prefix}sys_office (tree_sort);
CREATE INDEX idx_sys_office_tss ON ${_prefix}sys_office (tree_sorts);
CREATE INDEX idx_sys_post_cc ON ${_prefix}sys_post (corp_code);
CREATE INDEX idx_sys_post_status ON ${_prefix}sys_post (status);
CREATE INDEX idx_sys_post_ps ON ${_prefix}sys_post (post_sort);
CREATE INDEX idx_sys_role_cc ON ${_prefix}sys_role (corp_code);
CREATE INDEX idx_sys_role_is ON ${_prefix}sys_role (is_sys);
CREATE INDEX idx_sys_role_status ON ${_prefix}sys_role (status);
CREATE INDEX idx_sys_role_rs ON ${_prefix}sys_role (role_sort);
CREATE INDEX idx_sys_user_lc ON ${_prefix}sys_user (login_code);
CREATE INDEX idx_sys_user_email ON ${_prefix}sys_user (email);
CREATE INDEX idx_sys_user_mobile ON ${_prefix}sys_user (mobile);
CREATE INDEX idx_sys_user_wo ON ${_prefix}sys_user (wx_openid);
CREATE INDEX idx_sys_user_imei ON ${_prefix}sys_user (mobile_imei);
CREATE INDEX idx_sys_user_rt ON ${_prefix}sys_user (user_type);
CREATE INDEX idx_sys_user_rc ON ${_prefix}sys_user (ref_code);
CREATE INDEX idx_sys_user_mt ON ${_prefix}sys_user (mgr_type);
CREATE INDEX idx_sys_user_us ON ${_prefix}sys_user (user_weight);
CREATE INDEX idx_sys_user_ud ON ${_prefix}sys_user (update_date);
CREATE INDEX idx_sys_user_status ON ${_prefix}sys_user (status);
CREATE INDEX idx_sys_user_cc ON ${_prefix}sys_user (corp_code);



/* Comments */

COMMENT ON TABLE ${_prefix}biz_category IS '业务分类';
COMMENT ON COLUMN ${_prefix}biz_category.category_code IS '流程分类';
COMMENT ON COLUMN ${_prefix}biz_category.view_code IS '分类代码';
COMMENT ON COLUMN ${_prefix}biz_category.category_name IS '分类名称';
COMMENT ON COLUMN ${_prefix}biz_category.parent_code IS '父级编号';
COMMENT ON COLUMN ${_prefix}biz_category.parent_codes IS '所有父级编号';
COMMENT ON COLUMN ${_prefix}biz_category.tree_sort IS '排序号（升序）';
COMMENT ON COLUMN ${_prefix}biz_category.tree_sorts IS '所有排序号';
COMMENT ON COLUMN ${_prefix}biz_category.tree_leaf IS '是否最末级';
COMMENT ON COLUMN ${_prefix}biz_category.tree_level IS '层次级别';
COMMENT ON COLUMN ${_prefix}biz_category.tree_names IS '全节点名';
COMMENT ON COLUMN ${_prefix}biz_category.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}biz_category.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}biz_category.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}biz_category.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}biz_category.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}biz_category.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}biz_category.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}biz_category.corp_name IS '租户名称';
COMMENT ON TABLE ${_prefix}gen_table IS '代码生成表';
COMMENT ON COLUMN ${_prefix}gen_table.table_name IS '表名';
COMMENT ON COLUMN ${_prefix}gen_table.class_name IS '实体类名称';
COMMENT ON COLUMN ${_prefix}gen_table.comments IS '表说明';
COMMENT ON COLUMN ${_prefix}gen_table.parent_table_name IS '关联父表的表名';
COMMENT ON COLUMN ${_prefix}gen_table.parent_table_fk_name IS '本表关联父表的外键名';
COMMENT ON COLUMN ${_prefix}gen_table.data_source_name IS '数据源名称';
COMMENT ON COLUMN ${_prefix}gen_table.tpl_category IS '使用的模板';
COMMENT ON COLUMN ${_prefix}gen_table.package_name IS '生成包路径';
COMMENT ON COLUMN ${_prefix}gen_table.module_name IS '生成模块名';
COMMENT ON COLUMN ${_prefix}gen_table.sub_module_name IS '生成子模块名';
COMMENT ON COLUMN ${_prefix}gen_table.function_name IS '生成功能名';
COMMENT ON COLUMN ${_prefix}gen_table.function_name_simple IS '生成功能名（简写）';
COMMENT ON COLUMN ${_prefix}gen_table.function_author IS '生成功能作者';
COMMENT ON COLUMN ${_prefix}gen_table.gen_base_dir IS '生成基础路径';
COMMENT ON COLUMN ${_prefix}gen_table.gen_front_dir IS '生成前端路径';
COMMENT ON COLUMN ${_prefix}gen_table.options IS '其它生成选项';
COMMENT ON COLUMN ${_prefix}gen_table.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}gen_table.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}gen_table.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}gen_table.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}gen_table.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}gen_table_column IS '代码生成表列';
COMMENT ON COLUMN ${_prefix}gen_table_column.id IS '编号';
COMMENT ON COLUMN ${_prefix}gen_table_column.table_name IS '表名';
COMMENT ON COLUMN ${_prefix}gen_table_column.column_name IS '列名';
COMMENT ON COLUMN ${_prefix}gen_table_column.column_sort IS '列排序（升序）';
COMMENT ON COLUMN ${_prefix}gen_table_column.column_type IS '类型';
COMMENT ON COLUMN ${_prefix}gen_table_column.column_label IS '列标签名';
COMMENT ON COLUMN ${_prefix}gen_table_column.comments IS '列备注说明';
COMMENT ON COLUMN ${_prefix}gen_table_column.attr_name IS '类的属性名';
COMMENT ON COLUMN ${_prefix}gen_table_column.attr_type IS '类的属性类型';
COMMENT ON COLUMN ${_prefix}gen_table_column.is_pk IS '是否主键';
COMMENT ON COLUMN ${_prefix}gen_table_column.is_null IS '是否可为空';
COMMENT ON COLUMN ${_prefix}gen_table_column.is_insert IS '是否插入字段';
COMMENT ON COLUMN ${_prefix}gen_table_column.is_update IS '是否更新字段';
COMMENT ON COLUMN ${_prefix}gen_table_column.is_list IS '是否列表字段';
COMMENT ON COLUMN ${_prefix}gen_table_column.is_query IS '是否查询字段';
COMMENT ON COLUMN ${_prefix}gen_table_column.query_type IS '查询方式';
COMMENT ON COLUMN ${_prefix}gen_table_column.is_edit IS '是否编辑字段';
COMMENT ON COLUMN ${_prefix}gen_table_column.show_type IS '表单类型';
COMMENT ON COLUMN ${_prefix}gen_table_column.options IS '其它生成选项';
COMMENT ON TABLE ${_prefix}sys_area IS '行政区划';
COMMENT ON COLUMN ${_prefix}sys_area.area_code IS '区域编码';
COMMENT ON COLUMN ${_prefix}sys_area.area_name IS '区域名称';
COMMENT ON COLUMN ${_prefix}sys_area.parent_code IS '父级编号';
COMMENT ON COLUMN ${_prefix}sys_area.parent_codes IS '所有父级编号';
COMMENT ON COLUMN ${_prefix}sys_area.tree_sort IS '排序号（升序）';
COMMENT ON COLUMN ${_prefix}sys_area.tree_sorts IS '所有排序号';
COMMENT ON COLUMN ${_prefix}sys_area.tree_leaf IS '是否最末级';
COMMENT ON COLUMN ${_prefix}sys_area.tree_level IS '层次级别';
COMMENT ON COLUMN ${_prefix}sys_area.tree_names IS '全节点名';
COMMENT ON COLUMN ${_prefix}sys_area.area_type IS '区域类型';
COMMENT ON COLUMN ${_prefix}sys_area.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_area.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_area.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_area.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_area.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_area.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_company IS '公司表';
COMMENT ON COLUMN ${_prefix}sys_company.company_code IS '公司编码';
COMMENT ON COLUMN ${_prefix}sys_company.view_code IS '公司代码';
COMMENT ON COLUMN ${_prefix}sys_company.company_name IS '公司名称';
COMMENT ON COLUMN ${_prefix}sys_company.full_name IS '公司全称';
COMMENT ON COLUMN ${_prefix}sys_company.parent_code IS '父级编号';
COMMENT ON COLUMN ${_prefix}sys_company.parent_codes IS '所有父级编号';
COMMENT ON COLUMN ${_prefix}sys_company.tree_sort IS '排序号（升序）';
COMMENT ON COLUMN ${_prefix}sys_company.tree_sorts IS '所有排序号';
COMMENT ON COLUMN ${_prefix}sys_company.tree_leaf IS '是否最末级';
COMMENT ON COLUMN ${_prefix}sys_company.tree_level IS '层次级别';
COMMENT ON COLUMN ${_prefix}sys_company.tree_names IS '全节点名';
COMMENT ON COLUMN ${_prefix}sys_company.area_code IS '区域编码';
COMMENT ON COLUMN ${_prefix}sys_company.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_company.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_company.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_company.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_company.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_company.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_company.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_company.corp_name IS '租户名称';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_company.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_company.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_company.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_company.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_company.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_company.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_company.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_company.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_company.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_company.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_company.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_company.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_company.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN ${_prefix}sys_company.extend_json IS '扩展 JSON';
COMMENT ON TABLE ${_prefix}sys_company_office IS '公司部门关联表';
COMMENT ON COLUMN ${_prefix}sys_company_office.company_code IS '公司编码';
COMMENT ON COLUMN ${_prefix}sys_company_office.office_code IS '机构编码';
COMMENT ON TABLE ${_prefix}sys_config IS '参数配置表';
COMMENT ON COLUMN ${_prefix}sys_config.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_config.config_name IS '名称';
COMMENT ON COLUMN ${_prefix}sys_config.config_key IS '参数键';
COMMENT ON COLUMN ${_prefix}sys_config.config_value IS '参数值';
COMMENT ON COLUMN ${_prefix}sys_config.is_sys IS '系统内置（1是 0否）';
COMMENT ON COLUMN ${_prefix}sys_config.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_config.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_config.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_config.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_config.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_dict_data IS '字典数据表';
COMMENT ON COLUMN ${_prefix}sys_dict_data.dict_code IS '字典编码';
COMMENT ON COLUMN ${_prefix}sys_dict_data.dict_label IS '字典标签';
COMMENT ON COLUMN ${_prefix}sys_dict_data.parent_code IS '父级编号';
COMMENT ON COLUMN ${_prefix}sys_dict_data.parent_codes IS '所有父级编号';
COMMENT ON COLUMN ${_prefix}sys_dict_data.tree_sort IS '排序号（升序）';
COMMENT ON COLUMN ${_prefix}sys_dict_data.tree_sorts IS '所有排序号';
COMMENT ON COLUMN ${_prefix}sys_dict_data.tree_leaf IS '是否最末级';
COMMENT ON COLUMN ${_prefix}sys_dict_data.tree_level IS '层次级别';
COMMENT ON COLUMN ${_prefix}sys_dict_data.tree_names IS '全节点名';
COMMENT ON COLUMN ${_prefix}sys_dict_data.dict_value IS '字典键值';
COMMENT ON COLUMN ${_prefix}sys_dict_data.dict_icon IS '字典图标';
COMMENT ON COLUMN ${_prefix}sys_dict_data.dict_type IS '字典类型';
COMMENT ON COLUMN ${_prefix}sys_dict_data.is_sys IS '系统内置（1是 0否）';
COMMENT ON COLUMN ${_prefix}sys_dict_data.description IS '字典描述';
COMMENT ON COLUMN ${_prefix}sys_dict_data.css_style IS 'css样式（如：color:red)';
COMMENT ON COLUMN ${_prefix}sys_dict_data.css_class IS 'css类名（如：red）';
COMMENT ON COLUMN ${_prefix}sys_dict_data.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_dict_data.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_dict_data.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_dict_data.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_dict_data.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_dict_data.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_dict_data.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_dict_data.corp_name IS '租户名称';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN ${_prefix}sys_dict_data.extend_json IS '扩展 JSON';
COMMENT ON TABLE ${_prefix}sys_dict_type IS '字典类型表';
COMMENT ON COLUMN ${_prefix}sys_dict_type.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_dict_type.dict_name IS '字典名称';
COMMENT ON COLUMN ${_prefix}sys_dict_type.dict_type IS '字典类型';
COMMENT ON COLUMN ${_prefix}sys_dict_type.is_sys IS '是否系统字典';
COMMENT ON COLUMN ${_prefix}sys_dict_type.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_dict_type.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_dict_type.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_dict_type.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_dict_type.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_dict_type.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_employee IS '员工表';
COMMENT ON COLUMN ${_prefix}sys_employee.emp_code IS '员工编码';
COMMENT ON COLUMN ${_prefix}sys_employee.emp_name IS '员工姓名';
COMMENT ON COLUMN ${_prefix}sys_employee.emp_name_en IS '员工英文名';
COMMENT ON COLUMN ${_prefix}sys_employee.emp_no IS '员工工号';
COMMENT ON COLUMN ${_prefix}sys_employee.office_code IS '机构编码';
COMMENT ON COLUMN ${_prefix}sys_employee.office_name IS '机构名称';
COMMENT ON COLUMN ${_prefix}sys_employee.company_code IS '公司编码';
COMMENT ON COLUMN ${_prefix}sys_employee.company_name IS '公司名称';
COMMENT ON COLUMN ${_prefix}sys_employee.status IS '状态（0在职 1删除 2离职）';
COMMENT ON COLUMN ${_prefix}sys_employee.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_employee.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_employee.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_employee.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_employee.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_employee.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_employee.corp_name IS '租户名称';
COMMENT ON TABLE ${_prefix}sys_employee_office IS '员工附属机构关系表';
COMMENT ON COLUMN ${_prefix}sys_employee_office.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_employee_office.emp_code IS '员工编码';
COMMENT ON COLUMN ${_prefix}sys_employee_office.office_code IS '机构编码';
COMMENT ON COLUMN ${_prefix}sys_employee_office.post_code IS '岗位编码';
COMMENT ON TABLE ${_prefix}sys_employee_post IS '员工与岗位关联表';
COMMENT ON COLUMN ${_prefix}sys_employee_post.emp_code IS '员工编码';
COMMENT ON COLUMN ${_prefix}sys_employee_post.post_code IS '岗位编码';
COMMENT ON TABLE ${_prefix}sys_file_entity IS '文件实体表';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_id IS '文件编号';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_md5 IS '文件MD5';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_path IS '文件相对路径';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_content_type IS '文件内容类型';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_extension IS '文件后缀扩展名';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_size IS '文件大小(单位B)';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_meta IS '文件信息(JSON格式)';
COMMENT ON COLUMN ${_prefix}sys_file_entity.file_preview IS '文件预览标记';
COMMENT ON TABLE ${_prefix}sys_file_upload IS '文件上传表';
COMMENT ON COLUMN ${_prefix}sys_file_upload.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_file_upload.file_id IS '文件编号';
COMMENT ON COLUMN ${_prefix}sys_file_upload.file_name IS '文件名称';
COMMENT ON COLUMN ${_prefix}sys_file_upload.file_type IS '文件分类（image、media、file）';
COMMENT ON COLUMN ${_prefix}sys_file_upload.file_sort IS '文件排序（升序）';
COMMENT ON COLUMN ${_prefix}sys_file_upload.biz_key IS '业务主键';
COMMENT ON COLUMN ${_prefix}sys_file_upload.biz_type IS '业务类型';
COMMENT ON COLUMN ${_prefix}sys_file_upload.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_file_upload.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_file_upload.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_file_upload.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_file_upload.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_file_upload.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN ${_prefix}sys_file_upload.extend_json IS '扩展 JSON';
COMMENT ON TABLE ${_prefix}sys_job IS '作业调度表';
COMMENT ON COLUMN ${_prefix}sys_job.job_name IS '任务名称';
COMMENT ON COLUMN ${_prefix}sys_job.job_group IS '任务组名';
COMMENT ON COLUMN ${_prefix}sys_job.description IS '任务描述';
COMMENT ON COLUMN ${_prefix}sys_job.invoke_target IS '调用目标字符串';
COMMENT ON COLUMN ${_prefix}sys_job.job_type IS '任务触发类型';
COMMENT ON COLUMN ${_prefix}sys_job.job_priority IS '触发器优先级';
COMMENT ON COLUMN ${_prefix}sys_job.start_time IS '触发器启动时间';
COMMENT ON COLUMN ${_prefix}sys_job.start_delay IS '触发器延迟启动时间';
COMMENT ON COLUMN ${_prefix}sys_job.repeat_interval IS '循环间隔时间（毫秒）';
COMMENT ON COLUMN ${_prefix}sys_job.repeat_count IS '循环次数（ -1 无期限）';
COMMENT ON COLUMN ${_prefix}sys_job.cron_expression IS 'Cron执行表达式';
COMMENT ON COLUMN ${_prefix}sys_job.rule_expression IS '自定义规则表达式';
COMMENT ON COLUMN ${_prefix}sys_job.misfire_instruction IS '计划执行错误策略';
COMMENT ON COLUMN ${_prefix}sys_job.concurrent IS '是否并发执行';
COMMENT ON COLUMN ${_prefix}sys_job.instance_name IS '集群的实例名字';
COMMENT ON COLUMN ${_prefix}sys_job.status IS '状态（0正常 1删除 2暂停）';
COMMENT ON COLUMN ${_prefix}sys_job.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_job.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_job.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_job.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_job.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_job_log IS '作业调度日志表';
COMMENT ON COLUMN ${_prefix}sys_job_log.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_job_log.job_name IS '任务名称';
COMMENT ON COLUMN ${_prefix}sys_job_log.job_group IS '任务组名';
COMMENT ON COLUMN ${_prefix}sys_job_log.job_type IS '日志类型';
COMMENT ON COLUMN ${_prefix}sys_job_log.job_event IS '日志事件';
COMMENT ON COLUMN ${_prefix}sys_job_log.job_message IS '日志信息';
COMMENT ON COLUMN ${_prefix}sys_job_log.is_exception IS '是否异常';
COMMENT ON COLUMN ${_prefix}sys_job_log.exception_info IS '异常信息';
COMMENT ON COLUMN ${_prefix}sys_job_log.create_date IS '创建时间';
COMMENT ON TABLE ${_prefix}sys_lang IS '国际化语言';
COMMENT ON COLUMN ${_prefix}sys_lang.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_lang.module_code IS '归属模块';
COMMENT ON COLUMN ${_prefix}sys_lang.lang_code IS '语言编码';
COMMENT ON COLUMN ${_prefix}sys_lang.lang_text IS '语言译文';
COMMENT ON COLUMN ${_prefix}sys_lang.lang_type IS '语言类型';
COMMENT ON COLUMN ${_prefix}sys_lang.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_lang.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_lang.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_lang.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_lang.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_log IS '操作日志表';
COMMENT ON COLUMN ${_prefix}sys_log.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_log.log_type IS '日志类型';
COMMENT ON COLUMN ${_prefix}sys_log.log_title IS '日志标题';
COMMENT ON COLUMN ${_prefix}sys_log.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_log.create_by_name IS '用户名称';
COMMENT ON COLUMN ${_prefix}sys_log.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_log.request_uri IS '请求URI';
COMMENT ON COLUMN ${_prefix}sys_log.request_method IS '操作方式';
COMMENT ON COLUMN ${_prefix}sys_log.request_params IS '操作提交的数据';
COMMENT ON COLUMN ${_prefix}sys_log.diff_modify_data IS '新旧数据比较结果';
COMMENT ON COLUMN ${_prefix}sys_log.biz_key IS '业务主键';
COMMENT ON COLUMN ${_prefix}sys_log.biz_type IS '业务类型';
COMMENT ON COLUMN ${_prefix}sys_log.remote_addr IS '操作IP地址';
COMMENT ON COLUMN ${_prefix}sys_log.server_addr IS '请求服务器地址';
COMMENT ON COLUMN ${_prefix}sys_log.is_exception IS '是否异常';
COMMENT ON COLUMN ${_prefix}sys_log.exception_info IS '异常信息';
COMMENT ON COLUMN ${_prefix}sys_log.user_agent IS '用户代理';
COMMENT ON COLUMN ${_prefix}sys_log.device_name IS '设备名称/操作系统';
COMMENT ON COLUMN ${_prefix}sys_log.browser_name IS '浏览器名称';
COMMENT ON COLUMN ${_prefix}sys_log.execute_time IS '执行时间';
COMMENT ON COLUMN ${_prefix}sys_log.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_log.corp_name IS '租户名称';
COMMENT ON TABLE ${_prefix}sys_menu IS '菜单表';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_code IS '菜单编码';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN ${_prefix}sys_menu.parent_code IS '父级编号';
COMMENT ON COLUMN ${_prefix}sys_menu.parent_codes IS '所有父级编号';
COMMENT ON COLUMN ${_prefix}sys_menu.tree_sort IS '排序号（升序）';
COMMENT ON COLUMN ${_prefix}sys_menu.tree_sorts IS '所有排序号';
COMMENT ON COLUMN ${_prefix}sys_menu.tree_leaf IS '是否最末级';
COMMENT ON COLUMN ${_prefix}sys_menu.tree_level IS '层次级别';
COMMENT ON COLUMN ${_prefix}sys_menu.tree_names IS '全节点名';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_type IS '菜单类型（1菜单 2权限 3开发）';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_href IS '链接';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_target IS '目标';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_icon IS '图标';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_color IS '颜色';
COMMENT ON COLUMN ${_prefix}sys_menu.menu_title IS '菜单标题';
COMMENT ON COLUMN ${_prefix}sys_menu.permission IS '权限标识';
COMMENT ON COLUMN ${_prefix}sys_menu.weight IS '菜单权重';
COMMENT ON COLUMN ${_prefix}sys_menu.is_show IS '是否显示（1显示 0隐藏）';
COMMENT ON COLUMN ${_prefix}sys_menu.sys_code IS '归属系统（default:主导航菜单、mobileApp:APP菜单）';
COMMENT ON COLUMN ${_prefix}sys_menu.module_codes IS '归属模块（多个用逗号隔开）';
COMMENT ON COLUMN ${_prefix}sys_menu.component IS '组件路径';
COMMENT ON COLUMN ${_prefix}sys_menu.params IS '组件参数';
COMMENT ON COLUMN ${_prefix}sys_menu.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_menu.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_menu.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_menu.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_menu.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_menu.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN ${_prefix}sys_menu.extend_json IS '扩展 JSON';
COMMENT ON TABLE ${_prefix}sys_menu_data_scope IS '菜单数据权限';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.role_code IS '角色编码';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.menu_code IS '菜单编码';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.rule_name IS '规则名称';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.rule_type IS '规则类型（1 角色数据范围 2自定义条件规则 3自定义SQL）';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.rule_config IS '规则配置（JSON）';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_menu_data_scope.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_module IS '模块表';
COMMENT ON COLUMN ${_prefix}sys_module.module_code IS '模块编码';
COMMENT ON COLUMN ${_prefix}sys_module.module_name IS '模块名称';
COMMENT ON COLUMN ${_prefix}sys_module.description IS '模块描述';
COMMENT ON COLUMN ${_prefix}sys_module.main_class_name IS '主类全名';
COMMENT ON COLUMN ${_prefix}sys_module.current_version IS '当前版本';
COMMENT ON COLUMN ${_prefix}sys_module.upgrade_info IS '升级信息';
COMMENT ON COLUMN ${_prefix}sys_module.gen_base_dir IS '生成基础路径';
COMMENT ON COLUMN ${_prefix}sys_module.tpl_category IS '使用的模板';
COMMENT ON COLUMN ${_prefix}sys_module.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_module.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_module.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_module.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_module.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_module.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_msg_inner IS '内部消息';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.msg_title IS '消息标题';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.content_level IS '内容级别（1普通 2一般 3紧急）';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.content_type IS '内容类型（1公告 2新闻 3会议 4其它）';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.msg_content IS '消息内容';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.receive_type IS '接受者类型（0全部 1用户 2部门 3角色 4岗位）';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.receive_codes IS '接受者字符串';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.receive_names IS '接受者名称字符串';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.send_user_code IS '发送者用户编码';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.send_user_name IS '发送者用户姓名';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.send_date IS '发送时间';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.is_attac IS '是否有附件';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.notify_types IS '通知类型（PC APP 短信 邮件 微信）多选';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.status IS '状态（0正常 1删除 4审核 5驳回 9草稿）';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_msg_inner.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_msg_inner_record IS '内部消息发送记录表';
COMMENT ON COLUMN ${_prefix}sys_msg_inner_record.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_msg_inner_record.msg_inner_id IS '所属消息';
COMMENT ON COLUMN ${_prefix}sys_msg_inner_record.receive_user_code IS '接受者用户编码';
COMMENT ON COLUMN ${_prefix}sys_msg_inner_record.receive_user_name IS '接受者用户姓名';
COMMENT ON COLUMN ${_prefix}sys_msg_inner_record.read_status IS '读取状态（0未送达 1已读 2未读）';
COMMENT ON COLUMN ${_prefix}sys_msg_inner_record.read_date IS '阅读时间';
COMMENT ON COLUMN ${_prefix}sys_msg_inner_record.is_star IS '是否标星';
COMMENT ON TABLE ${_prefix}sys_msg_push IS '消息推送表';
COMMENT ON COLUMN ${_prefix}sys_msg_push.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_msg_push.msg_type IS '消息类型（PC APP 短信 邮件 微信）';
COMMENT ON COLUMN ${_prefix}sys_msg_push.msg_title IS '消息标题';
COMMENT ON COLUMN ${_prefix}sys_msg_push.msg_content IS '消息内容';
COMMENT ON COLUMN ${_prefix}sys_msg_push.biz_key IS '业务主键';
COMMENT ON COLUMN ${_prefix}sys_msg_push.biz_type IS '业务类型';
COMMENT ON COLUMN ${_prefix}sys_msg_push.receive_code IS '接受者账号';
COMMENT ON COLUMN ${_prefix}sys_msg_push.receive_user_code IS '接受者用户编码';
COMMENT ON COLUMN ${_prefix}sys_msg_push.receive_user_name IS '接受者用户姓名';
COMMENT ON COLUMN ${_prefix}sys_msg_push.send_user_code IS '发送者用户编码';
COMMENT ON COLUMN ${_prefix}sys_msg_push.send_user_name IS '发送者用户姓名';
COMMENT ON COLUMN ${_prefix}sys_msg_push.send_date IS '发送时间';
COMMENT ON COLUMN ${_prefix}sys_msg_push.is_merge_push IS '是否合并推送';
COMMENT ON COLUMN ${_prefix}sys_msg_push.plan_push_date IS '计划推送时间';
COMMENT ON COLUMN ${_prefix}sys_msg_push.push_number IS '推送尝试次数';
COMMENT ON COLUMN ${_prefix}sys_msg_push.push_return_code IS '推送返回结果码';
COMMENT ON COLUMN ${_prefix}sys_msg_push.push_return_msg_id IS '推送返回消息编号';
COMMENT ON COLUMN ${_prefix}sys_msg_push.push_return_content IS '推送返回的内容信息';
COMMENT ON COLUMN ${_prefix}sys_msg_push.push_status IS '推送状态（0未推送 1成功  2失败）';
COMMENT ON COLUMN ${_prefix}sys_msg_push.push_date IS '推送时间';
COMMENT ON COLUMN ${_prefix}sys_msg_push.read_status IS '读取状态（0未送达 1已读 2未读）';
COMMENT ON COLUMN ${_prefix}sys_msg_push.read_date IS '读取时间';
COMMENT ON TABLE ${_prefix}sys_msg_pushed IS '消息已推送表';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.msg_type IS '消息类型（PC APP 短信 邮件 微信）';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.msg_title IS '消息标题';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.msg_content IS '消息内容';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.biz_key IS '业务主键';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.biz_type IS '业务类型';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.receive_code IS '接受者账号';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.receive_user_code IS '接受者用户编码';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.receive_user_name IS '接受者用户姓名';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.send_user_code IS '发送者用户编码';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.send_user_name IS '发送者用户姓名';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.send_date IS '发送时间';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.is_merge_push IS '是否合并推送';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.plan_push_date IS '计划推送时间';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.push_number IS '推送尝试次数';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.push_return_content IS '推送返回的内容信息';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.push_return_code IS '推送返回结果码';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.push_return_msg_id IS '推送返回消息编号';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.push_status IS '推送状态（0未推送 1成功  2失败）';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.push_date IS '推送时间';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.read_status IS '读取状态（0未送达 1已读 2未读）';
COMMENT ON COLUMN ${_prefix}sys_msg_pushed.read_date IS '读取时间';
COMMENT ON TABLE ${_prefix}sys_msg_template IS '消息模板';
COMMENT ON COLUMN ${_prefix}sys_msg_template.id IS '编号';
COMMENT ON COLUMN ${_prefix}sys_msg_template.module_code IS '归属模块';
COMMENT ON COLUMN ${_prefix}sys_msg_template.tpl_key IS '模板键值';
COMMENT ON COLUMN ${_prefix}sys_msg_template.tpl_name IS '模板名称';
COMMENT ON COLUMN ${_prefix}sys_msg_template.tpl_type IS '模板类型';
COMMENT ON COLUMN ${_prefix}sys_msg_template.tpl_content IS '模板内容';
COMMENT ON COLUMN ${_prefix}sys_msg_template.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_msg_template.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_msg_template.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_msg_template.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_msg_template.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_msg_template.remarks IS '备注信息';
COMMENT ON TABLE ${_prefix}sys_office IS '组织机构表';
COMMENT ON COLUMN ${_prefix}sys_office.office_code IS '机构编码';
COMMENT ON COLUMN ${_prefix}sys_office.view_code IS '机构代码';
COMMENT ON COLUMN ${_prefix}sys_office.office_name IS '机构名称';
COMMENT ON COLUMN ${_prefix}sys_office.full_name IS '机构全称';
COMMENT ON COLUMN ${_prefix}sys_office.parent_code IS '父级编号';
COMMENT ON COLUMN ${_prefix}sys_office.parent_codes IS '所有父级编号';
COMMENT ON COLUMN ${_prefix}sys_office.tree_sort IS '排序号（升序）';
COMMENT ON COLUMN ${_prefix}sys_office.tree_sorts IS '所有排序号';
COMMENT ON COLUMN ${_prefix}sys_office.tree_leaf IS '是否最末级';
COMMENT ON COLUMN ${_prefix}sys_office.tree_level IS '层次级别';
COMMENT ON COLUMN ${_prefix}sys_office.tree_names IS '全节点名';
COMMENT ON COLUMN ${_prefix}sys_office.office_type IS '机构类型';
COMMENT ON COLUMN ${_prefix}sys_office.leader IS '负责人';
COMMENT ON COLUMN ${_prefix}sys_office.phone IS '办公电话';
COMMENT ON COLUMN ${_prefix}sys_office.address IS '联系地址';
COMMENT ON COLUMN ${_prefix}sys_office.zip_code IS '邮政编码';
COMMENT ON COLUMN ${_prefix}sys_office.email IS '电子邮箱';
COMMENT ON COLUMN ${_prefix}sys_office.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_office.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_office.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_office.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_office.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_office.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_office.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_office.corp_name IS '租户名称';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_office.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_office.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_office.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_office.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_office.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_office.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_office.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_office.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_office.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_office.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_office.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_office.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_office.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN ${_prefix}sys_office.extend_json IS '扩展 JSON';
COMMENT ON TABLE ${_prefix}sys_post IS '员工岗位表';
COMMENT ON COLUMN ${_prefix}sys_post.post_code IS '岗位编码';
COMMENT ON COLUMN ${_prefix}sys_post.view_code IS '岗位代码';
COMMENT ON COLUMN ${_prefix}sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN ${_prefix}sys_post.post_type IS '岗位分类（高管、中层、基层）';
COMMENT ON COLUMN ${_prefix}sys_post.post_sort IS '岗位排序（升序）';
COMMENT ON COLUMN ${_prefix}sys_post.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_post.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_post.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_post.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_post.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_post.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_post.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_post.corp_name IS '租户名称';
COMMENT ON TABLE ${_prefix}sys_post_role IS '岗位与角色关联表';
COMMENT ON COLUMN ${_prefix}sys_post_role.post_code IS '岗位编码';
COMMENT ON COLUMN ${_prefix}sys_post_role.role_code IS '角色编码';
COMMENT ON TABLE ${_prefix}sys_role IS '角色表';
COMMENT ON COLUMN ${_prefix}sys_role.role_code IS '角色编码';
COMMENT ON COLUMN ${_prefix}sys_role.role_name IS '角色名称';
COMMENT ON COLUMN ${_prefix}sys_role.view_code IS '角色代码';
COMMENT ON COLUMN ${_prefix}sys_role.role_type IS '角色分类（高管、中层、基层、其它）';
COMMENT ON COLUMN ${_prefix}sys_role.role_sort IS '角色排序（升序）';
COMMENT ON COLUMN ${_prefix}sys_role.is_sys IS '系统内置（1是 0否）';
COMMENT ON COLUMN ${_prefix}sys_role.is_show IS '是否显示';
COMMENT ON COLUMN ${_prefix}sys_role.user_type IS '用户类型（employee员工 member会员）';
COMMENT ON COLUMN ${_prefix}sys_role.desktop_url IS '桌面地址（仪表盘地址）';
COMMENT ON COLUMN ${_prefix}sys_role.data_scope IS '数据范围（0未设置 1全部数据 2自定义数据）';
COMMENT ON COLUMN ${_prefix}sys_role.biz_scope IS '适应业务范围（不同的功能，不同的数据权限支持）';
COMMENT ON COLUMN ${_prefix}sys_role.sys_codes IS '包含系统（多个用逗号隔开）';
COMMENT ON COLUMN ${_prefix}sys_role.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN ${_prefix}sys_role.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_role.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_role.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_role.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_role.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_role.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_role.corp_name IS '租户名称';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_role.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_role.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_role.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_role.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_role.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_role.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_role.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_role.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_role.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_role.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_role.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_role.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_role.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN ${_prefix}sys_role.extend_json IS '扩展 JSON';
COMMENT ON TABLE ${_prefix}sys_role_data_scope IS '角色数据权限表';
COMMENT ON COLUMN ${_prefix}sys_role_data_scope.role_code IS '控制角色编码';
COMMENT ON COLUMN ${_prefix}sys_role_data_scope.ctrl_type IS '控制类型';
COMMENT ON COLUMN ${_prefix}sys_role_data_scope.ctrl_data IS '控制数据';
COMMENT ON COLUMN ${_prefix}sys_role_data_scope.ctrl_permi IS '控制权限';
COMMENT ON COLUMN ${_prefix}sys_role_data_scope.menu_code IS '菜单编码';
COMMENT ON TABLE ${_prefix}sys_role_menu IS '角色与菜单关联表';
COMMENT ON COLUMN ${_prefix}sys_role_menu.role_code IS '角色编码';
COMMENT ON COLUMN ${_prefix}sys_role_menu.menu_code IS '菜单编码';
COMMENT ON TABLE ${_prefix}sys_user IS '用户表';
COMMENT ON COLUMN ${_prefix}sys_user.user_code IS '用户编码';
COMMENT ON COLUMN ${_prefix}sys_user.login_code IS '登录账号';
COMMENT ON COLUMN ${_prefix}sys_user.user_name IS '用户昵称';
COMMENT ON COLUMN ${_prefix}sys_user.password IS '登录密码';
COMMENT ON COLUMN ${_prefix}sys_user.email IS '电子邮箱';
COMMENT ON COLUMN ${_prefix}sys_user.mobile IS '手机号码';
COMMENT ON COLUMN ${_prefix}sys_user.phone IS '办公电话';
COMMENT ON COLUMN ${_prefix}sys_user.sex IS '用户性别';
COMMENT ON COLUMN ${_prefix}sys_user.avatar IS '头像路径';
COMMENT ON COLUMN ${_prefix}sys_user.sign IS '个性签名';
COMMENT ON COLUMN ${_prefix}sys_user.wx_openid IS '绑定的微信号';
COMMENT ON COLUMN ${_prefix}sys_user.mobile_imei IS '绑定的手机串号';
COMMENT ON COLUMN ${_prefix}sys_user.user_type IS '用户类型';
COMMENT ON COLUMN ${_prefix}sys_user.ref_code IS '用户类型引用编号';
COMMENT ON COLUMN ${_prefix}sys_user.ref_name IS '用户类型引用姓名';
COMMENT ON COLUMN ${_prefix}sys_user.mgr_type IS '管理员类型（0非管理员 1系统管理员  2二级管理员）';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_security_level IS '密码安全级别（0初始 1很弱 2弱 3安全 4很安全）';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_update_date IS '密码最后更新时间';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_update_record IS '密码修改记录';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_question IS '密保问题';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_question_answer IS '密保问题答案';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_question_2 IS '密保问题2';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_question_answer_2 IS '密保问题答案2';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_question_3 IS '密保问题3';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_question_answer_3 IS '密保问题答案3';
COMMENT ON COLUMN ${_prefix}sys_user.pwd_quest_update_date IS '密码问题修改时间';
COMMENT ON COLUMN ${_prefix}sys_user.last_login_ip IS '最后登陆IP';
COMMENT ON COLUMN ${_prefix}sys_user.last_login_date IS '最后登陆时间';
COMMENT ON COLUMN ${_prefix}sys_user.freeze_date IS '冻结时间';
COMMENT ON COLUMN ${_prefix}sys_user.freeze_cause IS '冻结原因';
COMMENT ON COLUMN ${_prefix}sys_user.user_weight IS '用户权重（降序）';
COMMENT ON COLUMN ${_prefix}sys_user.status IS '状态（0正常 1删除 2停用 3冻结）';
COMMENT ON COLUMN ${_prefix}sys_user.create_by IS '创建者';
COMMENT ON COLUMN ${_prefix}sys_user.create_date IS '创建时间';
COMMENT ON COLUMN ${_prefix}sys_user.update_by IS '更新者';
COMMENT ON COLUMN ${_prefix}sys_user.update_date IS '更新时间';
COMMENT ON COLUMN ${_prefix}sys_user.remarks IS '备注信息';
COMMENT ON COLUMN ${_prefix}sys_user.corp_code IS '租户代码';
COMMENT ON COLUMN ${_prefix}sys_user.corp_name IS '租户名称';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN ${_prefix}sys_user.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN ${_prefix}sys_user.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN ${_prefix}sys_user.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN ${_prefix}sys_user.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN ${_prefix}sys_user.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN ${_prefix}sys_user.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN ${_prefix}sys_user.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN ${_prefix}sys_user.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN ${_prefix}sys_user.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN ${_prefix}sys_user.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN ${_prefix}sys_user.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN ${_prefix}sys_user.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN ${_prefix}sys_user.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN ${_prefix}sys_user.extend_json IS '扩展 JSON';
COMMENT ON TABLE ${_prefix}sys_user_data_scope IS '用户数据权限表';
COMMENT ON COLUMN ${_prefix}sys_user_data_scope.user_code IS '控制用户编码';
COMMENT ON COLUMN ${_prefix}sys_user_data_scope.ctrl_type IS '控制类型';
COMMENT ON COLUMN ${_prefix}sys_user_data_scope.ctrl_data IS '控制数据';
COMMENT ON COLUMN ${_prefix}sys_user_data_scope.ctrl_permi IS '控制权限';
COMMENT ON TABLE ${_prefix}sys_user_role IS '用户与角色关联表';
COMMENT ON COLUMN ${_prefix}sys_user_role.user_code IS '用户编码';
COMMENT ON COLUMN ${_prefix}sys_user_role.role_code IS '角色编码';



