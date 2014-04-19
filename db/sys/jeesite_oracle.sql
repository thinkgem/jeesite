
/* Drop Tables */

DROP TABLE sys_role_office;
DROP TABLE sys_user_role;
DROP TABLE sys_user;
DROP TABLE sys_office;
DROP TABLE sys_area;
DROP TABLE sys_dict;
DROP TABLE sys_log;
DROP TABLE sys_mdict;
DROP TABLE sys_role_menu;
DROP TABLE sys_menu;
DROP TABLE sys_role;




/* Create Tables */

CREATE TABLE sys_area
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	code varchar2(100),
	name varchar2(100) NOT NULL,
	type char(1),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_dict
(
	id varchar2(64) NOT NULL,
	label varchar2(100) NOT NULL,
	value varchar2(100) NOT NULL,
	type varchar2(100) NOT NULL,
	description varchar2(100) NOT NULL,
	sort number(10,0) NOT NULL,
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_log
(
	id varchar2(64) NOT NULL,
	type char(1) DEFAULT '1',
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


CREATE TABLE sys_mdict
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	name varchar2(100) NOT NULL,
	description varchar2(100),
	sort number(10,0),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_menu
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	name varchar2(100) NOT NULL,
	href varchar2(255),
	target varchar2(20),
	icon varchar2(100),
	sort number(10,0) NOT NULL,
	is_show char(1) NOT NULL,
	is_activiti char(1),
	permission varchar2(200),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_office
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	area_id varchar2(64) NOT NULL,
	code varchar2(100),
	name varchar2(100) NOT NULL,
	type char(1) NOT NULL,
	grade char(1) NOT NULL,
	address varchar2(255),
	zip_code varchar2(100),
	master varchar2(100),
	phone varchar2(200),
	fax varchar2(200),
	email varchar2(200),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role
(
	id varchar2(64) NOT NULL,
	office_id varchar2(64),
	name varchar2(100) NOT NULL,
	data_scope char(1),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role_menu
(
	role_id varchar2(64) NOT NULL,
	menu_id varchar2(64) NOT NULL,
	PRIMARY KEY (role_id, menu_id)
);


CREATE TABLE sys_role_office
(
	role_id varchar2(64) NOT NULL,
	office_id varchar2(64) NOT NULL,
	PRIMARY KEY (role_id, office_id)
);


CREATE TABLE sys_user
(
	id varchar2(64) NOT NULL,
	company_id varchar2(64) NOT NULL,
	office_id varchar2(64) NOT NULL,
	login_name varchar2(100) NOT NULL,
	password varchar2(100) NOT NULL,
	no varchar2(100),
	name varchar2(100) NOT NULL,
	email varchar2(200),
	phone varchar2(200),
	mobile varchar2(200),
	user_type char(1),
	login_ip varchar2(100),
	login_date timestamp,
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


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
CREATE INDEX sys_role_del_flag ON sys_role (del_flag);
CREATE INDEX sys_user_office_id ON sys_user (office_id);
CREATE INDEX sys_user_login_name ON sys_user (login_name);
CREATE INDEX sys_user_company_id ON sys_user (company_id);
CREATE INDEX sys_user_update_date ON sys_user (update_date);
CREATE INDEX sys_user_del_flag ON sys_user (del_flag);



/* Comments */

COMMENT ON TABLE sys_area IS '区域表';
COMMENT ON COLUMN sys_area.id IS '编号';
COMMENT ON COLUMN sys_area.parent_id IS '父级编号';
COMMENT ON COLUMN sys_area.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_area.code IS '区域编码';
COMMENT ON COLUMN sys_area.name IS '区域名称';
COMMENT ON COLUMN sys_area.type IS '区域类型';
COMMENT ON COLUMN sys_area.create_by IS '创建者';
COMMENT ON COLUMN sys_area.create_date IS '创建时间';
COMMENT ON COLUMN sys_area.update_by IS '更新者';
COMMENT ON COLUMN sys_area.update_date IS '更新时间';
COMMENT ON COLUMN sys_area.remarks IS '备注信息';
COMMENT ON COLUMN sys_area.del_flag IS '删除标记';
COMMENT ON TABLE sys_dict IS '字典表';
COMMENT ON COLUMN sys_dict.id IS '编号';
COMMENT ON COLUMN sys_dict.label IS '标签名';
COMMENT ON COLUMN sys_dict.value IS '数据值';
COMMENT ON COLUMN sys_dict.type IS '类型';
COMMENT ON COLUMN sys_dict.description IS '描述';
COMMENT ON COLUMN sys_dict.sort IS '排序（升序）';
COMMENT ON COLUMN sys_dict.create_by IS '创建者';
COMMENT ON COLUMN sys_dict.create_date IS '创建时间';
COMMENT ON COLUMN sys_dict.update_by IS '更新者';
COMMENT ON COLUMN sys_dict.update_date IS '更新时间';
COMMENT ON COLUMN sys_dict.remarks IS '备注信息';
COMMENT ON COLUMN sys_dict.del_flag IS '删除标记';
COMMENT ON TABLE sys_log IS '日志表';
COMMENT ON COLUMN sys_log.id IS '编号';
COMMENT ON COLUMN sys_log.type IS '日志类型';
COMMENT ON COLUMN sys_log.create_by IS '创建者';
COMMENT ON COLUMN sys_log.create_date IS '创建时间';
COMMENT ON COLUMN sys_log.remote_addr IS '操作IP地址';
COMMENT ON COLUMN sys_log.user_agent IS '用户代理';
COMMENT ON COLUMN sys_log.request_uri IS '请求URI';
COMMENT ON COLUMN sys_log.method IS '操作方式';
COMMENT ON COLUMN sys_log.params IS '操作提交的数据';
COMMENT ON COLUMN sys_log.exception IS '异常信息';
COMMENT ON TABLE sys_mdict IS '多级字典表';
COMMENT ON COLUMN sys_mdict.id IS '编号';
COMMENT ON COLUMN sys_mdict.parent_id IS '父级编号';
COMMENT ON COLUMN sys_mdict.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_mdict.name IS '角色名称';
COMMENT ON COLUMN sys_mdict.description IS '描述';
COMMENT ON COLUMN sys_mdict.sort IS '排序（升序）';
COMMENT ON COLUMN sys_mdict.create_by IS '创建者';
COMMENT ON COLUMN sys_mdict.create_date IS '创建时间';
COMMENT ON COLUMN sys_mdict.update_by IS '更新者';
COMMENT ON COLUMN sys_mdict.update_date IS '更新时间';
COMMENT ON COLUMN sys_mdict.remarks IS '备注信息';
COMMENT ON COLUMN sys_mdict.del_flag IS '删除标记';
COMMENT ON TABLE sys_menu IS '菜单表';
COMMENT ON COLUMN sys_menu.id IS '编号';
COMMENT ON COLUMN sys_menu.parent_id IS '父级编号';
COMMENT ON COLUMN sys_menu.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_menu.name IS '菜单名称';
COMMENT ON COLUMN sys_menu.href IS '链接';
COMMENT ON COLUMN sys_menu.target IS '目标';
COMMENT ON COLUMN sys_menu.icon IS '图标';
COMMENT ON COLUMN sys_menu.sort IS '排序（升序）';
COMMENT ON COLUMN sys_menu.is_show IS '是否在菜单中显示';
COMMENT ON COLUMN sys_menu.is_activiti IS '是否同步工作流';
COMMENT ON COLUMN sys_menu.permission IS '权限标识';
COMMENT ON COLUMN sys_menu.create_by IS '创建者';
COMMENT ON COLUMN sys_menu.create_date IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by IS '更新者';
COMMENT ON COLUMN sys_menu.update_date IS '更新时间';
COMMENT ON COLUMN sys_menu.remarks IS '备注信息';
COMMENT ON COLUMN sys_menu.del_flag IS '删除标记';
COMMENT ON TABLE sys_office IS '机构表';
COMMENT ON COLUMN sys_office.id IS '编号';
COMMENT ON COLUMN sys_office.parent_id IS '父级编号';
COMMENT ON COLUMN sys_office.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_office.area_id IS '归属区域';
COMMENT ON COLUMN sys_office.code IS '区域编码';
COMMENT ON COLUMN sys_office.name IS '机构名称';
COMMENT ON COLUMN sys_office.type IS '机构类型';
COMMENT ON COLUMN sys_office.grade IS '机构等级';
COMMENT ON COLUMN sys_office.address IS '联系地址';
COMMENT ON COLUMN sys_office.zip_code IS '邮政编码';
COMMENT ON COLUMN sys_office.master IS '负责人';
COMMENT ON COLUMN sys_office.phone IS '电话';
COMMENT ON COLUMN sys_office.fax IS '传真';
COMMENT ON COLUMN sys_office.email IS '邮箱';
COMMENT ON COLUMN sys_office.create_by IS '创建者';
COMMENT ON COLUMN sys_office.create_date IS '创建时间';
COMMENT ON COLUMN sys_office.update_by IS '更新者';
COMMENT ON COLUMN sys_office.update_date IS '更新时间';
COMMENT ON COLUMN sys_office.remarks IS '备注信息';
COMMENT ON COLUMN sys_office.del_flag IS '删除标记';
COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '编号';
COMMENT ON COLUMN sys_role.office_id IS '归属机构';
COMMENT ON COLUMN sys_role.name IS '角色名称';
COMMENT ON COLUMN sys_role.data_scope IS '数据范围';
COMMENT ON COLUMN sys_role.create_by IS '创建者';
COMMENT ON COLUMN sys_role.create_date IS '创建时间';
COMMENT ON COLUMN sys_role.update_by IS '更新者';
COMMENT ON COLUMN sys_role.update_date IS '更新时间';
COMMENT ON COLUMN sys_role.remarks IS '备注信息';
COMMENT ON COLUMN sys_role.del_flag IS '删除标记';
COMMENT ON TABLE sys_role_menu IS '角色-菜单';
COMMENT ON COLUMN sys_role_menu.role_id IS '角色编号';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单编号';
COMMENT ON TABLE sys_role_office IS '角色-机构';
COMMENT ON COLUMN sys_role_office.role_id IS '角色编号';
COMMENT ON COLUMN sys_role_office.office_id IS '机构编号';
COMMENT ON TABLE sys_user IS '用户表';
COMMENT ON COLUMN sys_user.id IS '编号';
COMMENT ON COLUMN sys_user.company_id IS '归属公司';
COMMENT ON COLUMN sys_user.office_id IS '归属部门';
COMMENT ON COLUMN sys_user.login_name IS '登录名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.no IS '工号';
COMMENT ON COLUMN sys_user.name IS '姓名';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '电话';
COMMENT ON COLUMN sys_user.mobile IS '手机';
COMMENT ON COLUMN sys_user.user_type IS '用户类型';
COMMENT ON COLUMN sys_user.login_ip IS '最后登陆IP';
COMMENT ON COLUMN sys_user.login_date IS '最后登陆时间';
COMMENT ON COLUMN sys_user.create_by IS '创建者';
COMMENT ON COLUMN sys_user.create_date IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '更新者';
COMMENT ON COLUMN sys_user.update_date IS '更新时间';
COMMENT ON COLUMN sys_user.remarks IS '备注信息';
COMMENT ON COLUMN sys_user.del_flag IS '删除标记';
COMMENT ON TABLE sys_user_role IS '用户-角色';
COMMENT ON COLUMN sys_user_role.user_id IS '用户编号';
COMMENT ON COLUMN sys_user_role.role_id IS '角色编号';



