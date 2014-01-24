
/* Drop Indexes */

DROP INDEX prj_project_id;



/* Drop Tables */

DROP TABLE prj_project;




/* Create Tables */

CREATE TABLE prj_project
(
	id varchar(64) NOT NULL,
	name varchar(255),
	root_package varchar(255),
	erm_path varchar(2000),
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX prj_project_id ON prj_project (id);



