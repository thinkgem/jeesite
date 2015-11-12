

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
	id varchar(64) NOT NULL,
	parent_id varchar(64) NOT NULL,
	parent_ids varchar(2000) NOT NULL,
	name varchar(100) NOT NULL,
	sort decimal(10,0) NOT NULL,
	code varchar(100),
	type char(1),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_dict
(
	id varchar(64) NOT NULL,
	value varchar(100) NOT NULL,
	label varchar(100) NOT NULL,
	type varchar(100) NOT NULL,
	description varchar(100) NOT NULL,
	sort decimal(10,0) NOT NULL,
	parent_id varchar(64) DEFAULT '0',
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_log
(
	id varchar(64) NOT NULL,
	type char(1) DEFAULT '1',
	title varchar(255) DEFAULT '',
	create_by varchar(64),
	create_date datetime,
	remote_addr varchar(255),
	user_agent varchar(255),
	request_uri varchar(255),
	method varchar(5),
	params text,
	exception text,
	PRIMARY KEY (id)
);


CREATE TABLE sys_mdict
(
	id varchar(64) NOT NULL,
	parent_id varchar(64) NOT NULL,
	parent_ids varchar(2000) NOT NULL,
	name varchar(100) NOT NULL,
	sort decimal(10,0) NOT NULL,
	description varchar(100),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_menu
(
	id varchar(64) NOT NULL,
	parent_id varchar(64) NOT NULL,
	parent_ids varchar(2000) NOT NULL,
	name varchar(100) NOT NULL,
	sort decimal(10,0) NOT NULL,
	href varchar(2000),
	target varchar(20),
	icon varchar(100),
	is_show char(1) NOT NULL,
	permission varchar(200),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_office
(
	id varchar(64) NOT NULL,
	parent_id varchar(64) NOT NULL,
	parent_ids varchar(2000) NOT NULL,
	name varchar(100) NOT NULL,
	sort decimal(10,0) NOT NULL,
	area_id varchar(64) NOT NULL,
	code varchar(100),
	type char(1) NOT NULL,
	grade char(1) NOT NULL,
	address varchar(255),
	zip_code varchar(100),
	master varchar(100),
	phone varchar(200),
	fax varchar(200),
	email varchar(200),
	USEABLE varchar(64),
	PRIMARY_PERSON varchar(64),
	DEPUTY_PERSON varchar(64),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role
(
	id varchar(64) NOT NULL,
	office_id varchar(64),
	name varchar(100) NOT NULL,
	enname varchar(255),
	role_type varchar(255),
	data_scope char(1),
	is_sys varchar(64),
	useable varchar(64),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role_menu
(
	role_id varchar(64) NOT NULL,
	menu_id varchar(64) NOT NULL,
	PRIMARY KEY (role_id, menu_id)
);


CREATE TABLE sys_role_office
(
	role_id varchar(64) NOT NULL,
	office_id varchar(64) NOT NULL,
	PRIMARY KEY (role_id, office_id)
);


CREATE TABLE sys_user
(
	id varchar(64) NOT NULL,
	company_id varchar(64) NOT NULL,
	office_id varchar(64) NOT NULL,
	login_name varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	no varchar(100),
	name varchar(100) NOT NULL,
	email varchar(200),
	phone varchar(200),
	mobile varchar(200),
	user_type char(1),
	photo varchar(1000),
	login_ip varchar(100),
	login_date datetime,
	login_flag varchar(64),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_user_role
(
	user_id varchar(64) NOT NULL,
	role_id varchar(64) NOT NULL,
	PRIMARY KEY (user_id, role_id)
);



/* Create Indexes */

CREATE INDEX sys_area_parent_id ON sys_area (parent_id ASC);
/*CREATE INDEX sys_area_parent_ids ON sys_area (parent_ids ASC);*/
CREATE INDEX sys_area_del_flag ON sys_area (del_flag ASC);
CREATE INDEX sys_dict_value ON sys_dict (value ASC);
CREATE INDEX sys_dict_label ON sys_dict (label ASC);
CREATE INDEX sys_dict_del_flag ON sys_dict (del_flag ASC);
CREATE INDEX sys_log_create_by ON sys_log (create_by ASC);
CREATE INDEX sys_log_request_uri ON sys_log (request_uri ASC);
CREATE INDEX sys_log_type ON sys_log (type ASC);
CREATE INDEX sys_log_create_date ON sys_log (create_date ASC);
CREATE INDEX sys_mdict_parent_id ON sys_mdict (parent_id ASC);
/*CREATE INDEX sys_mdict_parent_ids ON sys_mdict (parent_ids ASC);*/
CREATE INDEX sys_mdict_del_flag ON sys_mdict (del_flag ASC);
CREATE INDEX sys_menu_parent_id ON sys_menu (parent_id ASC);
/*CREATE INDEX sys_menu_parent_ids ON sys_menu (parent_ids ASC);*/
CREATE INDEX sys_menu_del_flag ON sys_menu (del_flag ASC);
CREATE INDEX sys_office_parent_id ON sys_office (parent_id ASC);
/*CREATE INDEX sys_office_parent_ids ON sys_office (parent_ids ASC);*/
CREATE INDEX sys_office_del_flag ON sys_office (del_flag ASC);
CREATE INDEX sys_office_type ON sys_office (type ASC);
CREATE INDEX sys_role_del_flag ON sys_role (del_flag ASC);
CREATE INDEX sys_role_enname ON sys_role (enname ASC);
CREATE INDEX sys_user_office_id ON sys_user (office_id ASC);
CREATE INDEX sys_user_login_name ON sys_user (login_name ASC);
CREATE INDEX sys_user_company_id ON sys_user (company_id ASC);
CREATE INDEX sys_user_update_date ON sys_user (update_date ASC);
CREATE INDEX sys_user_del_flag ON sys_user (del_flag ASC);



