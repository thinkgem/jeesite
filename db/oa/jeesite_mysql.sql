SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS oa_leave;
DROP TABLE IF EXISTS oa_notify_record;
DROP TABLE IF EXISTS oa_notify;
DROP TABLE IF EXISTS OA_TEST_AUDIT;




/* Create Tables */

CREATE TABLE oa_leave
(
	id varchar(64) NOT NULL COMMENT '编号',
	process_instance_id varchar(64) COMMENT '流程实例编号',
	start_time datetime COMMENT '开始时间',
	end_time datetime COMMENT '结束时间',
	leave_type varchar(20) COMMENT '请假类型',
	reason varchar(255) COMMENT '请假理由',
	apply_time datetime COMMENT '申请时间',
	reality_start_time datetime COMMENT '实际开始时间',
	reality_end_time datetime COMMENT '实际结束时间',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '请假流程表';


CREATE TABLE oa_notify
(
	id varchar(64) NOT NULL COMMENT '编号',
	type char(1) COMMENT '类型',
	title varchar(200) COMMENT '标题',
	content varchar(2000) COMMENT '内容',
	files varchar(2000) COMMENT '附件',
	status char(1) COMMENT '状态',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '通知通告';


CREATE TABLE oa_notify_record
(
	id varchar(64) NOT NULL COMMENT '编号',
	oa_notify_id varchar(64) COMMENT '通知通告ID',
	user_id varchar(64) COMMENT '接受人',
	read_flag char(1) DEFAULT '0' COMMENT '阅读标记',
	read_date date COMMENT '阅读时间',
	PRIMARY KEY (id)
) COMMENT = '通知通告发送记录';


CREATE TABLE OA_TEST_AUDIT
(
	id varchar(64) NOT NULL COMMENT '编号',
	PROC_INS_ID varchar(64) COMMENT '流程实例ID',
	USER_ID varchar(64) COMMENT '变动用户',
	OFFICE_ID varchar(64) COMMENT '归属部门',
	POST varchar(255) COMMENT '岗位',
	AGE char(1) COMMENT '性别',
	EDU varchar(255) COMMENT '学历',
	CONTENT varchar(255) COMMENT '调整原因',
	OLDA varchar(255) COMMENT '现行标准 薪酬档级',
	OLDB varchar(255) COMMENT '现行标准 月工资额',
	OLDC varchar(255) COMMENT '现行标准 年薪总额',
	NEWA varchar(255) COMMENT '调整后标准 薪酬档级',
	NEWB varchar(255) COMMENT '调整后标准 月工资额',
	NEWC varchar(255) COMMENT '调整后标准 年薪总额',
	ADD_NUM varchar(255) COMMENT '月增资',
	EXE_DATE varchar(255) COMMENT '执行时间',
	HR_TEXT varchar(255) COMMENT '人力资源部门意见',
	LEAD_TEXT varchar(255) COMMENT '分管领导意见',
	MAIN_LEAD_TEXT varchar(255) COMMENT '集团主要领导意见',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	CONSTRAINT SYS_C0016476 PRIMARY KEY (id)
) COMMENT = '审批流程测试表';



/* Create Indexes */

CREATE INDEX oa_leave_create_by ON oa_leave (create_by ASC);
CREATE INDEX oa_leave_process_instance_id ON oa_leave (process_instance_id ASC);
CREATE INDEX oa_leave_del_flag ON oa_leave (del_flag ASC);
CREATE INDEX oa_notify_del_flag ON oa_notify (del_flag ASC);
CREATE INDEX oa_notify_record_notify_id ON oa_notify_record (oa_notify_id ASC);
CREATE INDEX oa_notify_record_user_id ON oa_notify_record (user_id ASC);
CREATE INDEX oa_notify_record_read_flag ON oa_notify_record (read_flag ASC);
CREATE INDEX OA_TEST_AUDIT_del_flag ON OA_TEST_AUDIT (del_flag ASC);



