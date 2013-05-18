SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Indexes */

DROP INDEX sys_area_parent_id ON sys_area;
DROP INDEX sys_area_parent_ids ON sys_area;
DROP INDEX sys_area_del_flag ON sys_area;
DROP INDEX sys_dict_value ON sys_dict;
DROP INDEX sys_dict_label ON sys_dict;
DROP INDEX sys_dict_del_flag ON sys_dict;
DROP INDEX sys_mdict_parent_id ON sys_mdict;
DROP INDEX sys_mdict_parent_ids ON sys_mdict;
DROP INDEX sys_mdict_del_flag ON sys_mdict;
DROP INDEX sys_menu_parent_id ON sys_menu;
DROP INDEX sys_menu_parent_ids ON sys_menu;
DROP INDEX sys_menu_del_flag ON sys_menu;
DROP INDEX sys_office_parent_id ON sys_office;
DROP INDEX sys_office_parent_ids ON sys_office;
DROP INDEX sys_office_del_flag ON sys_office;
DROP INDEX sys_role_del_flag ON sys_role;
DROP INDEX sys_user_office_id ON sys_user;
DROP INDEX sys_user_login_name ON sys_user;
DROP INDEX sys_user_del_flag ON sys_user;
DROP INDEX sys_user_company_id ON sys_user;



/* Drop Tables */

DROP TABLE sys_role_office;
DROP TABLE sys_user_role;
DROP TABLE sys_user;
DROP TABLE sys_office;
DROP TABLE sys_area;
DROP TABLE sys_dict;
DROP TABLE sys_mdict;
DROP TABLE sys_role_menu;
DROP TABLE sys_menu;
DROP TABLE sys_role;




/* Create Tables */

CREATE TABLE sys_area
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	parent_id bigint NOT NULL COMMENT '父级编号',
	parent_ids varchar(255) NOT NULL COMMENT '所有父级编号',
	code varchar(100) COMMENT '区域编码',
	name varchar(100) NOT NULL COMMENT '区域名称',
	type char(1) COMMENT '区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）',
	remarks varchar(255) COMMENT '备注',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '区域表';


CREATE TABLE sys_dict
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	label varchar(100) NOT NULL COMMENT '标签名',
	value varchar(100) NOT NULL COMMENT '数据值',
	type varchar(100) NOT NULL COMMENT '类型',
	description varchar(100) NOT NULL COMMENT '描述',
	sort int NOT NULL COMMENT '排序（升序）',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '字典表';


CREATE TABLE sys_mdict
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	parent_id bigint NOT NULL COMMENT '父级编号',
	parent_ids varchar(255) NOT NULL COMMENT '所有父级编号',
	name varchar(100) NOT NULL COMMENT '角色名称',
	description varchar(100) COMMENT '描述',
	sort int COMMENT '排序（升序）',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '区域表';


CREATE TABLE sys_menu
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	parent_id bigint NOT NULL COMMENT '父级编号',
	parent_ids varchar(255) NOT NULL COMMENT '所有父级编号',
	name varchar(100) NOT NULL COMMENT '菜单名称',
	href varchar(255) COMMENT '链接',
	target varchar(20) COMMENT '目标（mainFrame、 _blank、_self、_parent、_top）',
	icon varchar(100) COMMENT '图标',
	sort int NOT NULL COMMENT '排序（升序）',
	is_show char(1) NOT NULL COMMENT '是否在菜单中显示（1：显示；0：不显示）',
	permission varchar(200) COMMENT '权限标识',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '菜单表';


CREATE TABLE sys_office
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	parent_id bigint NOT NULL COMMENT '父级编号',
	parent_ids varchar(255) NOT NULL COMMENT '所有父级编号',
	area_id bigint NOT NULL COMMENT '归属区域',
	code varchar(100) COMMENT '区域编码',
	name varchar(100) NOT NULL COMMENT '机构名称',
	type char(1) NOT NULL COMMENT '机构类型（1：公司；2：部门；3：小组）',
	level char(1) NOT NULL COMMENT '机构级别（1：一级；2：二级；3：三级；4：四级）',
	address varchar(255) COMMENT '联系地址',
	zip_code varchar(100) COMMENT '邮政编码',
	master varchar(100) COMMENT '负责人',
	phone varbinary(200) COMMENT '电话',
	fax varchar(200) COMMENT '传真',
	email varchar(200) COMMENT '邮箱',
	remarks varchar(255) COMMENT '备注',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '部门表';


CREATE TABLE sys_role
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	office_id bigint COMMENT '归属机构',
	name varchar(100) NOT NULL COMMENT '角色名称',
	data_scope char(1) COMMENT '数据范围（0：所有数据；1：所在公司及以下数据；2：所在公司数据；3：所在部门及以下数据；4：所在部门数据；8：仅本人数据；9：按明细设置）',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '角色表';


CREATE TABLE sys_role_menu
(
	role_id bigint NOT NULL COMMENT '角色编号',
	menu_id bigint NOT NULL COMMENT '菜单编号',
	PRIMARY KEY (role_id, menu_id)
) COMMENT = '角色-菜单';


CREATE TABLE sys_role_office
(
	role_id bigint NOT NULL COMMENT '角色编号',
	office_id bigint NOT NULL COMMENT '机构编号',
	PRIMARY KEY (role_id, office_id)
) COMMENT = '角色-机构';


CREATE TABLE sys_user
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	company_id bigint NOT NULL COMMENT '归属公司',
	office_id bigint NOT NULL COMMENT '归属部门',
	login_name varchar(100) NOT NULL COMMENT '登录名',
	password varchar(100) NOT NULL COMMENT '密码',
	no varchar(100) COMMENT '工号',
	name varchar(100) NOT NULL COMMENT '姓名',
	email varchar(200) COMMENT '邮箱',
	phone varchar(200) COMMENT '电话',
	mobile varchar(200) COMMENT '手机',
	remarks varchar(255) COMMENT '备注',
	user_type char(1) COMMENT '用户类型',
	create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	login_ip varchar(100) COMMENT '最后登陆IP',
	login_date timestamp COMMENT '最后登陆时间',
	PRIMARY KEY (id)
) COMMENT = '用户表';


CREATE TABLE sys_user_role
(
	user_id bigint NOT NULL COMMENT '用户编号',
	role_id bigint NOT NULL COMMENT '角色编号',
	PRIMARY KEY (user_id, role_id)
) COMMENT = '用户-角色';



/* Create Indexes */

CREATE INDEX sys_area_parent_id ON sys_area (parent_id ASC);
CREATE INDEX sys_area_parent_ids ON sys_area (parent_ids ASC);
CREATE INDEX sys_area_del_flag ON sys_area (del_flag ASC);
CREATE INDEX sys_dict_value ON sys_dict (value ASC);
CREATE INDEX sys_dict_label ON sys_dict (label ASC);
CREATE INDEX sys_dict_del_flag ON sys_dict (del_flag ASC);
CREATE INDEX sys_mdict_parent_id ON sys_mdict (parent_id ASC);
CREATE INDEX sys_mdict_parent_ids ON sys_mdict (parent_ids ASC);
CREATE INDEX sys_mdict_del_flag ON sys_mdict (del_flag ASC);
CREATE INDEX sys_menu_parent_id ON sys_menu (parent_id ASC);
CREATE INDEX sys_menu_parent_ids ON sys_menu (parent_ids ASC);
CREATE INDEX sys_menu_del_flag ON sys_menu (del_flag ASC);
CREATE INDEX sys_office_parent_id ON sys_office (parent_id ASC);
CREATE INDEX sys_office_parent_ids ON sys_office (parent_ids ASC);
CREATE INDEX sys_office_del_flag ON sys_office (del_flag ASC);
CREATE INDEX sys_role_del_flag ON sys_role (del_flag ASC);
CREATE INDEX sys_user_office_id ON sys_user (office_id ASC);
CREATE INDEX sys_user_login_name ON sys_user (login_name ASC);
CREATE INDEX sys_user_del_flag ON sys_user (del_flag ASC);
CREATE INDEX sys_user_company_id ON sys_user (company_id ASC);



