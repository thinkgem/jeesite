

/* Drop Tables */

DROP TABLE oa_leave;
DROP TABLE oa_notify_record;
DROP TABLE oa_notify;
DROP TABLE OA_TEST_AUDIT;




/* Create Tables */

CREATE TABLE oa_leave
(
	id varchar(64) NOT NULL,
	process_instance_id varchar(64),
	start_time datetime,
	end_time datetime,
	leave_type varchar(20),
	reason varchar(255),
	apply_time datetime,
	reality_start_time datetime,
	reality_end_time datetime,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE oa_notify
(
	id varchar(64) NOT NULL,
	type char(1),
	title varchar(200),
	content varchar(2000),
	files varchar(2000),
	status char(1),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE oa_notify_record
(
	id varchar(64) NOT NULL,
	oa_notify_id varchar(64),
	user_id varchar(64),
	read_flag char(1) DEFAULT '0',
	read_date smalldatetime,
	PRIMARY KEY (id)
);


CREATE TABLE OA_TEST_AUDIT
(
	id varchar(64) NOT NULL,
	PROC_INS_ID varchar(64),
	USER_ID varchar(64),
	OFFICE_ID varchar(64),
	POST varchar(255),
	AGE char(1),
	EDU varchar(255),
	CONTENT varchar(255),
	OLDA varchar(255),
	OLDB varchar(255),
	OLDC varchar(255),
	NEWA varchar(255),
	NEWB varchar(255),
	NEWC varchar(255),
	ADD_NUM varchar(255),
	EXE_DATE varchar(255),
	HR_TEXT varchar(255),
	LEAD_TEXT varchar(255),
	MAIN_LEAD_TEXT varchar(255),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	CONSTRAINT SYS_C0016476 PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX oa_leave_create_by ON oa_leave (create_by ASC);
CREATE INDEX oa_leave_process_instance_id ON oa_leave (process_instance_id ASC);
CREATE INDEX oa_leave_del_flag ON oa_leave (del_flag ASC);
CREATE INDEX oa_notify_del_flag ON oa_notify (del_flag ASC);
CREATE INDEX oa_notify_record_notify_id ON oa_notify_record (oa_notify_id ASC);
CREATE INDEX oa_notify_record_user_id ON oa_notify_record (user_id ASC);
CREATE INDEX oa_notify_record_read_flag ON oa_notify_record (read_flag ASC);
CREATE INDEX OA_TEST_AUDIT_del_flag ON OA_TEST_AUDIT (del_flag ASC);



