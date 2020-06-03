SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS test_data;
DROP TABLE IF EXISTS test_data_child;
DROP TABLE IF EXISTS test_tree;




/* Create Tables */

-- 测试数据
CREATE TABLE test_data
(
	id varchar(64) NOT NULL COMMENT '编号',
	test_input varchar(200) COMMENT '单行文本',
	test_textarea varchar(200) COMMENT '多行文本',
	test_select varchar(10) COMMENT '下拉框',
	test_select_multiple varchar(200) COMMENT '下拉多选',
	test_radio varchar(10) COMMENT '单选框',
	test_checkbox varchar(200) COMMENT '复选框',
	test_date datetime COMMENT '日期选择',
	test_datetime datetime COMMENT '日期时间',
	test_user_code varchar(64) COMMENT '用户选择',
	test_office_code varchar(64) COMMENT '机构选择',
	test_area_code varchar(64) COMMENT '区域选择',
	test_area_name varchar(100) COMMENT '区域名称',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '测试数据';


-- 测试数据子表
CREATE TABLE test_data_child
(
	id varchar(64) NOT NULL COMMENT '编号',
	test_sort int COMMENT '排序号',
	test_data_id varchar(64) COMMENT '父表主键',
	test_input varchar(200) COMMENT '单行文本',
	test_textarea varchar(200) COMMENT '多行文本',
	test_select varchar(10) COMMENT '下拉框',
	test_select_multiple varchar(200) COMMENT '下拉多选',
	test_radio varchar(10) COMMENT '单选框',
	test_checkbox varchar(200) COMMENT '复选框',
	test_date datetime COMMENT '日期选择',
	test_datetime datetime COMMENT '日期时间',
	test_user_code varchar(64) COMMENT '用户选择',
	test_office_code varchar(64) COMMENT '机构选择',
	test_area_code varchar(64) COMMENT '区域选择',
	test_area_name varchar(100) COMMENT '区域名称',
	PRIMARY KEY (id)
) COMMENT = '测试数据子表';


-- 测试树表
CREATE TABLE test_tree
(
	tree_code varchar(64) NOT NULL COMMENT '节点编码',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(1000) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '本级排序号（升序）',
	tree_sorts varchar(1000) NOT NULL COMMENT '所有级别排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(1000) NOT NULL COMMENT '全节点名',
	tree_name varchar(200) NOT NULL COMMENT '节点名称',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (tree_code)
) COMMENT = '测试树表';



