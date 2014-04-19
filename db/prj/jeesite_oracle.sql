
/* Drop Indexes */

DROP INDEX prj_project_id;



/* Drop Tables */

DROP TABLE prj_project;




/* Create Tables */

CREATE TABLE prj_project
(
	id varchar2(64) NOT NULL,
	name varchar2(255),
	root_package varchar2(255),
	erm_path varchar2(2000),
	create_by varchar2(64),
	create_date timestamp,
	update_by varchar2(64),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX prj_project_id ON prj_project (id);



/* Comments */

COMMENT ON TABLE prj_project IS '项目';
COMMENT ON COLUMN prj_project.id IS '编号';
COMMENT ON COLUMN prj_project.name IS '名称';
COMMENT ON COLUMN prj_project.root_package IS '包名';
COMMENT ON COLUMN prj_project.erm_path IS '数据文件路径';
COMMENT ON COLUMN prj_project.create_by IS '创建者';
COMMENT ON COLUMN prj_project.create_date IS '创建时间';
COMMENT ON COLUMN prj_project.update_by IS '更新者';
COMMENT ON COLUMN prj_project.update_date IS '更新时间';
COMMENT ON COLUMN prj_project.remarks IS '备注信息';
COMMENT ON COLUMN prj_project.del_flag IS '删除标志';



