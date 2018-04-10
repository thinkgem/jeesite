
/* Drop Tables */

IF OBJECT_ID('[js_gen_table_column]') IS NOT NULL DROP TABLE [js_gen_table_column];
IF OBJECT_ID('[js_gen_table]') IS NOT NULL DROP TABLE [js_gen_table];




/* Create Tables */

-- 代码生成表
CREATE TABLE [js_gen_table]
(
	[table_name] varchar(64) NOT NULL,
	[class_name] varchar(100) NOT NULL,
	[comments] nvarchar(500) NOT NULL,
	[parent_table_name] varchar(64),
	[parent_table_fk_name] varchar(64),
	[tpl_category] varchar(200),
	[package_name] varchar(500),
	[module_name] varchar(30),
	[sub_module_name] varchar(30),
	[function_name] nvarchar(200),
	[function_name_simple] nvarchar(50),
	[function_author] nvarchar(50),
	[gen_base_dir] nvarchar(1000),
	[options] nvarchar(1000),
	[create_by] varchar(64) NOT NULL,
	[create_date] datetime NOT NULL,
	[update_by] varchar(64) NOT NULL,
	[update_date] datetime NOT NULL,
	[remarks] nvarchar(500),
	PRIMARY KEY ([table_name])
);


-- 代码生成表列
CREATE TABLE [js_gen_table_column]
(
	[id] varchar(64) NOT NULL,
	[table_name] varchar(64) NOT NULL,
	[column_name] varchar(64) NOT NULL,
	[column_sort] decimal(10),
	[column_type] varchar(100) NOT NULL,
	[column_label] nvarchar(50),
	[comments] nvarchar(500) NOT NULL,
	[attr_name] varchar(200) NOT NULL,
	[attr_type] varchar(200) NOT NULL,
	[is_pk] char(1),
	[is_null] char(1),
	[is_insert] char(1),
	[is_update] char(1),
	[is_list] char(1),
	[is_query] char(1),
	[query_type] varchar(200),
	[is_edit] char(1),
	[show_type] varchar(200),
	[options] nvarchar(1000),
	PRIMARY KEY ([id])
);



/* Create Indexes */

CREATE INDEX [idx_gen_table_ptn] ON [js_gen_table] ([parent_table_name]);
CREATE INDEX [idx_gen_table_column_tn] ON [js_gen_table_column] ([table_name]);



