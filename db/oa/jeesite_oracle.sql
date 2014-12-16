
/* Drop Tables */

DROP TABLE oa_leave;
DROP TABLE oa_notify_record;
DROP TABLE oa_notify;
DROP TABLE OA_TEST_AUDIT;




/* Create Tables */

CREATE TABLE oa_leave
(
	id varchar2(64) NOT NULL,
	process_instance_id varchar2(64),
	start_time timestamp,
	end_time timestamp,
	leave_type varchar2(20),
	reason nvarchar2(255),
	apply_time timestamp,
	reality_start_time timestamp,
	reality_end_time timestamp,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE oa_notify
(
	id varchar2(64) NOT NULL,
	type char(1),
	title nvarchar2(200),
	content nvarchar2(2000),
	files nvarchar2(2000),
	status char(1),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE oa_notify_record
(
	id varchar2(64) NOT NULL,
	oa_notify_id varchar2(64),
	user_id varchar2(64),
	read_flag char(1) DEFAULT '0',
	read_date date,
	PRIMARY KEY (id)
);


CREATE TABLE OA_TEST_AUDIT
(
	id varchar2(64) NOT NULL,
	PROC_INS_ID varchar2(64),
	USER_ID varchar2(64),
	OFFICE_ID varchar2(64),
	POST nvarchar2(255),
	AGE char(1),
	EDU nvarchar2(255),
	CONTENT nvarchar2(255),
	OLDA varchar2(255),
	OLDB varchar2(255),
	OLDC varchar2(255),
	NEWA varchar2(255),
	NEWB varchar2(255),
	NEWC varchar2(255),
	ADD_NUM varchar2(255),
	EXE_DATE varchar2(255),
	HR_TEXT nvarchar2(255),
	LEAD_TEXT nvarchar2(255),
	MAIN_LEAD_TEXT nvarchar2(255),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	CONSTRAINT SYS_C0016476 PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX oa_leave_create_by ON oa_leave (create_by);
CREATE INDEX oa_leave_process_instance_id ON oa_leave (process_instance_id);
CREATE INDEX oa_leave_del_flag ON oa_leave (del_flag);
CREATE INDEX oa_notify_del_flag ON oa_notify (del_flag);
CREATE INDEX oa_notify_record_notify_id ON oa_notify_record (oa_notify_id);
CREATE INDEX oa_notify_record_user_id ON oa_notify_record (user_id);
CREATE INDEX oa_notify_record_read_flag ON oa_notify_record (read_flag);
CREATE INDEX OA_TEST_AUDIT_del_flag ON OA_TEST_AUDIT (del_flag);



/* Comments */

COMMENT ON TABLE oa_leave IS '请假流程表';
COMMENT ON COLUMN oa_leave.id IS '编号';
COMMENT ON COLUMN oa_leave.process_instance_id IS '流程实例编号';
COMMENT ON COLUMN oa_leave.start_time IS '开始时间';
COMMENT ON COLUMN oa_leave.end_time IS '结束时间';
COMMENT ON COLUMN oa_leave.leave_type IS '请假类型';
COMMENT ON COLUMN oa_leave.reason IS '请假理由';
COMMENT ON COLUMN oa_leave.apply_time IS '申请时间';
COMMENT ON COLUMN oa_leave.reality_start_time IS '实际开始时间';
COMMENT ON COLUMN oa_leave.reality_end_time IS '实际结束时间';
COMMENT ON COLUMN oa_leave.create_by IS '创建者';
COMMENT ON COLUMN oa_leave.create_date IS '创建时间';
COMMENT ON COLUMN oa_leave.update_by IS '更新者';
COMMENT ON COLUMN oa_leave.update_date IS '更新时间';
COMMENT ON COLUMN oa_leave.remarks IS '备注信息';
COMMENT ON COLUMN oa_leave.del_flag IS '删除标记';
COMMENT ON TABLE oa_notify IS '通知通告';
COMMENT ON COLUMN oa_notify.id IS '编号';
COMMENT ON COLUMN oa_notify.type IS '类型';
COMMENT ON COLUMN oa_notify.title IS '标题';
COMMENT ON COLUMN oa_notify.content IS '内容';
COMMENT ON COLUMN oa_notify.files IS '附件';
COMMENT ON COLUMN oa_notify.status IS '状态';
COMMENT ON COLUMN oa_notify.create_by IS '创建者';
COMMENT ON COLUMN oa_notify.create_date IS '创建时间';
COMMENT ON COLUMN oa_notify.update_by IS '更新者';
COMMENT ON COLUMN oa_notify.update_date IS '更新时间';
COMMENT ON COLUMN oa_notify.remarks IS '备注信息';
COMMENT ON COLUMN oa_notify.del_flag IS '删除标记';
COMMENT ON TABLE oa_notify_record IS '通知通告发送记录';
COMMENT ON COLUMN oa_notify_record.id IS '编号';
COMMENT ON COLUMN oa_notify_record.oa_notify_id IS '通知通告ID';
COMMENT ON COLUMN oa_notify_record.user_id IS '接受人';
COMMENT ON COLUMN oa_notify_record.read_flag IS '阅读标记';
COMMENT ON COLUMN oa_notify_record.read_date IS '阅读时间';
COMMENT ON TABLE OA_TEST_AUDIT IS '审批流程测试表';
COMMENT ON COLUMN OA_TEST_AUDIT.id IS '编号';
COMMENT ON COLUMN OA_TEST_AUDIT.PROC_INS_ID IS '流程实例ID';
COMMENT ON COLUMN OA_TEST_AUDIT.USER_ID IS '变动用户';
COMMENT ON COLUMN OA_TEST_AUDIT.OFFICE_ID IS '归属部门';
COMMENT ON COLUMN OA_TEST_AUDIT.POST IS '岗位';
COMMENT ON COLUMN OA_TEST_AUDIT.AGE IS '性别';
COMMENT ON COLUMN OA_TEST_AUDIT.EDU IS '学历';
COMMENT ON COLUMN OA_TEST_AUDIT.CONTENT IS '调整原因';
COMMENT ON COLUMN OA_TEST_AUDIT.OLDA IS '现行标准 薪酬档级';
COMMENT ON COLUMN OA_TEST_AUDIT.OLDB IS '现行标准 月工资额';
COMMENT ON COLUMN OA_TEST_AUDIT.OLDC IS '现行标准 年薪总额';
COMMENT ON COLUMN OA_TEST_AUDIT.NEWA IS '调整后标准 薪酬档级';
COMMENT ON COLUMN OA_TEST_AUDIT.NEWB IS '调整后标准 月工资额';
COMMENT ON COLUMN OA_TEST_AUDIT.NEWC IS '调整后标准 年薪总额';
COMMENT ON COLUMN OA_TEST_AUDIT.ADD_NUM IS '月增资';
COMMENT ON COLUMN OA_TEST_AUDIT.EXE_DATE IS '执行时间';
COMMENT ON COLUMN OA_TEST_AUDIT.HR_TEXT IS '人力资源部门意见';
COMMENT ON COLUMN OA_TEST_AUDIT.LEAD_TEXT IS '分管领导意见';
COMMENT ON COLUMN OA_TEST_AUDIT.MAIN_LEAD_TEXT IS '集团主要领导意见';
COMMENT ON COLUMN OA_TEST_AUDIT.create_by IS '创建者';
COMMENT ON COLUMN OA_TEST_AUDIT.create_date IS '创建时间';
COMMENT ON COLUMN OA_TEST_AUDIT.update_by IS '更新者';
COMMENT ON COLUMN OA_TEST_AUDIT.update_date IS '更新时间';
COMMENT ON COLUMN OA_TEST_AUDIT.remarks IS '备注信息';
COMMENT ON COLUMN OA_TEST_AUDIT.del_flag IS '删除标记';



