

/* Create Tables */

-- APP意见反馈
CREATE TABLE js_app_comment
(
	id varchar(64) NOT NULL,
	category varchar(10),
	content varchar(500),
	contact varchar(200),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	create_by_name varchar(200),
	device_info varchar(4000),
	reply_date date,
	reply_content varchar(500),
	reply_user_code varchar(64),
	reply_user_name varchar(200),
	PRIMARY KEY (id)
) WITHOUT OIDS;


-- APP升级版本
CREATE TABLE js_app_upgrade
(
	id varchar(64) NOT NULL,
	app_code varchar(64),
	up_title varchar(200),
	up_content varchar(1000),
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
	remarks varchar(500),
	PRIMARY KEY (id)
) WITHOUT OIDS;



/* Comments */

COMMENT ON TABLE js_app_comment IS 'APP意见反馈';
COMMENT ON COLUMN js_app_comment.id IS '编号';
COMMENT ON COLUMN js_app_comment.category IS '问题分类';
COMMENT ON COLUMN js_app_comment.content IS '问题和意见';
COMMENT ON COLUMN js_app_comment.contact IS '联系方式';
COMMENT ON COLUMN js_app_comment.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_app_comment.create_by IS '创建者';
COMMENT ON COLUMN js_app_comment.create_date IS '创建时间';
COMMENT ON COLUMN js_app_comment.update_by IS '更新者';
COMMENT ON COLUMN js_app_comment.update_date IS '更新时间';
COMMENT ON COLUMN js_app_comment.remarks IS '备注信息';
COMMENT ON COLUMN js_app_comment.create_by_name IS '提问人员姓名';
COMMENT ON COLUMN js_app_comment.device_info IS '设备信息';
COMMENT ON COLUMN js_app_comment.reply_date IS '回复时间';
COMMENT ON COLUMN js_app_comment.reply_content IS '回复意见';
COMMENT ON COLUMN js_app_comment.reply_user_code IS '回复人员';
COMMENT ON COLUMN js_app_comment.reply_user_name IS '回复人员姓名';
COMMENT ON TABLE js_app_upgrade IS 'APP升级版本';
COMMENT ON COLUMN js_app_upgrade.id IS '编号';
COMMENT ON COLUMN js_app_upgrade.app_code IS '应用编号';
COMMENT ON COLUMN js_app_upgrade.up_title IS '升级标题';
COMMENT ON COLUMN js_app_upgrade.up_content IS '升级内容';
COMMENT ON COLUMN js_app_upgrade.up_version IS '升级版本';
COMMENT ON COLUMN js_app_upgrade.up_type IS '升级类型';
COMMENT ON COLUMN js_app_upgrade.up_date IS '发布时间';
COMMENT ON COLUMN js_app_upgrade.apk_url IS 'APK下载地址';
COMMENT ON COLUMN js_app_upgrade.res_url IS '资源下载地址';
COMMENT ON COLUMN js_app_upgrade.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_app_upgrade.create_by IS '创建者';
COMMENT ON COLUMN js_app_upgrade.create_date IS '创建时间';
COMMENT ON COLUMN js_app_upgrade.update_by IS '更新者';
COMMENT ON COLUMN js_app_upgrade.update_date IS '更新时间';
COMMENT ON COLUMN js_app_upgrade.remarks IS '备注信息';



