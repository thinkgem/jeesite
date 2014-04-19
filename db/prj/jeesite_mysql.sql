SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Indexes */

DROP INDEX prj_project_id ON prj_project;



/* Drop Tables */

DROP TABLE prj_project;




/* Create Tables */

CREATE TABLE prj_project
(
	id varchar(64) NOT NULL COMMENT '编号',
	name varchar(255) COMMENT '名称',
	root_package varchar(255) COMMENT '包名',
	erm_path varchar(2000) COMMENT '数据文件路径',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) NOT NULL COMMENT '删除标志',
	PRIMARY KEY (id)
) COMMENT = '项目';



/* Create Indexes */

CREATE INDEX prj_project_id ON prj_project (id ASC);



