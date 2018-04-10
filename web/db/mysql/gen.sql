SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS js_gen_table_column;
DROP TABLE IF EXISTS js_gen_table;




/* Create Tables */

-- 代码生成表
CREATE TABLE js_gen_table
(
	table_name varchar(64) NOT NULL COMMENT '表名',
	class_name varchar(100) NOT NULL COMMENT '实体类名称',
	comments varchar(500) NOT NULL COMMENT '表说明',
	parent_table_name varchar(64) COMMENT '关联父表的表名',
	parent_table_fk_name varchar(64) COMMENT '本表关联父表的外键名',
	tpl_category varchar(200) COMMENT '使用的模板',
	package_name varchar(500) COMMENT '生成包路径',
	module_name varchar(30) COMMENT '生成模块名',
	sub_module_name varchar(30) COMMENT '生成子模块名',
	function_name varchar(200) COMMENT '生成功能名',
	function_name_simple varchar(50) COMMENT '生成功能名（简写）',
	function_author varchar(50) COMMENT '生成功能作者',
	gen_base_dir varchar(1000) COMMENT '生成基础路径',
	options varchar(1000) COMMENT '其它生成选项',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (table_name)
) COMMENT = '代码生成表';


-- 代码生成表列
CREATE TABLE js_gen_table_column
(
	id varchar(64) NOT NULL COMMENT '编号',
	table_name varchar(64) NOT NULL COMMENT '表名',
	column_name varchar(64) NOT NULL COMMENT '列名',
	column_sort decimal(10) COMMENT '列排序（升序）',
	column_type varchar(100) NOT NULL COMMENT '类型',
	column_label varchar(50) COMMENT '列标签名',
	comments varchar(500) NOT NULL COMMENT '列备注说明',
	attr_name varchar(200) NOT NULL COMMENT '类的属性名',
	attr_type varchar(200) NOT NULL COMMENT '类的属性类型',
	is_pk char(1) COMMENT '是否主键',
	is_null char(1) COMMENT '是否可为空',
	is_insert char(1) COMMENT '是否插入字段',
	is_update char(1) COMMENT '是否更新字段',
	is_list char(1) COMMENT '是否列表字段',
	is_query char(1) COMMENT '是否查询字段',
	query_type varchar(200) COMMENT '查询方式',
	is_edit char(1) COMMENT '是否编辑字段',
	show_type varchar(200) COMMENT '表单类型',
	options varchar(1000) COMMENT '其它生成选项',
	PRIMARY KEY (id)
) COMMENT = '代码生成表列';



/* Create Indexes */

CREATE INDEX idx_gen_table_ptn ON js_gen_table (parent_table_name ASC);
CREATE INDEX idx_gen_table_column_tn ON js_gen_table_column (table_name ASC);



