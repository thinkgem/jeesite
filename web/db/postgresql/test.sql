
/* Drop Tables */

DROP TABLE IF EXISTS test_data;
DROP TABLE IF EXISTS test_data_child;
DROP TABLE IF EXISTS test_tree;




/* Create Tables */

-- 测试数据
CREATE TABLE test_data
(
	id varchar(64) NOT NULL,
	test_input varchar(200),
	test_textarea varchar(200),
	test_select varchar(10),
	test_select_multiple varchar(200),
	test_radio varchar(10),
	test_checkbox varchar(200),
	test_date timestamp,
	test_datetime timestamp,
	test_user_code varchar(64),
	test_office_code varchar(64),
	test_area_code varchar(64),
	test_area_name varchar(100),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (id)
) WITHOUT OIDS;


-- 测试数据子表
CREATE TABLE test_data_child
(
	id varchar(64) NOT NULL,
	test_sort int,
	test_data_id varchar(64),
	test_input varchar(200),
	test_textarea varchar(200),
	test_select varchar(10),
	test_select_multiple varchar(200),
	test_radio varchar(10),
	test_checkbox varchar(200),
	test_date timestamp,
	test_datetime timestamp,
	test_user_code varchar(64),
	test_office_code varchar(64),
	test_area_code varchar(64),
	test_area_name varchar(100),
	PRIMARY KEY (id)
) WITHOUT OIDS;


-- 测试树表
CREATE TABLE test_tree
(
	tree_code varchar(64) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(1000) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(1000) NOT NULL,
	tree_name varchar(200) NOT NULL,
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (tree_code)
) WITHOUT OIDS;



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
COMMENT ON COLUMN test_data.test_office_code IS '机构选择';
COMMENT ON COLUMN test_data.test_area_code IS '区域选择';
COMMENT ON COLUMN test_data.test_area_name IS '区域名称';
COMMENT ON COLUMN test_data.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN test_data.create_by IS '创建者';
COMMENT ON COLUMN test_data.create_date IS '创建时间';
COMMENT ON COLUMN test_data.update_by IS '更新者';
COMMENT ON COLUMN test_data.update_date IS '更新时间';
COMMENT ON COLUMN test_data.remarks IS '备注信息';
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
COMMENT ON COLUMN test_data_child.test_office_code IS '机构选择';
COMMENT ON COLUMN test_data_child.test_area_code IS '区域选择';
COMMENT ON COLUMN test_data_child.test_area_name IS '区域名称';
COMMENT ON TABLE test_tree IS '测试树表';
COMMENT ON COLUMN test_tree.tree_code IS '节点编码';
COMMENT ON COLUMN test_tree.parent_code IS '父级编号';
COMMENT ON COLUMN test_tree.parent_codes IS '所有父级编号';
COMMENT ON COLUMN test_tree.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN test_tree.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN test_tree.tree_leaf IS '是否最末级';
COMMENT ON COLUMN test_tree.tree_level IS '层次级别';
COMMENT ON COLUMN test_tree.tree_names IS '全节点名';
COMMENT ON COLUMN test_tree.tree_name IS '节点名称';
COMMENT ON COLUMN test_tree.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN test_tree.create_by IS '创建者';
COMMENT ON COLUMN test_tree.create_date IS '创建时间';
COMMENT ON COLUMN test_tree.update_by IS '更新者';
COMMENT ON COLUMN test_tree.update_date IS '更新时间';
COMMENT ON COLUMN test_tree.remarks IS '备注信息';



