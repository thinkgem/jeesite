

/* Create Tables */

-- APP意见反馈
CREATE TABLE [js_app_comment]
(
	[id] varchar(64) NOT NULL,
	[category] varchar(10),
	[content] nvarchar(500),
	[contact] nvarchar(200),
	[status] char(1) DEFAULT '0' NOT NULL,
	[create_by] varchar(64) NOT NULL,
	[create_date] datetime NOT NULL,
	[update_by] varchar(64) NOT NULL,
	[update_date] datetime NOT NULL,
	[remarks] nvarchar(500),
	[create_by_name] varchar(200),
	[device_info] varchar(1000),
	[reply_date] date,
	[reply_content] nvarchar(500),
	[reply_user_code] varchar(64),
	[reply_user_name] varchar(200),
	PRIMARY KEY ([id])
);


-- APP升级版本
CREATE TABLE [js_app_upgrade]
(
	[id] varchar(64) NOT NULL,
	[app_code] varchar(64),
	[up_title] nvarchar(200),
	[up_content] nvarchar(1000),
	[up_version] numeric,
	[up_type] char(1),
	[up_date] date,
	[apk_url] varchar(500),
	[res_url] varchar(500),
	[status] char(1) DEFAULT '0' NOT NULL,
	[create_by] varchar(64) NOT NULL,
	[create_date] datetime NOT NULL,
	[update_by] varchar(64) NOT NULL,
	[update_date] datetime NOT NULL,
	[remarks] nvarchar(500),
	PRIMARY KEY ([id])
);



