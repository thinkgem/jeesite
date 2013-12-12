
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
	id bigint NOT NULL IDENTITY ,
	parent_id bigint NOT NULL,
	parent_ids nvarchar(255) NOT NULL,
	code nvarchar(100),
	name nvarchar(100) NOT NULL,
	type char(1),
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_dict
(
	id bigint NOT NULL IDENTITY ,
	label nvarchar(100) NOT NULL,
	value nvarchar(100) NOT NULL,
	type nvarchar(100) NOT NULL,
	description nvarchar(100) NOT NULL,
	sort int NOT NULL,
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_log
(
	id bigint NOT NULL IDENTITY ,
	type char(1) DEFAULT '1',
	create_by bigint,
	create_date datetime,
	remote_addr nvarchar(255),
	user_agent nvarchar(255),
	request_uri nvarchar(255),
	method nvarchar(5),
	params text,
	exception text,
	PRIMARY KEY (id)
);


CREATE TABLE sys_mdict
(
	id bigint NOT NULL IDENTITY ,
	parent_id bigint NOT NULL,
	parent_ids nvarchar(255) NOT NULL,
	name nvarchar(100) NOT NULL,
	description nvarchar(100),
	sort int,
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_menu
(
	id bigint NOT NULL IDENTITY ,
	parent_id bigint NOT NULL,
	parent_ids nvarchar(255) NOT NULL,
	name nvarchar(100) NOT NULL,
	href nvarchar(255),
	target nvarchar(20),
	icon nvarchar(100),
	sort int NOT NULL,
	is_show char(1) NOT NULL,
	is_activiti char(1) NOT NULL,
	permission nvarchar(200),
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_office
(
	id bigint NOT NULL IDENTITY ,
	parent_id bigint NOT NULL,
	parent_ids nvarchar(255) NOT NULL,
	area_id bigint NOT NULL,
	code nvarchar(100),
	name nvarchar(100) NOT NULL,
	type char(1) NOT NULL,
	grade char(1) NOT NULL,
	address nvarchar(255),
	zip_code nvarchar(100),
	master nvarchar(100),
	phone nvarchar(200),
	fax nvarchar(200),
	email nvarchar(200),
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role
(
	id bigint NOT NULL IDENTITY ,
	office_id bigint,
	name nvarchar(100) NOT NULL,
	data_scope char(1),
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role_menu
(
	role_id bigint NOT NULL,
	menu_id bigint NOT NULL,
	PRIMARY KEY (role_id, menu_id)
);


CREATE TABLE sys_role_office
(
	role_id bigint NOT NULL,
	office_id bigint NOT NULL,
	PRIMARY KEY (role_id, office_id)
);


CREATE TABLE sys_user
(
	id bigint NOT NULL IDENTITY ,
	company_id bigint NOT NULL,
	office_id bigint NOT NULL,
	login_name nvarchar(100) NOT NULL,
	password nvarchar(100) NOT NULL,
	no nvarchar(100),
	name nvarchar(100) NOT NULL,
	email nvarchar(200),
	phone nvarchar(200),
	mobile nvarchar(200),
	user_type char(1),
	login_ip nvarchar(100),
	login_date datetime,
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_user_role
(
	user_id bigint NOT NULL,
	role_id bigint NOT NULL,
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



