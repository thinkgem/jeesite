

/* Drop Tables */

DROP TABLE test_data;
DROP TABLE test_data_child;
DROP TABLE test_data_main;
DROP TABLE test_tree;




/* Create Tables */

CREATE TABLE test_data
(
	id varchar(64) NOT NULL,
	user_id varchar(64),
	office_id varchar(64),
	area_id varchar(64),
	name varchar(100),
	sex char(1),
	in_date smalldatetime,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE test_data_child
(
	id varchar(64) NOT NULL,
	test_data_main_id varchar(64),
	name varchar(100),
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE test_data_main
(
	id varchar(64) NOT NULL,
	user_id varchar(64),
	office_id varchar(64),
	area_id varchar(64),
	name varchar(100),
	sex char(1),
	in_date smalldatetime,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE test_tree
(
	id varchar(64) NOT NULL,
	parent_id varchar(64) NOT NULL,
	parent_ids varchar(2000) NOT NULL,
	name varchar(100) NOT NULL,
	sort decimal(10,0) NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date datetime NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date datetime NOT NULL,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX test_data_del_flag ON test_data (del_flag ASC);
CREATE INDEX test_data_child_del_flag ON test_data_child (del_flag ASC);
CREATE INDEX test_data_main_del_flag ON test_data_main (del_flag ASC);
CREATE INDEX test_tree_del_flag ON test_tree (del_flag ASC);
CREATE INDEX test_data_parent_id ON test_tree (parent_id ASC);
/*CREATE INDEX test_data_parent_ids ON test_tree (parent_ids ASC);*/



