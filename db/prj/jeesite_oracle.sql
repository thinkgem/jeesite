
/* Drop Tables */

DROP TABLE prj_project CASCADE CONSTRAINTS;




/* Create Tables */

CREATE TABLE prj_project
(
	-- 编号
	id number(19,0) NOT NULL,
	-- 名称
	name varchar2(255),
	-- 例如：com.thinkgem.jeesite
	root_package varchar2(255),
	-- 数据文件路径
	erm_path varchar2(2000),
	-- 创建者
	create_by number(19,0),
	-- 创建时间
	create_date timestamp,
	-- 更新者
	update_by number(19,0),
	-- 更新时间
	update_date timestamp,
	-- 备注信息
	remarks varchar2(255),
	-- 删除标志
	del_flag char(1) NOT NULL,
	PRIMARY KEY (id)
);



/* Comments */

COMMENT ON COLUMN prj_project.id IS '编号';
COMMENT ON COLUMN prj_project.name IS '名称';
COMMENT ON COLUMN prj_project.root_package IS '例如：com.thinkgem.jeesite';
COMMENT ON COLUMN prj_project.erm_path IS '数据文件路径';
COMMENT ON COLUMN prj_project.create_by IS '创建者';
COMMENT ON COLUMN prj_project.create_date IS '创建时间';
COMMENT ON COLUMN prj_project.update_by IS '更新者';
COMMENT ON COLUMN prj_project.update_date IS '更新时间';
COMMENT ON COLUMN prj_project.remarks IS '备注信息';
COMMENT ON COLUMN prj_project.del_flag IS '删除标志';



