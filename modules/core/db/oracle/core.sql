
/* Drop Tables */

DROP TABLE js_sys_company_office CASCADE CONSTRAINTS;
DROP TABLE js_sys_employee_post CASCADE CONSTRAINTS;
DROP TABLE js_sys_user_data_scope CASCADE CONSTRAINTS;
DROP TABLE js_sys_user_role CASCADE CONSTRAINTS;
DROP TABLE js_sys_user CASCADE CONSTRAINTS;
DROP TABLE js_sys_employee CASCADE CONSTRAINTS;
DROP TABLE js_sys_company CASCADE CONSTRAINTS;
DROP TABLE js_sys_area CASCADE CONSTRAINTS;
DROP TABLE js_sys_config CASCADE CONSTRAINTS;
DROP TABLE js_sys_dict_data CASCADE CONSTRAINTS;
DROP TABLE js_sys_dict_type CASCADE CONSTRAINTS;
DROP TABLE js_sys_file_upload CASCADE CONSTRAINTS;
DROP TABLE js_sys_file_entity CASCADE CONSTRAINTS;
DROP TABLE js_sys_job_log CASCADE CONSTRAINTS;
DROP TABLE js_sys_job CASCADE CONSTRAINTS;
DROP TABLE js_sys_lang CASCADE CONSTRAINTS;
DROP TABLE js_sys_log CASCADE CONSTRAINTS;
DROP TABLE js_sys_role_menu CASCADE CONSTRAINTS;
DROP TABLE js_sys_menu CASCADE CONSTRAINTS;
DROP TABLE js_sys_module CASCADE CONSTRAINTS;
DROP TABLE js_sys_msg_inner_record CASCADE CONSTRAINTS;
DROP TABLE js_sys_msg_inner CASCADE CONSTRAINTS;
DROP TABLE js_sys_msg_push CASCADE CONSTRAINTS;
DROP TABLE js_sys_msg_pushed CASCADE CONSTRAINTS;
DROP TABLE js_sys_msg_template CASCADE CONSTRAINTS;
DROP TABLE js_sys_office CASCADE CONSTRAINTS;
DROP TABLE js_sys_post CASCADE CONSTRAINTS;
DROP TABLE js_sys_role_data_scope CASCADE CONSTRAINTS;
DROP TABLE js_sys_role CASCADE CONSTRAINTS;




/* Create Tables */

-- 行政区划
CREATE TABLE js_sys_area
(
	area_code varchar2(100) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(1000) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names varchar2(1000) NOT NULL,
	area_name nvarchar2(100) NOT NULL,
	area_type char(1),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (area_code)
);


-- 公司表
CREATE TABLE js_sys_company
(
	company_code varchar2(64) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(1000) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names varchar2(1000) NOT NULL,
	view_code varchar2(100) NOT NULL,
	company_name nvarchar2(200) NOT NULL,
	full_name nvarchar2(200) NOT NULL,
	area_code varchar2(100),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (company_code)
);


-- 公司部门关联表
CREATE TABLE js_sys_company_office
(
	company_code varchar2(64) NOT NULL,
	office_code varchar2(64) NOT NULL,
	PRIMARY KEY (company_code, office_code)
);


-- 参数配置表
CREATE TABLE js_sys_config
(
	id varchar2(64) NOT NULL,
	config_name nvarchar2(100) NOT NULL,
	config_key varchar2(100) NOT NULL,
	config_value nvarchar2(1000) NOT NULL,
	is_sys char(1) NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (id)
);


-- 字典数据表
CREATE TABLE js_sys_dict_data
(
	dict_code varchar2(64) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(1000) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names varchar2(1000) NOT NULL,
	dict_label nvarchar2(100) NOT NULL,
	dict_value varchar2(100) NOT NULL,
	dict_type varchar2(100) NOT NULL,
	is_sys char(1) NOT NULL,
	description nvarchar2(500),
	css_style nvarchar2(500),
	css_class nvarchar2(500),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (dict_code)
);


-- 字典类型表
CREATE TABLE js_sys_dict_type
(
	id varchar2(64) NOT NULL,
	dict_name nvarchar2(100) NOT NULL,
	dict_type varchar2(100) NOT NULL UNIQUE,
	is_sys char(1) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (id)
);


-- 员工表
CREATE TABLE js_sys_employee
(
	emp_code varchar2(64) NOT NULL,
	emp_name nvarchar2(100) NOT NULL,
	emp_name_en varchar2(100),
	office_code varchar2(64) NOT NULL,
	office_name nvarchar2(100) NOT NULL,
	company_code varchar2(64),
	company_name nvarchar2(200),
	status char(1) NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (emp_code)
);


-- 员工与岗位关联表
CREATE TABLE js_sys_employee_post
(
	emp_code varchar2(64) NOT NULL,
	post_code varchar2(64) NOT NULL,
	PRIMARY KEY (emp_code, post_code)
);


-- 文件实体表
CREATE TABLE js_sys_file_entity
(
	file_id varchar2(64) NOT NULL,
	file_md5 varchar2(64) NOT NULL UNIQUE,
	file_path nvarchar2(1000) NOT NULL,
	file_content_type varchar2(200) NOT NULL,
	file_extension varchar2(100) NOT NULL,
	file_size number(38) NOT NULL,
	PRIMARY KEY (file_id)
);


-- 文件上传表
CREATE TABLE js_sys_file_upload
(
	id varchar2(64) NOT NULL,
	file_id varchar2(64) NOT NULL,
	file_name nvarchar2(500) NOT NULL,
	file_type varchar2(20) NOT NULL,
	biz_key varchar2(64),
	biz_type varchar2(64),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (id)
);


-- 作业调度表
CREATE TABLE js_sys_job
(
	job_name varchar2(64) NOT NULL,
	job_group varchar2(64) NOT NULL,
	description nvarchar2(100) NOT NULL,
	invoke_target nvarchar2(1000) NOT NULL,
	cron_expression varchar2(255) NOT NULL,
	misfire_instruction number(1) NOT NULL,
	concurrent char(1) NOT NULL,
	status char(1) NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (job_name, job_group)
);


-- 作业调度日志表
CREATE TABLE js_sys_job_log
(
	id varchar2(64) NOT NULL,
	job_name varchar2(64) NOT NULL,
	job_group varchar2(64) NOT NULL,
	job_type varchar2(50),
	job_event varchar2(200),
	job_message varchar2(500),
	is_exception char(1),
	exception_info clob,
	create_date timestamp,
	PRIMARY KEY (id)
);


-- 国际化语言
CREATE TABLE js_sys_lang
(
	id varchar2(64) NOT NULL,
	module_code varchar2(64) NOT NULL,
	lang_code nvarchar2(500) NOT NULL,
	lang_text nvarchar2(500) NOT NULL,
	lang_type varchar2(50) NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (id)
);


-- 操作日志表
CREATE TABLE js_sys_log
(
	id varchar2(64) NOT NULL,
	log_type varchar2(50) NOT NULL,
	log_title nvarchar2(500) NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_by_name nvarchar2(100) NOT NULL,
	create_date timestamp NOT NULL,
	request_uri nvarchar2(500),
	request_method varchar2(10),
	request_params clob,
	diff_modify_data clob,
	biz_key varchar2(64),
	biz_type varchar2(64),
	remote_addr varchar2(255) NOT NULL,
	server_addr varchar2(255) NOT NULL,
	is_exception char(1),
	exception_info clob,
	user_agent nvarchar2(500),
	device_name varchar2(100),
	browser_name varchar2(100),
	execute_time number(19),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);


-- 菜单表
CREATE TABLE js_sys_menu
(
	menu_code varchar2(64) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(1000) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names varchar2(1000) NOT NULL,
	menu_name varchar2(100) NOT NULL,
	menu_type char(1) NOT NULL,
	menu_href varchar2(1000),
	menu_target varchar2(20),
	menu_icon varchar2(100),
	menu_color varchar2(50),
	permission varchar2(1000),
	weight number(4),
	is_show char(1) NOT NULL,
	sys_code varchar2(64) NOT NULL,
	module_codes varchar2(500) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (menu_code)
);


-- 模块表
CREATE TABLE js_sys_module
(
	module_code varchar2(64) NOT NULL,
	module_name varchar2(100) NOT NULL,
	description nvarchar2(500),
	main_class_name varchar2(500),
	current_version varchar2(50),
	upgrade_info varchar2(300),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (module_code)
);


-- 内部消息
CREATE TABLE js_sys_msg_inner
(
	id varchar2(64) NOT NULL,
	msg_title varchar2(200) NOT NULL,
	content_level char(1) NOT NULL,
	content_type char(1),
	msg_content clob NOT NULL,
	receive_type char(1) NOT NULL,
	receive_codes clob NOT NULL,
	receive_names clob NOT NULL,
	send_user_code varchar2(64) NOT NULL,
	send_user_name varchar2(100) NOT NULL,
	send_date timestamp NOT NULL,
	is_attac char(1),
	notify_types varchar2(100) NOT NULL,
	status char(1) NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (id)
);


-- 内部消息发送记录表
CREATE TABLE js_sys_msg_inner_record
(
	id varchar2(64) NOT NULL,
	msg_inner_id varchar2(64) NOT NULL,
	receive_user_code varchar2(64),
	receive_user_name varchar2(100) NOT NULL,
	read_status char(1) NOT NULL,
	read_date timestamp,
	is_star char(1),
	PRIMARY KEY (id)
);


-- 消息推送表
CREATE TABLE js_sys_msg_push
(
	id varchar2(64) NOT NULL,
	msg_type varchar2(16) NOT NULL,
	msg_title varchar2(200) NOT NULL,
	msg_content clob NOT NULL,
	biz_key varchar2(64),
	biz_type varchar2(64),
	receive_code varchar2(64) NOT NULL,
	receive_user_code varchar2(64) NOT NULL,
	receive_user_name varchar2(100) NOT NULL,
	send_user_code varchar2(64) NOT NULL,
	send_user_name varchar2(100) NOT NULL,
	send_date timestamp NOT NULL,
	is_merge_push char(1),
	plan_push_date timestamp,
	push_number number(10,0),
	push_return_code varchar2(200),
	push_return_msg_id varchar2(200),
	push_return_content clob,
	push_status char(1),
	push_date date,
	read_status char(1),
	read_date date,
	PRIMARY KEY (id)
);


-- 消息已推送表
CREATE TABLE js_sys_msg_pushed
(
	id varchar2(64) NOT NULL,
	msg_type varchar2(16) NOT NULL,
	msg_title varchar2(200) NOT NULL,
	msg_content clob NOT NULL,
	biz_key varchar2(64),
	biz_type varchar2(64),
	receive_code varchar2(64) NOT NULL,
	receive_user_code varchar2(64) NOT NULL,
	receive_user_name varchar2(100) NOT NULL,
	send_user_code varchar2(64) NOT NULL,
	send_user_name varchar2(100) NOT NULL,
	send_date timestamp NOT NULL,
	is_merge_push char(1),
	plan_push_date timestamp,
	push_number number(10,0),
	push_return_content clob,
	push_return_code varchar2(200),
	push_return_msg_id varchar2(200),
	push_status char(1),
	push_date date,
	read_status char(1),
	read_date date,
	PRIMARY KEY (id)
);


-- 消息模板
CREATE TABLE js_sys_msg_template
(
	id varchar2(64) NOT NULL,
	module_code varchar2(64),
	tpl_key varchar2(100) NOT NULL,
	tpl_name nvarchar2(100) NOT NULL,
	tpl_type varchar2(16) NOT NULL,
	tpl_content clob NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (id)
);


-- 组织机构表
CREATE TABLE js_sys_office
(
	office_code varchar2(64) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(1000) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names varchar2(1000) NOT NULL,
	view_code varchar2(100) NOT NULL,
	office_name nvarchar2(100) NOT NULL,
	full_name varchar2(200) NOT NULL,
	office_type char(1) NOT NULL,
	leader varchar2(100),
	phone varchar2(100),
	address varchar2(255),
	zip_code varchar2(100),
	email varchar2(300),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (office_code)
);


-- 员工岗位表
CREATE TABLE js_sys_post
(
	post_code varchar2(64) NOT NULL,
	post_name varchar2(100) NOT NULL,
	post_type varchar2(100),
	post_sort number(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (post_code)
);


-- 角色表
CREATE TABLE js_sys_role
(
	role_code varchar2(64) NOT NULL,
	role_name varchar2(100) NOT NULL,
	role_type varchar2(100),
	role_sort number(10),
	is_sys char(1),
	user_type varchar2(16),
	data_scope char(1),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (role_code)
);


-- 角色数据权限表
CREATE TABLE js_sys_role_data_scope
(
	role_code varchar2(64) NOT NULL,
	ctrl_type varchar2(20) NOT NULL,
	ctrl_data varchar2(64) NOT NULL,
	ctrl_permi varchar2(64) NOT NULL,
	PRIMARY KEY (role_code, ctrl_type, ctrl_data, ctrl_permi)
);


-- 角色与菜单关联表
CREATE TABLE js_sys_role_menu
(
	role_code varchar2(64) NOT NULL,
	menu_code varchar2(64) NOT NULL,
	PRIMARY KEY (role_code, menu_code)
);


-- 用户表
CREATE TABLE js_sys_user
(
	user_code varchar2(100) NOT NULL,
	login_code varchar2(100) NOT NULL,
	user_name varchar2(100) NOT NULL,
	password varchar2(100) NOT NULL,
	email varchar2(300),
	mobile varchar2(100),
	phone varchar2(100),
	sex char(1),
	avatar varchar2(1000),
	sign nvarchar2(200),
	wx_openid varchar2(100),
	mobile_imei varchar2(100),
	user_type varchar2(16) NOT NULL,
	ref_code varchar2(64),
	ref_name varchar2(100),
	mgr_type char(1) NOT NULL,
	pwd_security_level number(1),
	pwd_update_date timestamp,
	pwd_update_record varchar2(1000),
	pwd_question varchar2(200),
	pwd_question_answer varchar2(200),
	pwd_question_2 varchar2(200),
	pwd_question_answer_2 varchar2(200),
	pwd_question_3 varchar2(200),
	pwd_question_answer_3 varchar2(200),
	pwd_quest_update_date timestamp,
	last_login_ip varchar2(100),
	last_login_date timestamp,
	freeze_date timestamp,
	freeze_cause varchar2(200),
	user_weight number(8) DEFAULT 0,
	status char NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (user_code)
);


-- 用户数据权限表
CREATE TABLE js_sys_user_data_scope
(
	user_code varchar2(100) NOT NULL,
	ctrl_type varchar2(20) NOT NULL,
	ctrl_data varchar2(64) NOT NULL,
	ctrl_permi varchar2(64) NOT NULL,
	PRIMARY KEY (user_code, ctrl_type, ctrl_data, ctrl_permi)
);


-- 用户与角色关联表
CREATE TABLE js_sys_user_role
(
	user_code varchar2(100) NOT NULL,
	role_code varchar2(64) NOT NULL,
	PRIMARY KEY (user_code, role_code)
);



/* Create Indexes */

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
CREATE INDEX idx_sys_config_key ON js_sys_config (config_key);
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
CREATE INDEX idx_sys_module_status ON js_sys_module (status);
CREATE INDEX idx_sys_msg_inner_cb ON js_sys_msg_inner (create_by);
CREATE INDEX idx_sys_msg_inner_status ON js_sys_msg_inner (status);
CREATE INDEX idx_sys_msg_inner_cl ON js_sys_msg_inner (content_level);
CREATE INDEX idx_sys_msg_inner_sc ON js_sys_msg_inner (send_user_code);
CREATE INDEX idx_sys_msg_inner_sd ON js_sys_msg_inner (send_date);
CREATE INDEX idx_sys_msg_inner_r_mi ON js_sys_msg_inner_record (msg_inner_id);
CREATE INDEX idx_sys_msg_inner_r_rc ON js_sys_msg_inner_record (receive_user_code);
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



/* Comments */

COMMENT ON TABLE js_sys_area IS '行政区划';
COMMENT ON COLUMN js_sys_area.area_code IS '区域编码';
COMMENT ON COLUMN js_sys_area.parent_code IS '父级编号';
COMMENT ON COLUMN js_sys_area.parent_codes IS '所有父级编号';
COMMENT ON COLUMN js_sys_area.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN js_sys_area.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN js_sys_area.tree_leaf IS '是否最末级';
COMMENT ON COLUMN js_sys_area.tree_level IS '层次级别';
COMMENT ON COLUMN js_sys_area.tree_names IS '全节点名';
COMMENT ON COLUMN js_sys_area.area_name IS '区域名称';
COMMENT ON COLUMN js_sys_area.area_type IS '区域类型';
COMMENT ON COLUMN js_sys_area.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_area.create_by IS '创建者';
COMMENT ON COLUMN js_sys_area.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_area.update_by IS '更新者';
COMMENT ON COLUMN js_sys_area.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_area.remarks IS '备注信息';
COMMENT ON TABLE js_sys_company IS '公司表';
COMMENT ON COLUMN js_sys_company.company_code IS '公司编码';
COMMENT ON COLUMN js_sys_company.parent_code IS '父级编号';
COMMENT ON COLUMN js_sys_company.parent_codes IS '所有父级编号';
COMMENT ON COLUMN js_sys_company.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN js_sys_company.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN js_sys_company.tree_leaf IS '是否最末级';
COMMENT ON COLUMN js_sys_company.tree_level IS '层次级别';
COMMENT ON COLUMN js_sys_company.tree_names IS '全节点名';
COMMENT ON COLUMN js_sys_company.view_code IS '公司代码';
COMMENT ON COLUMN js_sys_company.company_name IS '公司名称';
COMMENT ON COLUMN js_sys_company.full_name IS '公司全称';
COMMENT ON COLUMN js_sys_company.area_code IS '区域编码';
COMMENT ON COLUMN js_sys_company.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_company.create_by IS '创建者';
COMMENT ON COLUMN js_sys_company.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_company.update_by IS '更新者';
COMMENT ON COLUMN js_sys_company.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_company.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_company.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_company.corp_name IS '归属集团Name';
COMMENT ON COLUMN js_sys_company.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN js_sys_company.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN js_sys_company.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN js_sys_company.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN js_sys_company.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN js_sys_company.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN js_sys_company.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN js_sys_company.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN js_sys_company.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN js_sys_company.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN js_sys_company.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN js_sys_company.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN js_sys_company.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN js_sys_company.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN js_sys_company.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN js_sys_company.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN js_sys_company.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN js_sys_company.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN js_sys_company.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN js_sys_company.extend_d4 IS '扩展 Date 4';
COMMENT ON TABLE js_sys_company_office IS '公司部门关联表';
COMMENT ON COLUMN js_sys_company_office.company_code IS '公司编码';
COMMENT ON COLUMN js_sys_company_office.office_code IS '机构编码';
COMMENT ON TABLE js_sys_config IS '参数配置表';
COMMENT ON COLUMN js_sys_config.id IS '编号';
COMMENT ON COLUMN js_sys_config.config_name IS '名称';
COMMENT ON COLUMN js_sys_config.config_key IS '参数键';
COMMENT ON COLUMN js_sys_config.config_value IS '参数值';
COMMENT ON COLUMN js_sys_config.is_sys IS '系统内置（1是 0否）';
COMMENT ON COLUMN js_sys_config.create_by IS '创建者';
COMMENT ON COLUMN js_sys_config.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_config.update_by IS '更新者';
COMMENT ON COLUMN js_sys_config.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_config.remarks IS '备注信息';
COMMENT ON TABLE js_sys_dict_data IS '字典数据表';
COMMENT ON COLUMN js_sys_dict_data.dict_code IS '字典编码';
COMMENT ON COLUMN js_sys_dict_data.parent_code IS '父级编号';
COMMENT ON COLUMN js_sys_dict_data.parent_codes IS '所有父级编号';
COMMENT ON COLUMN js_sys_dict_data.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN js_sys_dict_data.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN js_sys_dict_data.tree_leaf IS '是否最末级';
COMMENT ON COLUMN js_sys_dict_data.tree_level IS '层次级别';
COMMENT ON COLUMN js_sys_dict_data.tree_names IS '全节点名';
COMMENT ON COLUMN js_sys_dict_data.dict_label IS '字典标签';
COMMENT ON COLUMN js_sys_dict_data.dict_value IS '字典键值';
COMMENT ON COLUMN js_sys_dict_data.dict_type IS '字典类型';
COMMENT ON COLUMN js_sys_dict_data.is_sys IS '系统内置（1是 0否）';
COMMENT ON COLUMN js_sys_dict_data.description IS '字典描述';
COMMENT ON COLUMN js_sys_dict_data.css_style IS 'css样式（如：color:red)';
COMMENT ON COLUMN js_sys_dict_data.css_class IS 'css类名（如：red）';
COMMENT ON COLUMN js_sys_dict_data.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_dict_data.create_by IS '创建者';
COMMENT ON COLUMN js_sys_dict_data.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_dict_data.update_by IS '更新者';
COMMENT ON COLUMN js_sys_dict_data.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_dict_data.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_dict_data.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_dict_data.corp_name IS '归属集团Name';
COMMENT ON COLUMN js_sys_dict_data.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN js_sys_dict_data.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN js_sys_dict_data.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN js_sys_dict_data.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN js_sys_dict_data.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN js_sys_dict_data.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN js_sys_dict_data.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN js_sys_dict_data.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN js_sys_dict_data.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN js_sys_dict_data.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN js_sys_dict_data.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN js_sys_dict_data.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN js_sys_dict_data.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN js_sys_dict_data.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN js_sys_dict_data.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN js_sys_dict_data.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN js_sys_dict_data.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN js_sys_dict_data.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN js_sys_dict_data.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN js_sys_dict_data.extend_d4 IS '扩展 Date 4';
COMMENT ON TABLE js_sys_dict_type IS '字典类型表';
COMMENT ON COLUMN js_sys_dict_type.id IS '编号';
COMMENT ON COLUMN js_sys_dict_type.dict_name IS '字典名称';
COMMENT ON COLUMN js_sys_dict_type.dict_type IS '字典类型';
COMMENT ON COLUMN js_sys_dict_type.is_sys IS '是否系统字典';
COMMENT ON COLUMN js_sys_dict_type.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_dict_type.create_by IS '创建者';
COMMENT ON COLUMN js_sys_dict_type.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_dict_type.update_by IS '更新者';
COMMENT ON COLUMN js_sys_dict_type.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_dict_type.remarks IS '备注信息';
COMMENT ON TABLE js_sys_employee IS '员工表';
COMMENT ON COLUMN js_sys_employee.emp_code IS '员工编码';
COMMENT ON COLUMN js_sys_employee.emp_name IS '员工姓名';
COMMENT ON COLUMN js_sys_employee.emp_name_en IS '英文名';
COMMENT ON COLUMN js_sys_employee.office_code IS '机构编码';
COMMENT ON COLUMN js_sys_employee.office_name IS '机构名称';
COMMENT ON COLUMN js_sys_employee.company_code IS '公司编码';
COMMENT ON COLUMN js_sys_employee.company_name IS '公司名称';
COMMENT ON COLUMN js_sys_employee.status IS '状态（0在职 1删除 2离职）';
COMMENT ON COLUMN js_sys_employee.create_by IS '创建者';
COMMENT ON COLUMN js_sys_employee.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_employee.update_by IS '更新者';
COMMENT ON COLUMN js_sys_employee.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_employee.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_employee.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_employee.corp_name IS '归属集团Name';
COMMENT ON TABLE js_sys_employee_post IS '员工与岗位关联表';
COMMENT ON COLUMN js_sys_employee_post.emp_code IS '员工编码';
COMMENT ON COLUMN js_sys_employee_post.post_code IS '岗位编码';
COMMENT ON TABLE js_sys_file_entity IS '文件实体表';
COMMENT ON COLUMN js_sys_file_entity.file_id IS '文件编号';
COMMENT ON COLUMN js_sys_file_entity.file_md5 IS '文件MD5';
COMMENT ON COLUMN js_sys_file_entity.file_path IS '文件相对路径';
COMMENT ON COLUMN js_sys_file_entity.file_content_type IS '文件内容类型';
COMMENT ON COLUMN js_sys_file_entity.file_extension IS '文件后缀扩展名';
COMMENT ON COLUMN js_sys_file_entity.file_size IS '文件大小(单位B)';
COMMENT ON TABLE js_sys_file_upload IS '文件上传表';
COMMENT ON COLUMN js_sys_file_upload.id IS '编号';
COMMENT ON COLUMN js_sys_file_upload.file_id IS '文件编号';
COMMENT ON COLUMN js_sys_file_upload.file_name IS '文件名称';
COMMENT ON COLUMN js_sys_file_upload.file_type IS '文件分类（image、media、file）';
COMMENT ON COLUMN js_sys_file_upload.biz_key IS '业务主键';
COMMENT ON COLUMN js_sys_file_upload.biz_type IS '业务类型';
COMMENT ON COLUMN js_sys_file_upload.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_file_upload.create_by IS '创建者';
COMMENT ON COLUMN js_sys_file_upload.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_file_upload.update_by IS '更新者';
COMMENT ON COLUMN js_sys_file_upload.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_file_upload.remarks IS '备注信息';
COMMENT ON TABLE js_sys_job IS '作业调度表';
COMMENT ON COLUMN js_sys_job.job_name IS '任务名称';
COMMENT ON COLUMN js_sys_job.job_group IS '任务组名';
COMMENT ON COLUMN js_sys_job.description IS '任务描述';
COMMENT ON COLUMN js_sys_job.invoke_target IS '调用目标字符串';
COMMENT ON COLUMN js_sys_job.cron_expression IS 'Cron执行表达式';
COMMENT ON COLUMN js_sys_job.misfire_instruction IS '计划执行错误策略';
COMMENT ON COLUMN js_sys_job.concurrent IS '是否并发执行';
COMMENT ON COLUMN js_sys_job.status IS '状态（0正常 1删除 2暂停）';
COMMENT ON COLUMN js_sys_job.create_by IS '创建者';
COMMENT ON COLUMN js_sys_job.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_job.update_by IS '更新者';
COMMENT ON COLUMN js_sys_job.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_job.remarks IS '备注信息';
COMMENT ON TABLE js_sys_job_log IS '作业调度日志表';
COMMENT ON COLUMN js_sys_job_log.id IS '编号';
COMMENT ON COLUMN js_sys_job_log.job_name IS '任务名称';
COMMENT ON COLUMN js_sys_job_log.job_group IS '任务组名';
COMMENT ON COLUMN js_sys_job_log.job_type IS '日志类型';
COMMENT ON COLUMN js_sys_job_log.job_event IS '日志事件';
COMMENT ON COLUMN js_sys_job_log.job_message IS '日志信息';
COMMENT ON COLUMN js_sys_job_log.is_exception IS '是否异常';
COMMENT ON COLUMN js_sys_job_log.exception_info IS '异常信息';
COMMENT ON COLUMN js_sys_job_log.create_date IS '创建时间';
COMMENT ON TABLE js_sys_lang IS '国际化语言';
COMMENT ON COLUMN js_sys_lang.id IS '编号';
COMMENT ON COLUMN js_sys_lang.module_code IS '归属模块';
COMMENT ON COLUMN js_sys_lang.lang_code IS '语言编码';
COMMENT ON COLUMN js_sys_lang.lang_text IS '语言译文';
COMMENT ON COLUMN js_sys_lang.lang_type IS '语言类型';
COMMENT ON COLUMN js_sys_lang.create_by IS '创建者';
COMMENT ON COLUMN js_sys_lang.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_lang.update_by IS '更新者';
COMMENT ON COLUMN js_sys_lang.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_lang.remarks IS '备注信息';
COMMENT ON TABLE js_sys_log IS '操作日志表';
COMMENT ON COLUMN js_sys_log.id IS '编号';
COMMENT ON COLUMN js_sys_log.log_type IS '日志类型';
COMMENT ON COLUMN js_sys_log.log_title IS '日志标题';
COMMENT ON COLUMN js_sys_log.create_by IS '创建者';
COMMENT ON COLUMN js_sys_log.create_by_name IS '用户名称';
COMMENT ON COLUMN js_sys_log.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_log.request_uri IS '请求URI';
COMMENT ON COLUMN js_sys_log.request_method IS '操作方式';
COMMENT ON COLUMN js_sys_log.request_params IS '操作提交的数据';
COMMENT ON COLUMN js_sys_log.diff_modify_data IS '新旧数据比较结果';
COMMENT ON COLUMN js_sys_log.biz_key IS '业务主键';
COMMENT ON COLUMN js_sys_log.biz_type IS '业务类型';
COMMENT ON COLUMN js_sys_log.remote_addr IS '操作IP地址';
COMMENT ON COLUMN js_sys_log.server_addr IS '请求服务器地址';
COMMENT ON COLUMN js_sys_log.is_exception IS '是否异常';
COMMENT ON COLUMN js_sys_log.exception_info IS '异常信息';
COMMENT ON COLUMN js_sys_log.user_agent IS '用户代理';
COMMENT ON COLUMN js_sys_log.device_name IS '设备名称/操作系统';
COMMENT ON COLUMN js_sys_log.browser_name IS '浏览器名称';
COMMENT ON COLUMN js_sys_log.execute_time IS '执行时间';
COMMENT ON COLUMN js_sys_log.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_log.corp_name IS '归属集团Name';
COMMENT ON TABLE js_sys_menu IS '菜单表';
COMMENT ON COLUMN js_sys_menu.menu_code IS '菜单编码';
COMMENT ON COLUMN js_sys_menu.parent_code IS '父级编号';
COMMENT ON COLUMN js_sys_menu.parent_codes IS '所有父级编号';
COMMENT ON COLUMN js_sys_menu.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN js_sys_menu.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN js_sys_menu.tree_leaf IS '是否最末级';
COMMENT ON COLUMN js_sys_menu.tree_level IS '层次级别';
COMMENT ON COLUMN js_sys_menu.tree_names IS '全节点名';
COMMENT ON COLUMN js_sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN js_sys_menu.menu_type IS '菜单类型（1菜单 2权限 3开发）';
COMMENT ON COLUMN js_sys_menu.menu_href IS '链接';
COMMENT ON COLUMN js_sys_menu.menu_target IS '目标';
COMMENT ON COLUMN js_sys_menu.menu_icon IS '图标';
COMMENT ON COLUMN js_sys_menu.menu_color IS '颜色';
COMMENT ON COLUMN js_sys_menu.permission IS '权限标识';
COMMENT ON COLUMN js_sys_menu.weight IS '菜单权重';
COMMENT ON COLUMN js_sys_menu.is_show IS '是否显示（1显示 0隐藏）';
COMMENT ON COLUMN js_sys_menu.sys_code IS '归属系统（default:主导航菜单、mobileApp:APP菜单）';
COMMENT ON COLUMN js_sys_menu.module_codes IS '归属模块（多个用逗号隔开）';
COMMENT ON COLUMN js_sys_menu.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_menu.create_by IS '创建者';
COMMENT ON COLUMN js_sys_menu.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_menu.update_by IS '更新者';
COMMENT ON COLUMN js_sys_menu.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_menu.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_menu.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN js_sys_menu.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN js_sys_menu.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN js_sys_menu.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN js_sys_menu.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN js_sys_menu.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN js_sys_menu.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN js_sys_menu.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN js_sys_menu.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN js_sys_menu.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN js_sys_menu.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN js_sys_menu.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN js_sys_menu.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN js_sys_menu.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN js_sys_menu.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN js_sys_menu.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN js_sys_menu.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN js_sys_menu.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN js_sys_menu.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN js_sys_menu.extend_d4 IS '扩展 Date 4';
COMMENT ON TABLE js_sys_module IS '模块表';
COMMENT ON COLUMN js_sys_module.module_code IS '模块编码';
COMMENT ON COLUMN js_sys_module.module_name IS '模块名称';
COMMENT ON COLUMN js_sys_module.description IS '模块描述';
COMMENT ON COLUMN js_sys_module.main_class_name IS '主类全名';
COMMENT ON COLUMN js_sys_module.current_version IS '当前版本';
COMMENT ON COLUMN js_sys_module.upgrade_info IS '升级信息';
COMMENT ON COLUMN js_sys_module.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_module.create_by IS '创建者';
COMMENT ON COLUMN js_sys_module.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_module.update_by IS '更新者';
COMMENT ON COLUMN js_sys_module.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_module.remarks IS '备注信息';
COMMENT ON TABLE js_sys_msg_inner IS '内部消息';
COMMENT ON COLUMN js_sys_msg_inner.id IS '编号';
COMMENT ON COLUMN js_sys_msg_inner.msg_title IS '消息标题';
COMMENT ON COLUMN js_sys_msg_inner.content_level IS '内容级别（1普通 2一般 3紧急）';
COMMENT ON COLUMN js_sys_msg_inner.content_type IS '内容类型（1公告 2新闻 3会议 4其它）';
COMMENT ON COLUMN js_sys_msg_inner.msg_content IS '消息内容';
COMMENT ON COLUMN js_sys_msg_inner.receive_type IS '接受者类型（1用户 2部门 3角色 4岗位）';
COMMENT ON COLUMN js_sys_msg_inner.receive_codes IS '接受者字符串';
COMMENT ON COLUMN js_sys_msg_inner.receive_names IS '接受者名称字符串';
COMMENT ON COLUMN js_sys_msg_inner.send_user_code IS '发送者用户编码';
COMMENT ON COLUMN js_sys_msg_inner.send_user_name IS '发送者用户姓名';
COMMENT ON COLUMN js_sys_msg_inner.send_date IS '发送时间';
COMMENT ON COLUMN js_sys_msg_inner.is_attac IS '是否有附件';
COMMENT ON COLUMN js_sys_msg_inner.notify_types IS '通知类型（PC APP 短信 邮件 微信）多选';
COMMENT ON COLUMN js_sys_msg_inner.status IS '状态（0正常 1删除 4审核 5驳回 9草稿）';
COMMENT ON COLUMN js_sys_msg_inner.create_by IS '创建者';
COMMENT ON COLUMN js_sys_msg_inner.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_msg_inner.update_by IS '更新者';
COMMENT ON COLUMN js_sys_msg_inner.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_msg_inner.remarks IS '备注信息';
COMMENT ON TABLE js_sys_msg_inner_record IS '内部消息发送记录表';
COMMENT ON COLUMN js_sys_msg_inner_record.id IS '编号';
COMMENT ON COLUMN js_sys_msg_inner_record.msg_inner_id IS '所属消息';
COMMENT ON COLUMN js_sys_msg_inner_record.receive_user_code IS '接受者用户编码';
COMMENT ON COLUMN js_sys_msg_inner_record.receive_user_name IS '接受者用户姓名';
COMMENT ON COLUMN js_sys_msg_inner_record.read_status IS '读取状态（0未送达 1未读 2已读）';
COMMENT ON COLUMN js_sys_msg_inner_record.read_date IS '阅读时间';
COMMENT ON COLUMN js_sys_msg_inner_record.is_star IS '是否标星';
COMMENT ON TABLE js_sys_msg_push IS '消息推送表';
COMMENT ON COLUMN js_sys_msg_push.id IS '编号';
COMMENT ON COLUMN js_sys_msg_push.msg_type IS '消息类型（PC APP 短信 邮件 微信）';
COMMENT ON COLUMN js_sys_msg_push.msg_title IS '消息标题';
COMMENT ON COLUMN js_sys_msg_push.msg_content IS '消息内容';
COMMENT ON COLUMN js_sys_msg_push.biz_key IS '业务主键';
COMMENT ON COLUMN js_sys_msg_push.biz_type IS '业务类型';
COMMENT ON COLUMN js_sys_msg_push.receive_code IS '接受者账号';
COMMENT ON COLUMN js_sys_msg_push.receive_user_code IS '接受者用户编码';
COMMENT ON COLUMN js_sys_msg_push.receive_user_name IS '接受者用户姓名';
COMMENT ON COLUMN js_sys_msg_push.send_user_code IS '发送者用户编码';
COMMENT ON COLUMN js_sys_msg_push.send_user_name IS '发送者用户姓名';
COMMENT ON COLUMN js_sys_msg_push.send_date IS '发送时间';
COMMENT ON COLUMN js_sys_msg_push.is_merge_push IS '是否合并推送';
COMMENT ON COLUMN js_sys_msg_push.plan_push_date IS '计划推送时间';
COMMENT ON COLUMN js_sys_msg_push.push_number IS '推送尝试次数';
COMMENT ON COLUMN js_sys_msg_push.push_return_code IS '推送返回结果码';
COMMENT ON COLUMN js_sys_msg_push.push_return_msg_id IS '推送返回消息编号';
COMMENT ON COLUMN js_sys_msg_push.push_return_content IS '推送返回的内容信息';
COMMENT ON COLUMN js_sys_msg_push.push_status IS '推送状态（0未推送 1成功  2失败）';
COMMENT ON COLUMN js_sys_msg_push.push_date IS '推送时间';
COMMENT ON COLUMN js_sys_msg_push.read_status IS '读取状态（0未送达 1未读 2已读）';
COMMENT ON COLUMN js_sys_msg_push.read_date IS '读取时间';
COMMENT ON TABLE js_sys_msg_pushed IS '消息已推送表';
COMMENT ON COLUMN js_sys_msg_pushed.id IS '编号';
COMMENT ON COLUMN js_sys_msg_pushed.msg_type IS '消息类型（PC APP 短信 邮件 微信）';
COMMENT ON COLUMN js_sys_msg_pushed.msg_title IS '消息标题';
COMMENT ON COLUMN js_sys_msg_pushed.msg_content IS '消息内容';
COMMENT ON COLUMN js_sys_msg_pushed.biz_key IS '业务主键';
COMMENT ON COLUMN js_sys_msg_pushed.biz_type IS '业务类型';
COMMENT ON COLUMN js_sys_msg_pushed.receive_code IS '接受者账号';
COMMENT ON COLUMN js_sys_msg_pushed.receive_user_code IS '接受者用户编码';
COMMENT ON COLUMN js_sys_msg_pushed.receive_user_name IS '接受者用户姓名';
COMMENT ON COLUMN js_sys_msg_pushed.send_user_code IS '发送者用户编码';
COMMENT ON COLUMN js_sys_msg_pushed.send_user_name IS '发送者用户姓名';
COMMENT ON COLUMN js_sys_msg_pushed.send_date IS '发送时间';
COMMENT ON COLUMN js_sys_msg_pushed.is_merge_push IS '是否合并推送';
COMMENT ON COLUMN js_sys_msg_pushed.plan_push_date IS '计划推送时间';
COMMENT ON COLUMN js_sys_msg_pushed.push_number IS '推送尝试次数';
COMMENT ON COLUMN js_sys_msg_pushed.push_return_content IS '推送返回的内容信息';
COMMENT ON COLUMN js_sys_msg_pushed.push_return_code IS '推送返回结果码';
COMMENT ON COLUMN js_sys_msg_pushed.push_return_msg_id IS '推送返回消息编号';
COMMENT ON COLUMN js_sys_msg_pushed.push_status IS '推送状态（0未推送 1成功  2失败）';
COMMENT ON COLUMN js_sys_msg_pushed.push_date IS '推送时间';
COMMENT ON COLUMN js_sys_msg_pushed.read_status IS '读取状态（0未送达 1未读 2已读）';
COMMENT ON COLUMN js_sys_msg_pushed.read_date IS '读取时间';
COMMENT ON TABLE js_sys_msg_template IS '消息模板';
COMMENT ON COLUMN js_sys_msg_template.id IS '编号';
COMMENT ON COLUMN js_sys_msg_template.module_code IS '归属模块';
COMMENT ON COLUMN js_sys_msg_template.tpl_key IS '模板键值';
COMMENT ON COLUMN js_sys_msg_template.tpl_name IS '模板名称';
COMMENT ON COLUMN js_sys_msg_template.tpl_type IS '模板类型';
COMMENT ON COLUMN js_sys_msg_template.tpl_content IS '模板内容';
COMMENT ON COLUMN js_sys_msg_template.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_msg_template.create_by IS '创建者';
COMMENT ON COLUMN js_sys_msg_template.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_msg_template.update_by IS '更新者';
COMMENT ON COLUMN js_sys_msg_template.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_msg_template.remarks IS '备注信息';
COMMENT ON TABLE js_sys_office IS '组织机构表';
COMMENT ON COLUMN js_sys_office.office_code IS '机构编码';
COMMENT ON COLUMN js_sys_office.parent_code IS '父级编号';
COMMENT ON COLUMN js_sys_office.parent_codes IS '所有父级编号';
COMMENT ON COLUMN js_sys_office.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN js_sys_office.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN js_sys_office.tree_leaf IS '是否最末级';
COMMENT ON COLUMN js_sys_office.tree_level IS '层次级别';
COMMENT ON COLUMN js_sys_office.tree_names IS '全节点名';
COMMENT ON COLUMN js_sys_office.view_code IS '机构代码';
COMMENT ON COLUMN js_sys_office.office_name IS '机构名称';
COMMENT ON COLUMN js_sys_office.full_name IS '机构全称';
COMMENT ON COLUMN js_sys_office.office_type IS '机构类型';
COMMENT ON COLUMN js_sys_office.leader IS '负责人';
COMMENT ON COLUMN js_sys_office.phone IS '办公电话';
COMMENT ON COLUMN js_sys_office.address IS '联系地址';
COMMENT ON COLUMN js_sys_office.zip_code IS '邮政编码';
COMMENT ON COLUMN js_sys_office.email IS '电子邮箱';
COMMENT ON COLUMN js_sys_office.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_office.create_by IS '创建者';
COMMENT ON COLUMN js_sys_office.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_office.update_by IS '更新者';
COMMENT ON COLUMN js_sys_office.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_office.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_office.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_office.corp_name IS '归属集团Name';
COMMENT ON COLUMN js_sys_office.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN js_sys_office.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN js_sys_office.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN js_sys_office.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN js_sys_office.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN js_sys_office.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN js_sys_office.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN js_sys_office.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN js_sys_office.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN js_sys_office.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN js_sys_office.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN js_sys_office.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN js_sys_office.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN js_sys_office.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN js_sys_office.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN js_sys_office.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN js_sys_office.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN js_sys_office.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN js_sys_office.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN js_sys_office.extend_d4 IS '扩展 Date 4';
COMMENT ON TABLE js_sys_post IS '员工岗位表';
COMMENT ON COLUMN js_sys_post.post_code IS '岗位编码';
COMMENT ON COLUMN js_sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN js_sys_post.post_type IS '岗位分类（高管、中层、基层）';
COMMENT ON COLUMN js_sys_post.post_sort IS '岗位排序（升序）';
COMMENT ON COLUMN js_sys_post.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_post.create_by IS '创建者';
COMMENT ON COLUMN js_sys_post.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_post.update_by IS '更新者';
COMMENT ON COLUMN js_sys_post.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_post.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_post.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_post.corp_name IS '归属集团Name';
COMMENT ON TABLE js_sys_role IS '角色表';
COMMENT ON COLUMN js_sys_role.role_code IS '角色编码';
COMMENT ON COLUMN js_sys_role.role_name IS '角色名称';
COMMENT ON COLUMN js_sys_role.role_type IS '角色分类（高管、中层、基层、其它）';
COMMENT ON COLUMN js_sys_role.role_sort IS '角色排序（升序）';
COMMENT ON COLUMN js_sys_role.is_sys IS '系统内置（1是 0否）';
COMMENT ON COLUMN js_sys_role.user_type IS '用户类型（employee员工 member会员）';
COMMENT ON COLUMN js_sys_role.data_scope IS '数据范围设置（0未设置  1全部数据 2自定义数据）';
COMMENT ON COLUMN js_sys_role.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_sys_role.create_by IS '创建者';
COMMENT ON COLUMN js_sys_role.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_role.update_by IS '更新者';
COMMENT ON COLUMN js_sys_role.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_role.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_role.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_role.corp_name IS '归属集团Name';
COMMENT ON TABLE js_sys_role_data_scope IS '角色数据权限表';
COMMENT ON COLUMN js_sys_role_data_scope.role_code IS '控制角色编码';
COMMENT ON COLUMN js_sys_role_data_scope.ctrl_type IS '控制类型';
COMMENT ON COLUMN js_sys_role_data_scope.ctrl_data IS '控制数据';
COMMENT ON COLUMN js_sys_role_data_scope.ctrl_permi IS '控制权限';
COMMENT ON TABLE js_sys_role_menu IS '角色与菜单关联表';
COMMENT ON COLUMN js_sys_role_menu.role_code IS '角色编码';
COMMENT ON COLUMN js_sys_role_menu.menu_code IS '菜单编码';
COMMENT ON TABLE js_sys_user IS '用户表';
COMMENT ON COLUMN js_sys_user.user_code IS '用户编码';
COMMENT ON COLUMN js_sys_user.login_code IS '登录账号';
COMMENT ON COLUMN js_sys_user.user_name IS '用户昵称';
COMMENT ON COLUMN js_sys_user.password IS '登录密码';
COMMENT ON COLUMN js_sys_user.email IS '电子邮箱';
COMMENT ON COLUMN js_sys_user.mobile IS '手机号码';
COMMENT ON COLUMN js_sys_user.phone IS '办公电话';
COMMENT ON COLUMN js_sys_user.sex IS '用户性别';
COMMENT ON COLUMN js_sys_user.avatar IS '头像路径';
COMMENT ON COLUMN js_sys_user.sign IS '个性签名';
COMMENT ON COLUMN js_sys_user.wx_openid IS '绑定的微信号';
COMMENT ON COLUMN js_sys_user.mobile_imei IS '绑定的手机串号';
COMMENT ON COLUMN js_sys_user.user_type IS '用户类型';
COMMENT ON COLUMN js_sys_user.ref_code IS '用户类型引用编号';
COMMENT ON COLUMN js_sys_user.ref_name IS '用户类型引用姓名';
COMMENT ON COLUMN js_sys_user.mgr_type IS '管理员类型（0非管理员 1系统管理员  2二级管理员）';
COMMENT ON COLUMN js_sys_user.pwd_security_level IS '密码安全级别（0初始 1很弱 2弱 3安全 4很安全）';
COMMENT ON COLUMN js_sys_user.pwd_update_date IS '密码最后更新时间';
COMMENT ON COLUMN js_sys_user.pwd_update_record IS '密码修改记录';
COMMENT ON COLUMN js_sys_user.pwd_question IS '密保问题';
COMMENT ON COLUMN js_sys_user.pwd_question_answer IS '密保问题答案';
COMMENT ON COLUMN js_sys_user.pwd_question_2 IS '密保问题2';
COMMENT ON COLUMN js_sys_user.pwd_question_answer_2 IS '密保问题答案2';
COMMENT ON COLUMN js_sys_user.pwd_question_3 IS '密保问题3';
COMMENT ON COLUMN js_sys_user.pwd_question_answer_3 IS '密保问题答案3';
COMMENT ON COLUMN js_sys_user.pwd_quest_update_date IS '密码问题修改时间';
COMMENT ON COLUMN js_sys_user.last_login_ip IS '最后登陆IP';
COMMENT ON COLUMN js_sys_user.last_login_date IS '最后登陆时间';
COMMENT ON COLUMN js_sys_user.freeze_date IS '冻结时间';
COMMENT ON COLUMN js_sys_user.freeze_cause IS '冻结原因';
COMMENT ON COLUMN js_sys_user.user_weight IS '用户权重（降序）';
COMMENT ON COLUMN js_sys_user.status IS '状态（0正常 1删除 2停用 3冻结）';
COMMENT ON COLUMN js_sys_user.create_by IS '创建者';
COMMENT ON COLUMN js_sys_user.create_date IS '创建时间';
COMMENT ON COLUMN js_sys_user.update_by IS '更新者';
COMMENT ON COLUMN js_sys_user.update_date IS '更新时间';
COMMENT ON COLUMN js_sys_user.remarks IS '备注信息';
COMMENT ON COLUMN js_sys_user.corp_code IS '归属集团Code';
COMMENT ON COLUMN js_sys_user.corp_name IS '归属集团Name';
COMMENT ON COLUMN js_sys_user.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN js_sys_user.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN js_sys_user.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN js_sys_user.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN js_sys_user.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN js_sys_user.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN js_sys_user.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN js_sys_user.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN js_sys_user.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN js_sys_user.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN js_sys_user.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN js_sys_user.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN js_sys_user.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN js_sys_user.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN js_sys_user.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN js_sys_user.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN js_sys_user.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN js_sys_user.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN js_sys_user.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN js_sys_user.extend_d4 IS '扩展 Date 4';
COMMENT ON TABLE js_sys_user_data_scope IS '用户数据权限表';
COMMENT ON COLUMN js_sys_user_data_scope.user_code IS '控制用户编码';
COMMENT ON COLUMN js_sys_user_data_scope.ctrl_type IS '控制类型';
COMMENT ON COLUMN js_sys_user_data_scope.ctrl_data IS '控制数据';
COMMENT ON COLUMN js_sys_user_data_scope.ctrl_permi IS '控制权限';
COMMENT ON TABLE js_sys_user_role IS '用户与角色关联表';
COMMENT ON COLUMN js_sys_user_role.user_code IS '用户编码';
COMMENT ON COLUMN js_sys_user_role.role_code IS '角色编码';



