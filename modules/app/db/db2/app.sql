

/* Create Tables */

-- APP意见反馈
CREATE TABLE js_app_comment
(
	id varchar(64) NOT NULL,
	category varchar(10),
	content vargraphic(500),
	contact vargraphic(200),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	create_by_name varchar(200),
	device_info varchar(4000),
	reply_date date,
	reply_content vargraphic(500),
	reply_user_code varchar(64),
	reply_user_name varchar(200),
	PRIMARY KEY (id)
);


-- APP升级版本
CREATE TABLE js_app_upgrade
(
	id varchar(64) NOT NULL,
	app_code varchar(64),
	up_title vargraphic(200),
	up_content vargraphic(1000),
	up_version numeric,
	up_type char(1),
	up_date date,
	apk_url varchar(500),
	res_url varchar(500),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	PRIMARY KEY (id)
);



