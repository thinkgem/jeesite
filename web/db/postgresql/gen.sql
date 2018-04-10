
/* Drop Tables */

DROP TABLE IF EXISTS js_gen_table_column;
DROP TABLE IF EXISTS js_gen_table;




/* Create Tables */

-- 代码生成表
CREATE TABLE js_gen_table
(
	table_name varchar(64) NOT NULL,
	class_name varchar(100) NOT NULL,
	comments varchar(500) NOT NULL,
	parent_table_name varchar(64),
	parent_table_fk_name varchar(64),
	tpl_category varchar(200),
	package_name varchar(500),
	module_name varchar(30),
	sub_module_name varchar(30),
	function_name varchar(200),
	function_name_simple varchar(50),
	function_author varchar(50),
	gen_base_dir varchar(1000),
	options varchar(1000),
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks varchar(500),
	PRIMARY KEY (table_name)
) WITHOUT OIDS;


-- 代码生成表列
CREATE TABLE js_gen_table_column
(
	id varchar(64) NOT NULL,
	table_name varchar(64) NOT NULL,
	column_name varchar(64) NOT NULL,
	column_sort decimal(10),
	column_type varchar(100) NOT NULL,
	column_label varchar(50),
	comments varchar(500) NOT NULL,
	attr_name varchar(200) NOT NULL,
	attr_type varchar(200) NOT NULL,
	is_pk char(1),
	is_null char(1),
	is_insert char(1),
	is_update char(1),
	is_list char(1),
	is_query char(1),
	query_type varchar(200),
	is_edit char(1),
	show_type varchar(200),
	options varchar(1000),
	PRIMARY KEY (id)
) WITHOUT OIDS;



/* Create Indexes */

CREATE INDEX idx_gen_table_ptn ON js_gen_table (parent_table_name);
CREATE INDEX idx_gen_table_column_tn ON js_gen_table_column (table_name);



/* Comments */

COMMENT ON TABLE js_gen_table IS '代码生成表';
COMMENT ON COLUMN js_gen_table.table_name IS '表名';
COMMENT ON COLUMN js_gen_table.class_name IS '实体类名称';
COMMENT ON COLUMN js_gen_table.comments IS '表说明';
COMMENT ON COLUMN js_gen_table.parent_table_name IS '关联父表的表名';
COMMENT ON COLUMN js_gen_table.parent_table_fk_name IS '本表关联父表的外键名';
COMMENT ON COLUMN js_gen_table.tpl_category IS '使用的模板';
COMMENT ON COLUMN js_gen_table.package_name IS '生成包路径';
COMMENT ON COLUMN js_gen_table.module_name IS '生成模块名';
COMMENT ON COLUMN js_gen_table.sub_module_name IS '生成子模块名';
COMMENT ON COLUMN js_gen_table.function_name IS '生成功能名';
COMMENT ON COLUMN js_gen_table.function_name_simple IS '生成功能名（简写）';
COMMENT ON COLUMN js_gen_table.function_author IS '生成功能作者';
COMMENT ON COLUMN js_gen_table.gen_base_dir IS '生成基础路径';
COMMENT ON COLUMN js_gen_table.options IS '其它生成选项';
COMMENT ON COLUMN js_gen_table.create_by IS '创建者';
COMMENT ON COLUMN js_gen_table.create_date IS '创建时间';
COMMENT ON COLUMN js_gen_table.update_by IS '更新者';
COMMENT ON COLUMN js_gen_table.update_date IS '更新时间';
COMMENT ON COLUMN js_gen_table.remarks IS '备注信息';
COMMENT ON TABLE js_gen_table_column IS '代码生成表列';
COMMENT ON COLUMN js_gen_table_column.id IS '编号';
COMMENT ON COLUMN js_gen_table_column.table_name IS '表名';
COMMENT ON COLUMN js_gen_table_column.column_name IS '列名';
COMMENT ON COLUMN js_gen_table_column.column_sort IS '列排序（升序）';
COMMENT ON COLUMN js_gen_table_column.column_type IS '类型';
COMMENT ON COLUMN js_gen_table_column.column_label IS '列标签名';
COMMENT ON COLUMN js_gen_table_column.comments IS '列备注说明';
COMMENT ON COLUMN js_gen_table_column.attr_name IS '类的属性名';
COMMENT ON COLUMN js_gen_table_column.attr_type IS '类的属性类型';
COMMENT ON COLUMN js_gen_table_column.is_pk IS '是否主键';
COMMENT ON COLUMN js_gen_table_column.is_null IS '是否可为空';
COMMENT ON COLUMN js_gen_table_column.is_insert IS '是否插入字段';
COMMENT ON COLUMN js_gen_table_column.is_update IS '是否更新字段';
COMMENT ON COLUMN js_gen_table_column.is_list IS '是否列表字段';
COMMENT ON COLUMN js_gen_table_column.is_query IS '是否查询字段';
COMMENT ON COLUMN js_gen_table_column.query_type IS '查询方式';
COMMENT ON COLUMN js_gen_table_column.is_edit IS '是否编辑字段';
COMMENT ON COLUMN js_gen_table_column.show_type IS '表单类型';
COMMENT ON COLUMN js_gen_table_column.options IS '其它生成选项';



