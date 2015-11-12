

/* Drop Tables*/

DROP TABLE gen_scheme;
DROP TABLE gen_table_column;
DROP TABLE gen_table;
DROP TABLE gen_template; 




/* Create Tables */

CREATE TABLE gen_scheme
(
	id varchar(64) NOT NULL,
	name varchar(200),
	category varchar(2000),
	package_name varchar(500),
	module_name varchar(30),
	sub_module_name varchar(30),
	function_name varchar(500),
	function_name_simple varchar(100),
	function_author varchar(100),
	gen_table_id varchar(200),
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE gen_table
(
	id varchar(64) NOT NULL,
	name varchar(200),
	comments varchar(500),
	class_name varchar(100),
	parent_table varchar(200),
	parent_table_fk varchar(100),
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE gen_table_column
(
	id varchar(64) NOT NULL,
	gen_table_id varchar(64),
	name varchar(200),
	comments varchar(500),
	jdbc_type varchar(100),
	java_type varchar(500),
	java_field varchar(200),
	is_pk char(1),
	is_null char(1),
	is_insert char(1),
	is_edit char(1),
	is_list char(1),
	is_query char(1),
	query_type varchar(200),
	show_type varchar(200),
	dict_type varchar(200),
	settings varchar(2000),
	sort decimal,
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE gen_template
(
	id varchar(64) NOT NULL,
	name varchar(200),
	category varchar(2000),
	file_path varchar(500),
	file_name varchar(200),
	content text,
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX gen_scheme_del_flag ON gen_scheme (del_flag ASC);
CREATE INDEX gen_table_name ON gen_table (name ASC);
CREATE INDEX gen_table_del_flag ON gen_table (del_flag ASC);
CREATE INDEX gen_table_column_table_id ON gen_table_column (gen_table_id ASC);
CREATE INDEX gen_table_column_name ON gen_table_column (name ASC);
CREATE INDEX gen_table_column_sort ON gen_table_column (sort ASC);
CREATE INDEX gen_table_column_del_flag ON gen_table_column (del_flag ASC);
CREATE INDEX gen_template_del_falg ON gen_template (del_flag ASC);



