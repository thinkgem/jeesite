SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- APP意见反馈
CREATE TABLE ${_prefix}app_comment
(
	id varchar(64) NOT NULL COMMENT '编号',
	category varchar(10) COMMENT '问题分类',
	content varchar(500) COMMENT '问题和意见',
	contact varchar(200) COMMENT '联系方式',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	create_by_name varchar(200) COMMENT '提问人员姓名',
	device_info varchar(4000) COMMENT '设备信息',
	reply_date date COMMENT '回复时间',
	reply_content varchar(500) COMMENT '回复意见',
	reply_user_code varchar(64) COMMENT '回复人员',
	reply_user_name varchar(200) COMMENT '回复人员姓名',
	PRIMARY KEY (id)
) COMMENT = 'APP意见反馈';


-- APP升级版本
CREATE TABLE ${_prefix}app_upgrade
(
	id varchar(64) NOT NULL COMMENT '编号',
	app_code varchar(64) COMMENT '应用编号',
	up_title varchar(200) COMMENT '升级标题',
	up_content varchar(1000) COMMENT '升级内容',
	up_version numeric COMMENT '升级版本',
	up_type char(1) COMMENT '升级类型',
	up_date date COMMENT '发布时间',
	apk_url varchar(500) COMMENT 'APK下载地址',
	res_url varchar(500) COMMENT '资源下载地址',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = 'APP升级版本';



