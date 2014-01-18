
/* Drop Tables */

DROP TABLE cms_article_data;
DROP TABLE cms_article;
DROP TABLE cms_comment;
DROP TABLE cms_link;
DROP TABLE cms_category;
DROP TABLE cms_guestbook;
DROP TABLE cms_site;




/* Create Tables */

CREATE TABLE cms_article
(
	id varchar(64) NOT NULL,
	category_id varchar(64) NOT NULL,
	title varchar(255) NOT NULL,
	link varchar(255),
	color varchar(50),
	image varchar(255),
	keywords varchar(255),
	description varchar(255),
	weight int DEFAULT 0,
	weight_date datetime,
	hits int DEFAULT 0,
	posid varchar(10),
	custom_content_view varchar(255),
	view_config text,
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_article_data
(
	id varchar(64) NOT NULL,
	content text,
	copyfrom varchar(255),
	relation varchar(255),
	allow_comment char(1),
	PRIMARY KEY (id)
);


CREATE TABLE cms_category
(
	id varchar(64) NOT NULL,
	site_id varchar(64) DEFAULT '1',
	office_id varchar(64),
	parent_id varchar(64) NOT NULL,
	parent_ids varchar(2000) NOT NULL,
	module varchar(20),
	name varchar(100) NOT NULL,
	image varchar(255),
	href varchar(255),
	target varchar(20),
	description varchar(255),
	keywords varchar(255),
	sort int DEFAULT 30,
	in_menu char(1) DEFAULT '1',
	in_list char(1) DEFAULT '1',
	show_modes char(1) DEFAULT '0',
	allow_comment char(1),
	is_audit char(1),
	custom_list_view varchar(255),
	custom_content_view varchar(255),
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	view_config text,
	PRIMARY KEY (id)
);


CREATE TABLE cms_comment
(
	id varchar(64) NOT NULL,
	category_id varchar(64) NOT NULL,
	content_id varchar(64) NOT NULL,
	title varchar(255),
	content varchar(255),
	name varchar(100),
	ip varchar(100),
	create_date datetime NOT NULL,
	audit_user_id varchar(64),
	audit_date datetime,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_guestbook
(
	id varchar(64) NOT NULL,
	type char(1) NOT NULL,
	content varchar(255) NOT NULL,
	name varchar(100) NOT NULL,
	email varchar(100) NOT NULL,
	phone varchar(100) NOT NULL,
	workunit varchar(100) NOT NULL,
	ip varchar(100) NOT NULL,
	create_date datetime NOT NULL,
	re_user_id varchar(64),
	re_date datetime,
	re_content varchar(100),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_link
(
	id varchar(64) NOT NULL,
	category_id varchar(64) NOT NULL,
	title varchar(255) NOT NULL,
	color varchar(50),
	image varchar(255),
	href varchar(255),
	weight int DEFAULT 0,
	weight_date datetime,
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_site
(
	id varchar(64) NOT NULL,
	name varchar(100) NOT NULL,
	title varchar(100) NOT NULL,
	logo varchar(255),
	domain varchar(255),
	description varchar(255),
	keywords varchar(255),
	theme varchar(255) DEFAULT 'default',
	copyright text,
	custom_index_view varchar(255),
	create_by varchar(64),
	create_date datetime,
	update_by varchar(64),
	update_date datetime,
	remarks varchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX cms_article_create_by ON cms_article (create_by);
CREATE INDEX cms_article_title ON cms_article (title);
CREATE INDEX cms_article_keywords ON cms_article (keywords);
CREATE INDEX cms_article_del_flag ON cms_article (del_flag);
CREATE INDEX cms_article_weight ON cms_article (weight);
CREATE INDEX cms_article_update_date ON cms_article (update_date);
CREATE INDEX cms_article_category_id ON cms_article (category_id);
CREATE INDEX cms_article_id ON cms_article (id);
CREATE INDEX cms_article_data_id ON cms_article_data (id);
CREATE INDEX cms_category_parent_id ON cms_category (parent_id);
CREATE INDEX cms_category_parent_ids ON cms_category (parent_ids);
CREATE INDEX cms_category_module ON cms_category (module);
CREATE INDEX cms_category_name ON cms_category (name);
CREATE INDEX cms_category_sort ON cms_category (sort);
CREATE INDEX cms_category_del_flag ON cms_category (del_flag);
CREATE INDEX cms_category_office_id ON cms_category (office_id);
CREATE INDEX cms_category_site_id ON cms_category (site_id);
CREATE INDEX cms_category_id ON cms_category (id);
CREATE INDEX cms_comment_category_id ON cms_comment (category_id);
CREATE INDEX cms_comment_content_id ON cms_comment (content_id);
CREATE INDEX cms_comment_status ON cms_comment (del_flag);
CREATE INDEX cms_comment_id ON cms_comment (id);
CREATE INDEX cms_guestbook_del_flag ON cms_guestbook (del_flag);
CREATE INDEX cms_site_id ON cms_guestbook (id);
CREATE INDEX cms_link_category_id ON cms_link (category_id);
CREATE INDEX cms_link_title ON cms_link (title);
CREATE INDEX cms_link_del_flag ON cms_link (del_flag);
CREATE INDEX cms_link_weight ON cms_link (weight);
CREATE INDEX cms_link_create_by ON cms_link (create_by);
CREATE INDEX cms_link_update_date ON cms_link (update_date);
CREATE INDEX cms_link_id ON cms_link (id);
CREATE INDEX cms_site_del_flag ON cms_site (del_flag);
CREATE INDEX cms_site_id ON cms_site (id);



