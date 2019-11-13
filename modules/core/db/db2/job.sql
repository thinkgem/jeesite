-- tables_db2_v95.sql

DROP TABLE js_job_FIRED_TRIGGERS;
DROP TABLE js_job_PAUSED_TRIGGER_GRPS;
DROP TABLE js_job_SCHEDULER_STATE;
DROP TABLE js_job_LOCKS;
DROP TABLE js_job_SIMPLE_TRIGGERS;
DROP TABLE js_job_SIMPROP_TRIGGERS;
DROP TABLE js_job_CRON_TRIGGERS;
DROP TABLE js_job_TRIGGERS;
DROP TABLE js_job_JOB_DETAILS;
DROP TABLE js_job_CALENDARS;
DROP TABLE js_job_BLOB_TRIGGERS;

create table js_job_job_details(
sched_name varchar(120) not null,
job_name varchar(80) not null,
job_group varchar(80) not null,
description varchar(120),
job_class_name varchar(128) not null,
is_durable integer not null,
is_nonconcurrent integer not null,
is_update_data integer not null,
requests_recovery integer not null,
job_data blob(2000),
primary key (sched_name,job_name,job_group)
);

create table js_job_triggers(
sched_name varchar(120) not null,
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
job_name varchar(80) not null,
job_group varchar(80) not null,
description varchar(120),
next_fire_time bigint,
prev_fire_time bigint,
priority integer,
trigger_state varchar(16) not null,
trigger_type varchar(8) not null,
start_time bigint not null,
end_time bigint,
calendar_name varchar(80),
misfire_instr smallint,
job_data blob(2000),
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,job_name,job_group) references js_job_job_details(sched_name,job_name,job_group)
);

create table js_job_simple_triggers(
sched_name varchar(120) not null,
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
repeat_count bigint not null,
repeat_interval bigint not null,
times_triggered bigint not null,
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,trigger_name,trigger_group) references js_job_triggers(sched_name,trigger_name,trigger_group)
);

create table js_job_cron_triggers(
sched_name varchar(120) not null,
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
cron_expression varchar(120) not null,
time_zone_id varchar(80),
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,trigger_name,trigger_group) references js_job_triggers(sched_name,trigger_name,trigger_group)
);

CREATE TABLE js_job_simprop_triggers
  (          
    sched_name varchar(120) not null,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512),
    STR_PROP_2 VARCHAR(512),
    STR_PROP_3 VARCHAR(512),
    INT_PROP_1 INT,
    INT_PROP_2 INT,
    LONG_PROP_1 BIGINT,
    LONG_PROP_2 BIGINT,
    DEC_PROP_1 NUMERIC(13,4),
    DEC_PROP_2 NUMERIC(13,4),
    BOOL_PROP_1 VARCHAR(1),
    BOOL_PROP_2 VARCHAR(1),
    PRIMARY KEY (sched_name,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (sched_name,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES js_job_TRIGGERS(sched_name,TRIGGER_NAME,TRIGGER_GROUP)
);

create table js_job_blob_triggers(
sched_name varchar(120) not null,
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
blob_data blob(2000),
primary key (sched_name,trigger_name,trigger_group),
foreign key (sched_name,trigger_name,trigger_group) references js_job_triggers(sched_name,trigger_name,trigger_group)
);

create table js_job_calendars(
sched_name varchar(120) not null,
calendar_name varchar(80) not null,
calendar blob(2000) not null,
primary key (calendar_name)
);

create table js_job_fired_triggers(
sched_name varchar(120) not null,
entry_id varchar(95) not null,
trigger_name varchar(80) not null,
trigger_group varchar(80) not null,
instance_name varchar(80) not null,
fired_time bigint not null,
sched_time bigint not null,
priority integer not null,
state varchar(16) not null,
job_name varchar(80),
job_group varchar(80),
is_nonconcurrent integer,
requests_recovery integer,
primary key (sched_name,entry_id)
);

create table js_job_paused_trigger_grps(
sched_name varchar(120) not null,
trigger_group varchar(80) not null,
primary key (sched_name,trigger_group)
);

create table js_job_scheduler_state(
sched_name varchar(120) not null,
instance_name varchar(80) not null,
last_checkin_time bigint not null,
checkin_interval bigint not null,
primary key (sched_name,instance_name)
);

create table js_job_locks(
sched_name varchar(120) not null,
lock_name varchar(40) not null,
primary key (sched_name,lock_name)
);
