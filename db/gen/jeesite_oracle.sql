
/* Drop Tables */

DROP TABLE gen_scheme;
DROP TABLE gen_table_column;
DROP TABLE gen_table;
DROP TABLE gen_template;




/* Create Tables */

CREATE TABLE gen_scheme
(
	id varchar2(64) NOT NULL,
	name nvarchar2(200),
	category varchar2(2000),
	package_name varchar2(500),
	module_name varchar2(30),
	sub_module_name varchar2(30),
	function_name nvarchar2(500),
	function_name_simple nvarchar2(100),
	function_author nvarchar2(100),
	gen_table_id varchar2(200),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE gen_table
(
	id varchar2(64) NOT NULL,
	name nvarchar2(200),
	comments nvarchar2(500),
	class_name varchar2(100),
	parent_table varchar2(200),
	parent_table_fk varchar2(100),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE gen_table_column
(
	id varchar2(64) NOT NULL,
	gen_table_id varchar2(64),
	name nvarchar2(200),
	comments nvarchar2(500),
	jdbc_type varchar2(100),
	java_type varchar2(500),
	java_field varchar2(200),
	is_pk char(1),
	is_null char(1),
	is_insert char(1),
	is_edit char(1),
	is_list char(1),
	is_query char(1),
	query_type varchar2(200),
	show_type varchar2(200),
	dict_type varchar2(200),
	settings nvarchar2(2000),
	sort number,
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE gen_template
(
	id varchar2(64) NOT NULL,
	name nvarchar2(200),
	category varchar2(2000),
	file_path varchar2(500),
	file_name varchar2(200),
	content clob,
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks nvarchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX gen_scheme_del_flag ON gen_scheme (del_flag);
CREATE INDEX gen_table_name ON gen_table (name);
CREATE INDEX gen_table_del_flag ON gen_table (del_flag);
CREATE INDEX gen_table_column_table_id ON gen_table_column (gen_table_id);
CREATE INDEX gen_table_column_name ON gen_table_column (name);
CREATE INDEX gen_table_column_sort ON gen_table_column (sort);
CREATE INDEX gen_table_column_del_flag ON gen_table_column (del_flag);
CREATE INDEX gen_template_del_falg ON gen_template (del_flag);



/* Comments */

COMMENT ON TABLE gen_scheme IS '生成方案';
COMMENT ON COLUMN gen_scheme.id IS '编号';
COMMENT ON COLUMN gen_scheme.name IS '名称';
COMMENT ON COLUMN gen_scheme.category IS '分类';
COMMENT ON COLUMN gen_scheme.package_name IS '生成包路径';
COMMENT ON COLUMN gen_scheme.module_name IS '生成模块名';
COMMENT ON COLUMN gen_scheme.sub_module_name IS '生成子模块名';
COMMENT ON COLUMN gen_scheme.function_name IS '生成功能名';
COMMENT ON COLUMN gen_scheme.function_name_simple IS '生成功能名（简写）';
COMMENT ON COLUMN gen_scheme.function_author IS '生成功能作者';
COMMENT ON COLUMN gen_scheme.gen_table_id IS '生成表编号';
COMMENT ON COLUMN gen_scheme.create_by IS '创建者';
COMMENT ON COLUMN gen_scheme.create_date IS '创建时间';
COMMENT ON COLUMN gen_scheme.update_by IS '更新者';
COMMENT ON COLUMN gen_scheme.update_date IS '更新时间';
COMMENT ON COLUMN gen_scheme.remarks IS '备注信息';
COMMENT ON COLUMN gen_scheme.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE gen_table IS '业务表';
COMMENT ON COLUMN gen_table.id IS '编号';
COMMENT ON COLUMN gen_table.name IS '名称';
COMMENT ON COLUMN gen_table.comments IS '描述';
COMMENT ON COLUMN gen_table.class_name IS '实体类名称';
COMMENT ON COLUMN gen_table.parent_table IS '关联父表';
COMMENT ON COLUMN gen_table.parent_table_fk IS '关联父表外键';
COMMENT ON COLUMN gen_table.create_by IS '创建者';
COMMENT ON COLUMN gen_table.create_date IS '创建时间';
COMMENT ON COLUMN gen_table.update_by IS '更新者';
COMMENT ON COLUMN gen_table.update_date IS '更新时间';
COMMENT ON COLUMN gen_table.remarks IS '备注信息';
COMMENT ON COLUMN gen_table.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE gen_table_column IS '业务表字段';
COMMENT ON COLUMN gen_table_column.id IS '编号';
COMMENT ON COLUMN gen_table_column.gen_table_id IS '归属表编号';
COMMENT ON COLUMN gen_table_column.name IS '名称';
COMMENT ON COLUMN gen_table_column.comments IS '描述';
COMMENT ON COLUMN gen_table_column.jdbc_type IS '列的数据类型的字节长度';
COMMENT ON COLUMN gen_table_column.java_type IS 'JAVA类型';
COMMENT ON COLUMN gen_table_column.java_field IS 'JAVA字段名';
COMMENT ON COLUMN gen_table_column.is_pk IS '是否主键';
COMMENT ON COLUMN gen_table_column.is_null IS '是否可为空';
COMMENT ON COLUMN gen_table_column.is_insert IS '是否为插入字段';
COMMENT ON COLUMN gen_table_column.is_edit IS '是否编辑字段';
COMMENT ON COLUMN gen_table_column.is_list IS '是否列表字段';
COMMENT ON COLUMN gen_table_column.is_query IS '是否查询字段';
COMMENT ON COLUMN gen_table_column.query_type IS '查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）';
COMMENT ON COLUMN gen_table_column.show_type IS '字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）';
COMMENT ON COLUMN gen_table_column.dict_type IS '字典类型';
COMMENT ON COLUMN gen_table_column.settings IS '其它设置（扩展字段JSON）';
COMMENT ON COLUMN gen_table_column.sort IS '排序（升序）';
COMMENT ON COLUMN gen_table_column.create_by IS '创建者';
COMMENT ON COLUMN gen_table_column.create_date IS '创建时间';
COMMENT ON COLUMN gen_table_column.update_by IS '更新者';
COMMENT ON COLUMN gen_table_column.update_date IS '更新时间';
COMMENT ON COLUMN gen_table_column.remarks IS '备注信息';
COMMENT ON COLUMN gen_table_column.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE gen_template IS '代码模板表';
COMMENT ON COLUMN gen_template.id IS '编号';
COMMENT ON COLUMN gen_template.name IS '名称';
COMMENT ON COLUMN gen_template.category IS '分类';
COMMENT ON COLUMN gen_template.file_path IS '生成文件路径';
COMMENT ON COLUMN gen_template.file_name IS '生成文件名';
COMMENT ON COLUMN gen_template.content IS '内容';
COMMENT ON COLUMN gen_template.create_by IS '创建者';
COMMENT ON COLUMN gen_template.create_date IS '创建时间';
COMMENT ON COLUMN gen_template.update_by IS '更新者';
COMMENT ON COLUMN gen_template.update_date IS '更新时间';
COMMENT ON COLUMN gen_template.remarks IS '备注信息';
COMMENT ON COLUMN gen_template.del_flag IS '删除标记（0：正常；1：删除）';



