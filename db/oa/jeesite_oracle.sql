
/* Drop Tables */

DROP TABLE oa_leave;




/* Create Tables */

CREATE TABLE oa_leave
(
	id varchar2(64) NOT NULL,
	process_instance_id varchar2(64),
	start_time timestamp,
	end_time timestamp,
	leave_type varchar2(20),
	reason varchar2(255),
	apply_time timestamp,
	reality_start_time timestamp,
	reality_end_time timestamp,
	process_status varchar2(50),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX oa_leave_create_by ON oa_leave (create_by);
CREATE INDEX oa_leave_process_instance_id ON oa_leave (process_instance_id);
CREATE INDEX oa_leave_del_flag ON oa_leave (del_flag);
CREATE INDEX oa_leave_id ON oa_leave (id);



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
COMMENT ON COLUMN oa_leave.process_status IS '流程状态';
COMMENT ON COLUMN oa_leave.create_by IS '创建者';
COMMENT ON COLUMN oa_leave.create_date IS '创建时间';
COMMENT ON COLUMN oa_leave.update_by IS '更新者';
COMMENT ON COLUMN oa_leave.update_date IS '更新时间';
COMMENT ON COLUMN oa_leave.remarks IS '备注信息';
COMMENT ON COLUMN oa_leave.del_flag IS '删除标记';



