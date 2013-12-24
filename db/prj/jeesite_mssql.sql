
/* Drop Tables */

DROP TABLE prj_project;




/* Create Tables */

CREATE TABLE prj_project
(
	-- 编号
	id bigint NOT NULL IDENTITY ,
	-- 名称
	name varchar(255),
	-- 例如：com.thinkgem.jeesite
	root_package varchar(255),
	-- 数据文件路径
	erm_path varchar(2000),
	-- 创建者
	create_by bigint,
	-- 创建时间
	create_date datetime,
	-- 更新者
	update_by bigint,
	-- 更新时间
	update_date datetime,
	-- 备注信息
	remarks varchar(255),
	-- 删除标志
	del_flag char(1) NOT NULL,
	PRIMARY KEY (id)
);
