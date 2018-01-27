
/* Drop Tables */

DROP TABLE test_data CASCADE CONSTRAINTS;
DROP TABLE test_data_child CASCADE CONSTRAINTS;
DROP TABLE test_tree CASCADE CONSTRAINTS;




/* Create Tables */

-- 测试数据
CREATE TABLE test_data
(
	id varchar2(64) NOT NULL,
	test_input varchar2(200),
	test_textarea varchar2(200),
	test_select varchar2(10),
	test_select_multiple varchar2(200),
	test_radio varchar2(10),
	test_checkbox varchar2(200),
	test_date timestamp,
	test_datetime timestamp,
	test_user_code varchar2(64),
	test_office_code varchar2(64),
	test_area_code varchar2(64),
	test_area_name nvarchar2(100),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	PRIMARY KEY (id)
);


-- 测试数据子表
CREATE TABLE test_data_child
(
	id varchar2(64) NOT NULL,
	test_sort number(10,0),
	test_data_id varchar2(64),
	test_input varchar2(200),
	test_textarea varchar2(200),
	test_select varchar2(10),
	test_select_multiple varchar2(200),
	test_radio varchar2(10),
	test_checkbox varchar2(200),
	test_date timestamp,
	test_datetime timestamp,
	test_user_code varchar2(64),
	test_office_code varchar2(64),
	test_area_code varchar2(64),
	test_area_name nvarchar2(100),
	PRIMARY KEY (id)
);


-- 测试树表
CREATE TABLE test_tree
(
	id varchar2(64) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(2000) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(1200) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names varchar2(2000) NOT NULL,
	tree_name nvarchar2(200) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (id)
);



/* Comments */

COMMENT ON TABLE test_data IS '测试数据';
COMMENT ON COLUMN test_data.id IS '编号';
COMMENT ON COLUMN test_data.test_input IS '单行文本';
COMMENT ON COLUMN test_data.test_textarea IS '多行文本';
COMMENT ON COLUMN test_data.test_select IS '下拉框';
COMMENT ON COLUMN test_data.test_select_multiple IS '下拉多选';
COMMENT ON COLUMN test_data.test_radio IS '单选框';
COMMENT ON COLUMN test_data.test_checkbox IS '复选框';
COMMENT ON COLUMN test_data.test_date IS '日期选择';
COMMENT ON COLUMN test_data.test_datetime IS '日期时间';
COMMENT ON COLUMN test_data.test_user_code IS '用户选择';
COMMENT ON COLUMN test_data.test_office_code IS '部门选择';
COMMENT ON COLUMN test_data.test_area_code IS '区域选择';
COMMENT ON COLUMN test_data.test_area_name IS '区域名称';
COMMENT ON COLUMN test_data.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN test_data.create_by IS '创建者';
COMMENT ON COLUMN test_data.create_date IS '创建时间';
COMMENT ON COLUMN test_data.update_by IS '更新者';
COMMENT ON COLUMN test_data.update_date IS '更新时间';
COMMENT ON COLUMN test_data.remarks IS '备注信息';
COMMENT ON COLUMN test_data.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN test_data.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN test_data.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN test_data.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN test_data.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN test_data.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN test_data.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN test_data.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN test_data.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN test_data.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN test_data.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN test_data.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN test_data.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN test_data.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN test_data.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN test_data.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN test_data.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN test_data.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN test_data.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN test_data.extend_d4 IS '扩展 Date 4';
COMMENT ON TABLE test_data_child IS '测试数据子表';
COMMENT ON COLUMN test_data_child.id IS '编号';
COMMENT ON COLUMN test_data_child.test_sort IS '排序号';
COMMENT ON COLUMN test_data_child.test_data_id IS '父表主键';
COMMENT ON COLUMN test_data_child.test_input IS '单行文本';
COMMENT ON COLUMN test_data_child.test_textarea IS '多行文本';
COMMENT ON COLUMN test_data_child.test_select IS '下拉框';
COMMENT ON COLUMN test_data_child.test_select_multiple IS '下拉多选';
COMMENT ON COLUMN test_data_child.test_radio IS '单选框';
COMMENT ON COLUMN test_data_child.test_checkbox IS '复选框';
COMMENT ON COLUMN test_data_child.test_date IS '日期选择';
COMMENT ON COLUMN test_data_child.test_datetime IS '日期时间';
COMMENT ON COLUMN test_data_child.test_user_code IS '用户选择';
COMMENT ON COLUMN test_data_child.test_office_code IS '部门选择';
COMMENT ON COLUMN test_data_child.test_area_code IS '区域选择';
COMMENT ON COLUMN test_data_child.test_area_name IS '区域名称';
COMMENT ON TABLE test_tree IS '测试树表';
COMMENT ON COLUMN test_tree.id IS '编号';
COMMENT ON COLUMN test_tree.parent_code IS '父级编号';
COMMENT ON COLUMN test_tree.parent_codes IS '所有父级编号';
COMMENT ON COLUMN test_tree.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN test_tree.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN test_tree.tree_leaf IS '是否最末级';
COMMENT ON COLUMN test_tree.tree_level IS '层次级别';
COMMENT ON COLUMN test_tree.tree_names IS '全节点名';
COMMENT ON COLUMN test_tree.tree_name IS '树节点名';
COMMENT ON COLUMN test_tree.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN test_tree.create_by IS '创建者';
COMMENT ON COLUMN test_tree.create_date IS '创建时间';
COMMENT ON COLUMN test_tree.update_by IS '更新者';
COMMENT ON COLUMN test_tree.update_date IS '更新时间';
COMMENT ON COLUMN test_tree.remarks IS '备注信息';



