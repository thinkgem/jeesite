SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE oa_leave;




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
	process_status varchar(50) COMMENT '流程状态',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '请假流程表';



/* Create Indexes */

CREATE INDEX oa_leave_create_by ON oa_leave (create_by ASC);
CREATE INDEX oa_leave_process_instance_id ON oa_leave (process_instance_id ASC);
CREATE INDEX oa_leave_del_flag ON oa_leave (del_flag ASC);
CREATE INDEX oa_leave_id ON oa_leave (id ASC);



