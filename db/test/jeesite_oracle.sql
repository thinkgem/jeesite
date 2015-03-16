
/* Drop Tables */

DROP TABLE test_data;
DROP TABLE test_data_child;
DROP TABLE test_data_main;
DROP TABLE test_tree;




/* Create Tables */

CREATE TABLE test_data
(
	id varchar2(64) NOT NULL,
	user_id varchar2(64),
	office_id varchar2(64),
	area_id nvarchar2(64),
	name nvarchar2(100),
	sex char(1),
	in_date date,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE test_data_child
(
	id varchar2(64) NOT NULL,
	test_data_main_id varchar2(64),
	name nvarchar2(100),
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE test_data_main
(
	id varchar2(64) NOT NULL,
	user_id varchar2(64),
	office_id varchar2(64),
	area_id nvarchar2(64),
	name nvarchar2(100),
	sex char(1),
	in_date date,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE test_tree
(
	id varchar2(64) NOT NULL,
	parent_id varchar2(64) NOT NULL,
	parent_ids varchar2(2000) NOT NULL,
	name nvarchar2(100) NOT NULL,
	sort number(10,0) NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX test_data_del_flag ON test_data (del_flag);
CREATE INDEX test_data_child_del_flag ON test_data_child (del_flag);
CREATE INDEX test_data_main_del_flag ON test_data_main (del_flag);
CREATE INDEX test_tree_del_flag ON test_tree (del_flag);
CREATE INDEX test_data_parent_id ON test_tree (parent_id);
CREATE INDEX test_data_parent_ids ON test_tree (parent_ids);



/* Comments */

COMMENT ON TABLE test_data IS '业务数据表';
COMMENT ON COLUMN test_data.id IS '编号';
COMMENT ON COLUMN test_data.user_id IS '归属用户';
COMMENT ON COLUMN test_data.office_id IS '归属部门';
COMMENT ON COLUMN test_data.area_id IS '归属区域';
COMMENT ON COLUMN test_data.name IS '名称';
COMMENT ON COLUMN test_data.sex IS '性别';
COMMENT ON COLUMN test_data.in_date IS '加入日期';
COMMENT ON COLUMN test_data.create_by IS '创建者';
COMMENT ON COLUMN test_data.create_date IS '创建时间';
COMMENT ON COLUMN test_data.update_by IS '更新者';
COMMENT ON COLUMN test_data.update_date IS '更新时间';
COMMENT ON COLUMN test_data.remarks IS '备注信息';
COMMENT ON COLUMN test_data.del_flag IS '删除标记';
COMMENT ON TABLE test_data_child IS '业务数据子表';
COMMENT ON COLUMN test_data_child.id IS '编号';
COMMENT ON COLUMN test_data_child.test_data_main_id IS '业务主表ID';
COMMENT ON COLUMN test_data_child.name IS '名称';
COMMENT ON COLUMN test_data_child.create_by IS '创建者';
COMMENT ON COLUMN test_data_child.create_date IS '创建时间';
COMMENT ON COLUMN test_data_child.update_by IS '更新者';
COMMENT ON COLUMN test_data_child.update_date IS '更新时间';
COMMENT ON COLUMN test_data_child.remarks IS '备注信息';
COMMENT ON COLUMN test_data_child.del_flag IS '删除标记';
COMMENT ON TABLE test_data_main IS '业务数据表';
COMMENT ON COLUMN test_data_main.id IS '编号';
COMMENT ON COLUMN test_data_main.user_id IS '归属用户';
COMMENT ON COLUMN test_data_main.office_id IS '归属部门';
COMMENT ON COLUMN test_data_main.area_id IS '归属区域';
COMMENT ON COLUMN test_data_main.name IS '名称';
COMMENT ON COLUMN test_data_main.sex IS '性别';
COMMENT ON COLUMN test_data_main.in_date IS '加入日期';
COMMENT ON COLUMN test_data_main.create_by IS '创建者';
COMMENT ON COLUMN test_data_main.create_date IS '创建时间';
COMMENT ON COLUMN test_data_main.update_by IS '更新者';
COMMENT ON COLUMN test_data_main.update_date IS '更新时间';
COMMENT ON COLUMN test_data_main.remarks IS '备注信息';
COMMENT ON COLUMN test_data_main.del_flag IS '删除标记';
COMMENT ON TABLE test_tree IS '树结构表';
COMMENT ON COLUMN test_tree.id IS '编号';
COMMENT ON COLUMN test_tree.parent_id IS '父级编号';
COMMENT ON COLUMN test_tree.parent_ids IS '所有父级编号';
COMMENT ON COLUMN test_tree.name IS '名称';
COMMENT ON COLUMN test_tree.sort IS '排序';
COMMENT ON COLUMN test_tree.create_by IS '创建者';
COMMENT ON COLUMN test_tree.create_date IS '创建时间';
COMMENT ON COLUMN test_tree.update_by IS '更新者';
COMMENT ON COLUMN test_tree.update_date IS '更新时间';
COMMENT ON COLUMN test_tree.remarks IS '备注信息';
COMMENT ON COLUMN test_tree.del_flag IS '删除标记';



