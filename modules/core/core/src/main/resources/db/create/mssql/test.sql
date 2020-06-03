
/* Drop Tables */

IF OBJECT_ID('[test_data]') IS NOT NULL DROP TABLE [test_data];
IF OBJECT_ID('[test_data_child]') IS NOT NULL DROP TABLE [test_data_child];
IF OBJECT_ID('[test_tree]') IS NOT NULL DROP TABLE [test_tree];




/* Create Tables */

-- 测试数据
CREATE TABLE [test_data]
(
	[id] varchar(64) NOT NULL,
	[test_input] varchar(200),
	[test_textarea] nvarchar(200),
	[test_select] varchar(10),
	[test_select_multiple] varchar(200),
	[test_radio] varchar(10),
	[test_checkbox] varchar(200),
	[test_date] datetime,
	[test_datetime] datetime,
	[test_user_code] varchar(64),
	[test_office_code] varchar(64),
	[test_area_code] varchar(64),
	[test_area_name] nvarchar(100),
	[status] char(1) DEFAULT '0' NOT NULL,
	[create_by] varchar(64) NOT NULL,
	[create_date] datetime NOT NULL,
	[update_by] varchar(64) NOT NULL,
	[update_date] datetime NOT NULL,
	[remarks] nvarchar(500),
	PRIMARY KEY ([id])
);


-- 测试数据子表
CREATE TABLE [test_data_child]
(
	[id] varchar(64) NOT NULL,
	[test_sort] int,
	[test_data_id] varchar(64),
	[test_input] varchar(200),
	[test_textarea] nvarchar(200),
	[test_select] varchar(10),
	[test_select_multiple] varchar(200),
	[test_radio] varchar(10),
	[test_checkbox] varchar(200),
	[test_date] datetime,
	[test_datetime] datetime,
	[test_user_code] varchar(64),
	[test_office_code] varchar(64),
	[test_area_code] varchar(64),
	[test_area_name] nvarchar(100),
	PRIMARY KEY ([id])
);


-- 测试树表
CREATE TABLE [test_tree]
(
	[tree_code] varchar(64) NOT NULL,
	[parent_code] varchar(64) NOT NULL,
	[parent_codes] varchar(1000) NOT NULL,
	[tree_sort] decimal(10) NOT NULL,
	[tree_sorts] varchar(1000) NOT NULL,
	[tree_leaf] char(1) NOT NULL,
	[tree_level] decimal(4) NOT NULL,
	[tree_names] varchar(1000) NOT NULL,
	[tree_name] nvarchar(200) NOT NULL,
	[status] char(1) DEFAULT '0' NOT NULL,
	[create_by] varchar(64) NOT NULL,
	[create_date] datetime NOT NULL,
	[update_by] varchar(64) NOT NULL,
	[update_date] datetime NOT NULL,
	[remarks] nvarchar(500),
	PRIMARY KEY ([tree_code])
);



