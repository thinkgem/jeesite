
/* Drop Indexes */

DROP INDEX sys_area_parent_id;
DROP INDEX sys_area_parent_ids;
DROP INDEX sys_area_del_flag;
DROP INDEX sys_dict_value;
DROP INDEX sys_dict_label;
DROP INDEX sys_dict_del_flag;
DROP INDEX sys_log_create_by;
DROP INDEX sys_log_request_uri;
DROP INDEX sys_log_type;
DROP INDEX sys_log_create_date;
DROP INDEX sys_mdict_parent_id;
DROP INDEX sys_mdict_parent_ids;
DROP INDEX sys_mdict_del_flag;
DROP INDEX sys_menu_parent_id;
DROP INDEX sys_menu_parent_ids;
DROP INDEX sys_menu_del_flag;
DROP INDEX sys_office_parent_id;
DROP INDEX sys_office_parent_ids;
DROP INDEX sys_office_del_flag;
DROP INDEX sys_office_type;
DROP INDEX sys_role_del_flag;
DROP INDEX sys_role_enname;
DROP INDEX sys_user_office_id;
DROP INDEX sys_user_login_name;
DROP INDEX sys_user_company_id;
DROP INDEX sys_user_update_date;
DROP INDEX sys_user_del_flag;



/* Drop Tables */

DROP TABLE sys_user_role CASCADE CONSTRAINTS;
DROP TABLE sys_user CASCADE CONSTRAINTS;
DROP TABLE sys_role_office CASCADE CONSTRAINTS;
DROP TABLE sys_office CASCADE CONSTRAINTS;
DROP TABLE sys_area CASCADE CONSTRAINTS;
DROP TABLE sys_dict CASCADE CONSTRAINTS;
DROP TABLE sys_log CASCADE CONSTRAINTS;
DROP TABLE sys_mdict CASCADE CONSTRAINTS;
DROP TABLE sys_role_menu CASCADE CONSTRAINTS;
DROP TABLE sys_menu CASCADE CONSTRAINTS;
DROP TABLE sys_role CASCADE CONSTRAINTS;




/* Create Tables */

-- 区域表
CREATE TABLE sys_area
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	name nvarchar2(100) NOT NULL,
	sort number(10,0) NOT NULL,
	code varchar2(100),
	type char(1),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


-- 字典表
CREATE TABLE sys_dict
(
	id varchar2(64) NOT NULL,
	value varchar2(100) NOT NULL,
	label varchar2(100) NOT NULL,
	type varchar2(100) NOT NULL,
	description nvarchar2(100) NOT NULL,
	sort number(10,0) NOT NULL,
	parent_id varchar2(64) DEFAULT '0',
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


-- 日志表
CREATE TABLE sys_log
(
	id varchar2(64) NOT NULL,
	type char(1) DEFAULT '1',
	title nvarchar2(500),
	create_by varchar2(64),
	create_date timestamp,
	remote_addr varchar2(255),
	user_agent varchar2(255),
	request_uri varchar2(255),
	method varchar2(5),
	params clob,
	exception clob,
	PRIMARY KEY (id)
);


-- 多级字典表
CREATE TABLE sys_mdict
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	name nvarchar2(100) NOT NULL,
	sort number(10,0) NOT NULL,
	description nvarchar2(100),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


-- 菜单表
CREATE TABLE sys_menu
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	name nvarchar2(100) NOT NULL,
	sort number(10,0) NOT NULL,
	href varchar2(2000),
	target varchar2(20),
	icon varchar2(100),
	is_show char(1) NOT NULL,
	permission varchar2(200),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


-- 机构表
CREATE TABLE sys_office
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	name nvarchar2(100) NOT NULL,
	sort number(10,0) NOT NULL,
	area_id varchar2(64) NOT NULL,
	code varchar2(100),
	type char(1) NOT NULL,
	grade char(1) NOT NULL,
	address nvarchar2(255),
	zip_code varchar2(100),
	master nvarchar2(100),
	phone nvarchar2(200),
	fax nvarchar2(200),
	email nvarchar2(200),
	USEABLE varchar2(64),
	PRIMARY_PERSON varchar2(64),
	DEPUTY_PERSON varchar2(64),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


-- 角色表
CREATE TABLE sys_role
(
	id varchar2(64) NOT NULL,
	office_id varchar2(64),
	name nvarchar2(100) NOT NULL,
	enname varchar2(255),
	role_type varchar2(255),
	data_scope char(1),
	is_sys varchar2(64),
	useable varchar2(64),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


-- 角色-菜单
CREATE TABLE sys_role_menu
(
	role_id varchar2(64) NOT NULL,
	menu_id varchar2(64) NOT NULL,
	PRIMARY KEY (role_id, menu_id)
);


-- 角色-机构
CREATE TABLE sys_role_office
(
	role_id varchar2(64) NOT NULL,
	office_id varchar2(64) NOT NULL,
	PRIMARY KEY (role_id, office_id)
);


-- 用户表
CREATE TABLE sys_user
(
	id varchar2(64) NOT NULL,
	company_id varchar2(64) NOT NULL,
	office_id varchar2(64) NOT NULL,
	login_name varchar2(100) NOT NULL,
	password varchar2(100) NOT NULL,
	no varchar2(100),
	name nvarchar2(100) NOT NULL,
	email nvarchar2(200),
	phone varchar2(200),
	mobile varchar2(200),
	user_type char(1),
	photo varchar2(1000),
	login_ip varchar2(100),
	login_date timestamp,
	login_flag varchar2(64),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


-- 用户-角色
CREATE TABLE sys_user_role
(
	user_id varchar2(64) NOT NULL,
	role_id varchar2(64) NOT NULL,
	PRIMARY KEY (user_id, role_id)
);



/* Create Indexes */

CREATE INDEX sys_area_parent_id ON sys_area (parent_id);
CREATE INDEX sys_area_parent_ids ON sys_area (parent_ids);
CREATE INDEX sys_area_del_flag ON sys_area (del_flag);
CREATE INDEX sys_dict_value ON sys_dict (value);
CREATE INDEX sys_dict_label ON sys_dict (label);
CREATE INDEX sys_dict_del_flag ON sys_dict (del_flag);
CREATE INDEX sys_log_create_by ON sys_log (create_by);
CREATE INDEX sys_log_request_uri ON sys_log (request_uri);
CREATE INDEX sys_log_type ON sys_log (type);
CREATE INDEX sys_log_create_date ON sys_log (create_date);
CREATE INDEX sys_mdict_parent_id ON sys_mdict (parent_id);
CREATE INDEX sys_mdict_parent_ids ON sys_mdict (parent_ids);
CREATE INDEX sys_mdict_del_flag ON sys_mdict (del_flag);
CREATE INDEX sys_menu_parent_id ON sys_menu (parent_id);
CREATE INDEX sys_menu_parent_ids ON sys_menu (parent_ids);
CREATE INDEX sys_menu_del_flag ON sys_menu (del_flag);
CREATE INDEX sys_office_parent_id ON sys_office (parent_id);
CREATE INDEX sys_office_parent_ids ON sys_office (parent_ids);
CREATE INDEX sys_office_del_flag ON sys_office (del_flag);
CREATE INDEX sys_office_type ON sys_office (type);
CREATE INDEX sys_role_del_flag ON sys_role (del_flag);
CREATE INDEX sys_role_enname ON sys_role (enname);
CREATE INDEX sys_user_office_id ON sys_user (office_id);
CREATE INDEX sys_user_login_name ON sys_user (login_name);
CREATE INDEX sys_user_company_id ON sys_user (company_id);
CREATE INDEX sys_user_update_date ON sys_user (update_date);
CREATE INDEX sys_user_del_flag ON sys_user (del_flag);



